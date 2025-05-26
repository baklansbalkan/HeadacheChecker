package org.baklansbalkan.HeadacheChecker.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.baklansbalkan.HeadacheChecker.models.Role;
import org.baklansbalkan.HeadacheChecker.models.RoleEnum;
import org.baklansbalkan.HeadacheChecker.models.User;
import org.baklansbalkan.HeadacheChecker.repositories.RoleRepository;
import org.baklansbalkan.HeadacheChecker.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private User adminUser;
    private String tokenAdmin;

    @BeforeEach
    void setUpEach() throws Exception {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role adminRole = new Role();
        adminRole.setName(RoleEnum.ROLE_ADMIN);
        roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setName(RoleEnum.ROLE_USER);
        roleRepository.save(userRole);

        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setRoles(Set.of(userRole));
        userRepository.save(testUser);

        adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword(passwordEncoder.encode("adminpass"));
        adminUser.setRoles(Set.of(adminRole));
        userRepository.save(adminUser);

        tokenAdmin = obtainAccessToken("admin", "adminpass");
    }

    @Test
    @DisplayName("Test: Getting all the users")
    void getAllUsers() throws Exception {
        mockMvc.perform(get("/admin")
                        .header(HttpHeaders.AUTHORIZATION, tokenAdmin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testuser"));
    }

    @Test
    @DisplayName("Test: Getting one user by id")
    void getUserById() throws Exception {
        mockMvc.perform(get("/admin/id/{id}", testUser.getId())
                        .header(HttpHeaders.AUTHORIZATION, tokenAdmin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @DisplayName("Test: Getting an exception when searching user by id")
    void getUserByUsernameException() throws Exception {
        mockMvc.perform(get("/admin/id/{id}", 999)
                        .header(HttpHeaders.AUTHORIZATION, tokenAdmin))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Sorry, this user doesn't exist"));
    }

    @Test
    @DisplayName("Test: Getting one user by username")
    void getUserByUsername() throws Exception {
        mockMvc.perform(get("/admin/username/{username}", "testuser")
                        .header(HttpHeaders.AUTHORIZATION, tokenAdmin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @DisplayName("Test: Getting one user by email")
    void getUserByEmail() throws Exception {
        mockMvc.perform(get("/admin/email/{email}", "test@example.com")
                        .header(HttpHeaders.AUTHORIZATION, tokenAdmin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @DisplayName("Test: Deleting user")
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/admin/id/{id}", testUser.getId())
                        .header(HttpHeaders.AUTHORIZATION, tokenAdmin))
                .andExpect(status().isOk());

        assertThat(userRepository.findById(testUser.getId())).isEmpty();
    }

    @Test
    @DisplayName("Test: Trying to delete user without admin rights")
    void deleteUserByUser() throws Exception {
        String tokenUser = obtainAccessToken("testuser", "password");

        mockMvc.perform(delete("/admin/id/{id}", testUser.getId())
                        .header(HttpHeaders.AUTHORIZATION, tokenUser))
                .andExpect(status().isForbidden());
    }

    private String obtainAccessToken(String username, String password) throws Exception {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", username);
        loginRequest.put("password", password);

        String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

        ResultActions result = mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isOk());

        String response = result.andReturn().getResponse().getContentAsString();
        return "Bearer " + objectMapper.readTree(response).get("token").asText();
    }
}