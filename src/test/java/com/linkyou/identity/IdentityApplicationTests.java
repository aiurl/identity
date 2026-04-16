package com.linkyou.identity;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Timestamp;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class IdentityApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	@Transactional
	void shouldAutoFillAuditFieldsWhenPersistingUserEntity() throws Exception {
		Class<?> entityClass = Class.forName("com.linkyou.identity.infrastructure.persistence.entity.UserEntity");
		Object userEntity = newInstance(entityClass);
		setField(userEntity, "id", "audit-user-1");
		setField(userEntity, "username", "audit-user");
		setField(userEntity, "nickname", "Audit User");
		setField(userEntity, "email", "audit@example.com");
		setField(userEntity, "passwordHash", "hash");
		setField(userEntity, "passwordSalt", "salt");
		setField(userEntity, "phone", "13900000000");
		setField(userEntity, "active", true);

		entityManager.persist(userEntity);
		entityManager.flush();

		Timestamp createdAt = jdbcTemplate.queryForObject(
				"select created_at from sys_users where id = ?",
				Timestamp.class,
				"audit-user-1");
		Timestamp updatedAtBefore = jdbcTemplate.queryForObject(
				"select updated_at from sys_users where id = ?",
				Timestamp.class,
				"audit-user-1");

		org.junit.jupiter.api.Assertions.assertNotNull(createdAt);
		org.junit.jupiter.api.Assertions.assertNotNull(updatedAtBefore);

		Thread.sleep(25L);
		setField(userEntity, "nickname", "Audit User Updated");
		entityManager.flush();
		TestTransaction.flagForCommit();
		TestTransaction.end();

		Timestamp updatedAtAfter = jdbcTemplate.queryForObject(
				"select updated_at from sys_users where id = ?",
				Timestamp.class,
				"audit-user-1");

		org.junit.jupiter.api.Assertions.assertTrue(updatedAtAfter.after(updatedAtBefore));
	}

	@Test
	void shouldRejectUsersRequestWithoutToken() {
		WebTestClient.bindToApplicationContext(applicationContext)
				.build()
				.get()
				.uri("/api/users")
				.exchange()
				.expectStatus().isUnauthorized();
	}

	@Test
	void shouldRegisterLoginAndAccessUsersEndpoint() {
		WebTestClient client = WebTestClient.bindToApplicationContext(applicationContext).build();

		client.post()
				.uri("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue("""
						{
						  \"username\": \"alice\",
						  \"email\": \"alice@example.com\",
						  \"password\": \"P@ssw0rd\"
						}
						""")
				.exchange()
				.expectStatus().isOk();

		String token = loginAndGetToken(client, "alice", "P@ssw0rd");

		client.get()
				.uri("/api/users")
				.header("Authorization", "Bearer " + token)
				.exchange()
				.expectStatus().isOk()
				.expectBody(String.class)
				.value(body -> org.junit.jupiter.api.Assertions.assertTrue(body.contains("\"username\":\"alice\"")));
	}

	@Test
	void shouldRejectInvalidRegisterPayload() {
		WebTestClient client = WebTestClient.bindToApplicationContext(applicationContext).build();

		client.post()
				.uri("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue("""
						{
						  \"username\": \"\",
						  \"nickname\": \"\",
						  \"phone\": \"abc\",
						  \"email\": \"bad-email\",
						  \"password\": \"\"
						}
						""")
				.exchange()
				.expectStatus().isBadRequest();
	}

	@Test
	void shouldRejectDuplicateUsernamePhoneOrEmail() {
		WebTestClient client = WebTestClient.bindToApplicationContext(applicationContext).build();

		client.post()
				.uri("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue("""
						{
						  \"username\": \"david\",
						  \"nickname\": \"David\",
						  \"phone\": \"13800000001\",
						  \"email\": \"david@example.com\",
						  \"password\": \"P@ssw0rd\"
						}
						""")
				.exchange()
				.expectStatus().isOk();

		client.post()
				.uri("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue("""
						{
						  \"username\": \"david\",
						  \"nickname\": \"David2\",
						  \"phone\": \"13800000002\",
						  \"email\": \"david2@example.com\",
						  \"password\": \"P@ssw0rd\"
						}
						""")
				.exchange()
				.expectStatus().isBadRequest();

		client.post()
				.uri("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue("""
						{
						  \"username\": \"david3\",
						  \"nickname\": \"David3\",
						  \"phone\": \"13800000001\",
						  \"email\": \"david3@example.com\",
						  \"password\": \"P@ssw0rd\"
						}
						""")
				.exchange()
				.expectStatus().isBadRequest();

		client.post()
				.uri("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue("""
						{
						  \"username\": \"david4\",
						  \"nickname\": \"David4\",
						  \"phone\": \"13800000004\",
						  \"email\": \"david@example.com\",
						  \"password\": \"P@ssw0rd\"
						}
						""")
				.exchange()
				.expectStatus().isBadRequest();
	}

	@Test
	void shouldForbidRoleManagementForNormalUser() {
		WebTestClient client = WebTestClient.bindToApplicationContext(applicationContext).build();

		client.post()
				.uri("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue("""
						{
						  \"username\": \"bob\",
						  \"email\": \"bob@example.com\",
						  \"password\": \"P@ssw0rd\"
						}
						""")
				.exchange()
				.expectStatus().isOk();

		String token = loginAndGetToken(client, "bob", "P@ssw0rd");

		client.get()
				.uri("/api/roles")
				.header("Authorization", "Bearer " + token)
				.exchange()
				.expectStatus().isForbidden();
	}

	@Test
	void shouldManageUserRolePermissionRelationsAsAdmin() {
		WebTestClient client = WebTestClient.bindToApplicationContext(applicationContext).build();

		client.post()
				.uri("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue("""
						{
						  \"username\": \"admin\",
						  \"email\": \"admin@example.com\",
						  \"password\": \"Admin@123\"
						}
						""")
				.exchange()
				.expectStatus().isOk();

		client.post()
				.uri("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue("""
						{
						  \"username\": \"charlie\",
						  \"email\": \"charlie@example.com\",
						  \"password\": \"P@ssw0rd\"
						}
						""")
				.exchange()
				.expectStatus().isOk();

		String adminToken = loginAndGetToken(client, "admin", "Admin@123");

		String permissionResponse = client.post()
				.uri("/api/permissions")
				.header("Authorization", "Bearer " + adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue("""
						{
						  \"code\": \"USER_ASSIGN_ROLE\",
						  \"description\": \"assign role to user\"
						}
						""")
				.exchange()
				.expectStatus().isOk()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();
		String permissionId = extractJsonField(permissionResponse, "id");

		String roleResponse = client.post()
				.uri("/api/roles")
				.header("Authorization", "Bearer " + adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue("""
						{
						  \"name\": \"MANAGER\",
						  \"description\": \"manager role\"
						}
						""")
				.exchange()
				.expectStatus().isOk()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();
		String roleId = extractJsonField(roleResponse, "id");

		String usersResponse = client.get()
				.uri("/api/users")
				.header("Authorization", "Bearer " + adminToken)
				.exchange()
				.expectStatus().isOk()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();
		String userId = extractUserIdByUsername(usersResponse, "charlie");

		client.post()
				.uri("/api/roles/" + roleId + "/permissions/" + permissionId)
				.header("Authorization", "Bearer " + adminToken)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.permissions[0]").isEqualTo("USER_ASSIGN_ROLE");

		client.post()
				.uri("/api/users/" + userId + "/roles/" + roleId)
				.header("Authorization", "Bearer " + adminToken)
				.exchange()
				.expectStatus().isOk()
				.expectBody(String.class)
				.value(body -> org.junit.jupiter.api.Assertions.assertTrue(body.contains("MANAGER")));
	}

	private String loginAndGetToken(WebTestClient client, String username, String password) {
		String tokenResponse = client.post()
				.uri("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue("""
						{
						  \"username\": \"%s\",
						  \"password\": \"%s\"
						}
						""".formatted(username, password))
				.exchange()
				.expectStatus().isOk()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();

		return extractJsonField(tokenResponse, "token");
	}

	private String extractJsonField(String json, String field) {
		String marker = "\"" + field + "\":\"";
		int start = json.indexOf(marker);
		if (start < 0) {
			throw new IllegalStateException("Field not found: " + field + " in " + json);
		}
		int valueStart = start + marker.length();
		int valueEnd = json.indexOf("\"", valueStart);
		return json.substring(valueStart, valueEnd);
	}

	private Object newInstance(Class<?> type) throws Exception {
		Constructor<?> constructor = type.getDeclaredConstructor();
		constructor.setAccessible(true);
		return constructor.newInstance();
	}

	private void setField(Object target, String fieldName, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	private String extractUserIdByUsername(String json, String username) {
		String lookup = "\"username\":\"" + username + "\"";
		int usernamePos = json.indexOf(lookup);
		if (usernamePos < 0) {
			throw new IllegalStateException("Username not found: " + username + " in " + json);
		}
		String prefix = "\"id\":\"";
		int idPos = json.lastIndexOf(prefix, usernamePos);
		int valueStart = idPos + prefix.length();
		int valueEnd = json.indexOf("\"", valueStart);
		return json.substring(valueStart, valueEnd);
	}

}
