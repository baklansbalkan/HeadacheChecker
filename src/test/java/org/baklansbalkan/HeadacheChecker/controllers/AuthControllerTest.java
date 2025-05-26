package org.baklansbalkan.HeadacheChecker.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.baklansbalkan.HeadacheChecker.dto.LoginRequest;
import org.baklansbalkan.HeadacheChecker.dto.SignUpRequest;
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
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        roleRepository.deleteAll();

        Role userRole = new Role();
        userRole.setName(RoleEnum.ROLE_USER);
        roleRepository.save(userRole);

        Role adminRole = new Role();
        adminRole.setName(RoleEnum.ROLE_ADMIN);
        roleRepository.save(adminRole);
    }

    @Test
    @DisplayName("Test: Register new user")
    void registerUser() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("testuser");
        signUpRequest.setEmail("test@example.com");
        signUpRequest.setPassword("password");
        signUpRequest.setRoles(Set.of("user"));

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully"));

        User user = userRepository.findByUsername("testuser").orElseThrow();
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    @DisplayName("Test: Register new user with existing username")
    void registerUserExistingUsername() throws Exception {
        User existingUser = new User();
        existingUser.setUsername("existing");
        existingUser.setEmail("existing@example.com");
        existingUser.setPassword(passwordEncoder.encode("password"));
        userRepository.save(existingUser);

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("existing");
        signUpRequest.setEmail("new@example.com");
        signUpRequest.setPassword("password");

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Error: Username is already taken"));
    }

    @Test
    @DisplayName("Test: Register new user with existing email")
    void registerUserExistingEmail() throws Exception {
        User existingUser = new User();
        existingUser.setUsername("existing");
        existingUser.setEmail("existing@example.com");
        existingUser.setPassword(passwordEncoder.encode("password"));
        userRepository.save(existingUser);

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("newuser");
        signUpRequest.setEmail("existing@example.com");
        signUpRequest.setPassword("password");

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Error: Email is already in use"));
    }

    @Test
    @DisplayName("Test: Successful authentication")
    void authenticateUserSuccessfully() throws Exception {
        User user = new User();
        user.setUsername("authuser");
        user.setEmail("auth@example.com");
        user.setPassword(passwordEncoder.encode("authpass"));
        Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER).orElseThrow();
        user.setRoles(Set.of(userRole));
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("authuser");
        loginRequest.setPassword("authpass");

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value("authuser"))
                .andExpect(jsonPath("$.email").value("auth@example.com"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));
    }

    @Test
    @DisplayName("Test: Unsuccessful authentication")
    void authenticateUserError() throws Exception {
        User user = new User();
        user.setUsername("authuser");
        user.setEmail("auth@example.com");
        user.setPassword(passwordEncoder.encode("authpass"));
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("authuser");
        loginRequest.setPassword("wrongpassword");

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}



