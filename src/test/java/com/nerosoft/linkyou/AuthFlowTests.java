package com.nerosoft.linkyou;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class AuthFlowTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void grantToken_returnsJwtForValidCredentials() throws Exception {
        mockMvc.perform(post("/api/auth/token/grant")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          \"username\": \"admin\",
                          \"password\": \"admin123\"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isString())
                .andExpect(jsonPath("$.refreshToken").isString());
    }

    @Test
    void protectedEndpoint_requiresBearerToken() throws Exception {
        mockMvc.perform(get("/api/account/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void protectedEndpoint_allowsValidBearerToken() throws Exception {
        MvcResult login = mockMvc.perform(post("/api/auth/token/grant")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          \"username\": \"demo\",
                          \"password\": \"password\"
                        }
                        """))
                .andExpect(status().isOk())
                .andReturn();

        String response = login.getResponse().getContentAsString();
        String token = response.split("\"accessToken\":\"")[1].split("\"")[0];

        mockMvc.perform(get("/api/account/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("demo"));
    }

    @Test
    void registerAccount_createsUser() throws Exception {
        mockMvc.perform(post("/api/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "username": "alice",
                          "password": "Secret123!",
                          "email": "alice@example.com",
                          "phone": "+86 13800138000",
                          "nickname": "Alice"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.username").value("alice"));
    }

    @Test
    void getAccountById_returnsUserDetailWhenAuthenticated() throws Exception {
        MvcResult registration = mockMvc.perform(post("/api/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "username": "bob",
                          "password": "Secret123!",
                          "email": "bob@example.com",
                          "nickname": "Bob"
                        }
                        """))
                .andExpect(status().isCreated())
                .andReturn();

        String registrationResponse = registration.getResponse().getContentAsString();
        String userId = registrationResponse.split("\"id\":\"")[1].split("\"")[0];

        MvcResult login = mockMvc.perform(post("/api/auth/token/grant")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "username": "demo",
                          "password": "password"
                        }
                        """))
                .andExpect(status().isOk())
                .andReturn();

        String loginResponse = login.getResponse().getContentAsString();
        String token = loginResponse.split("\"accessToken\":\"")[1].split("\"")[0];

        mockMvc.perform(get("/api/account/" + userId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value("bob"));
    }

    @Test
    void swaggerEndpoints_arePublic() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/swagger"))
                .andExpect(status().is3xxRedirection());
    }
}
