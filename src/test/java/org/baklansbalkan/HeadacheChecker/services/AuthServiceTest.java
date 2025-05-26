package org.baklansbalkan.HeadacheChecker.services;

import org.baklansbalkan.HeadacheChecker.dto.JwtResponse;
import org.baklansbalkan.HeadacheChecker.dto.MessageResponse;
import org.baklansbalkan.HeadacheChecker.dto.SignUpRequest;
import org.baklansbalkan.HeadacheChecker.dto.LoginRequest;
import org.baklansbalkan.HeadacheChecker.models.Role;
import org.baklansbalkan.HeadacheChecker.models.RoleEnum;
import org.baklansbalkan.HeadacheChecker.models.User;
import org.baklansbalkan.HeadacheChecker.repositories.RoleRepository;
import org.baklansbalkan.HeadacheChecker.repositories.UserRepository;
import org.baklansbalkan.HeadacheChecker.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        if (roleRepository.findByName(RoleEnum.ROLE_USER).isEmpty()) {
            Role userRole = new Role();
            userRole.setName(RoleEnum.ROLE_USER);
            roleRepository.save(userRole);
        }

        if (roleRepository.findByName(RoleEnum.ROLE_ADMIN).isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName(RoleEnum.ROLE_ADMIN);
            roleRepository.save(adminRole);
        }
    }

    @Test
    @Transactional
    @DisplayName("Test: Register new user")
    void registerUserSuccessfully() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("testuser");
        signUpRequest.setEmail("test@example.com");
        signUpRequest.setPassword("password");
        signUpRequest.setRoles(Set.of("user"));

        MessageResponse response = authService.registerUser(signUpRequest);

        assertThat(response.getMessage()).isEqualTo("User registered successfully");

        User savedUser = userRepository.findByUsername("testuser").orElseThrow();

        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(passwordEncoder.matches("password", savedUser.getPassword())).isTrue();
        assertThat(savedUser.getRoles())
                .hasSize(1)
                .extracting(Role::getName)
                .containsExactly(RoleEnum.ROLE_USER);
    }

    @Test
    @DisplayName("Test: Trying to register new user with existing username")
    void registerUserUsernameExists() {
        User existingUser = new User();
        existingUser.setUsername("existing");
        existingUser.setEmail("existing@example.com");
        existingUser.setPassword(passwordEncoder.encode("password"));
        userRepository.save(existingUser);

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("existing");
        signUpRequest.setEmail("new@example.com");
        signUpRequest.setPassword("password");

        MessageResponse response = authService.registerUser(signUpRequest);

        assertThat(response.getMessage()).isEqualTo("Error: Username is already taken");
    }

    @Test
    @DisplayName("Test: Trying to register new user with existing email")
    void registerUserEmailExists() {
        User existingUser = new User();
        existingUser.setUsername("existing");
        existingUser.setEmail("existing@example.com");
        existingUser.setPassword(passwordEncoder.encode("password"));
        userRepository.save(existingUser);

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("notexisting");
        signUpRequest.setEmail("existing@example.com");
        signUpRequest.setPassword("password");

        MessageResponse response = authService.registerUser(signUpRequest);

        assertThat(response.getMessage()).isEqualTo("Error: Email is already in use");
    }

    @Test
    @Transactional
    @DisplayName("Test: Register new admin")
    void registerUserAdmin() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("adminuser");
        signUpRequest.setEmail("admin@example.com");
        signUpRequest.setPassword("adminpass");
        signUpRequest.setRoles(Set.of("admin"));

        authService.registerUser(signUpRequest);

        User adminUser = userRepository.findByUsername("adminuser").orElseThrow();

        assertThat(adminUser.getRoles())
                .extracting(Role::getName)
                .contains(RoleEnum.ROLE_ADMIN);
    }

    @Test
    @DisplayName("Test: Successful authentication")
    void authenticateUserSuccessfully() {
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

        JwtResponse response = authService.authenticateUser(loginRequest);

        assertThat(response.getToken()).isNotBlank();
        assertThat(response.getUsername()).isEqualTo("authuser");
        assertThat(response.getEmail()).isEqualTo("auth@example.com");
        assertThat(jwtUtils.validateJwtToken(response.getToken())).isTrue();
        assertThat(response.getRoles()).contains("ROLE_USER");
    }

    @Test
    @DisplayName("Test: Unsuccessful authentication")
    void authenticateUserException() {
        User user = new User();
        user.setUsername("authuser");
        user.setEmail("auth@example.com");
        user.setPassword(passwordEncoder.encode("authpass"));
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("authuser");
        loginRequest.setPassword("wrongpassword");

        assertThatThrownBy(() -> authService.authenticateUser(loginRequest))
                .isInstanceOf(BadCredentialsException.class);
    }
}