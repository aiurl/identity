package com.linkyou.identity;

import com.linkyou.identity.domain.model.aggregate.User;
import com.linkyou.identity.domain.repository.PermissionRepository;
import com.linkyou.identity.domain.repository.RoleRepository;
import com.linkyou.identity.domain.repository.UserRepository;
import com.linkyou.identity.infrastructure.persistence.repository.JdbcPermissionRepository;
import com.linkyou.identity.infrastructure.persistence.repository.JdbcRoleRepository;
import com.linkyou.identity.infrastructure.persistence.repository.JdbcUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RepositorySwitchingTests {

    @Test
    void shouldUseMysqlRepositoriesWhenConfigured() {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(IdentityApplication.class)
                .run(
                        "--spring.main.web-application-type=none",
                        "--identity.repository.type=mysql",
                        "--spring.datasource.url=jdbc:h2:mem:mysql-switch;MODE=MYSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
                        "--spring.datasource.driver-class-name=org.h2.Driver",
                        "--spring.datasource.username=sa",
                        "--spring.sql.init.mode=always"
                )) {
            UserRepository userRepository = context.getBean(UserRepository.class);
            RoleRepository roleRepository = context.getBean(RoleRepository.class);
            PermissionRepository permissionRepository = context.getBean(PermissionRepository.class);

            assertInstanceOf(JdbcUserRepository.class, userRepository);
            assertInstanceOf(JdbcRoleRepository.class, roleRepository);
            assertInstanceOf(JdbcPermissionRepository.class, permissionRepository);

            userRepository.save(User.register("mysql-user", "mysql@example.com"));
            assertTrue(userRepository.findByUsername("mysql-user").isPresent());
        }
    }

    @Test
    void shouldUsePostgreSqlRepositoriesWhenConfigured() {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(IdentityApplication.class)
                .run(
                        "--spring.main.web-application-type=none",
                        "--identity.repository.type=postgresql",
                        "--spring.datasource.url=jdbc:h2:mem:postgres-switch;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
                        "--spring.datasource.driver-class-name=org.h2.Driver",
                        "--spring.datasource.username=sa",
                        "--spring.sql.init.mode=always"
                )) {
            UserRepository userRepository = context.getBean(UserRepository.class);
            RoleRepository roleRepository = context.getBean(RoleRepository.class);
            PermissionRepository permissionRepository = context.getBean(PermissionRepository.class);

            assertInstanceOf(JdbcUserRepository.class, userRepository);
            assertInstanceOf(JdbcRoleRepository.class, roleRepository);
            assertInstanceOf(JdbcPermissionRepository.class, permissionRepository);

            userRepository.save(User.register("pg-user", "pg@example.com"));
            assertTrue(userRepository.findByUsername("pg-user").isPresent());
        }
    }
}
