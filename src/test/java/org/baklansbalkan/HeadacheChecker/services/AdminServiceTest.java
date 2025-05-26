package org.baklansbalkan.HeadacheChecker.services;

import org.baklansbalkan.HeadacheChecker.dto.UserDTO;
import org.baklansbalkan.HeadacheChecker.models.User;
import org.baklansbalkan.HeadacheChecker.repositories.UserRepository;
import org.baklansbalkan.HeadacheChecker.util.EntryNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class AdminServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminService adminService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser1;
    private User testUser2;


    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();

        testUser1 = new User();
        testUser1.setUsername("admin");
        testUser1.setEmail("admin@example.com");
        testUser1.setPassword(passwordEncoder.encode("password"));
        userRepository.save(testUser1);

        testUser2 = new User();
        testUser2.setUsername("user");
        testUser2.setEmail("user@example.com");
        testUser2.setPassword(passwordEncoder.encode("password"));
        userRepository.save(testUser1);

        userRepository.saveAll(List.of(testUser1, testUser2));
    }

    @Test
    @DisplayName("Test: Getting all the users")
    void getAllUsers() {
        List<UserDTO> users = adminService.getAllUsers();

        assertThat(users)
                .hasSize(2)
                .extracting(UserDTO::getUsername)
                .containsExactlyInAnyOrder("admin", "user");
    }

    @Test
    @DisplayName("Test: Getting one user by Id")
    void getUserById() {
        UserDTO user = adminService.getUserById(testUser1.getId());

        assertThat(user.getUsername()).isEqualTo("admin");
        assertThat(user.getEmail()).isEqualTo("admin@example.com");
    }

    @Test
    @DisplayName("Test: Getting an exception when searching by Id")
    void getUserByIdException() {
        int nonExistentId = 999;

        assertThatThrownBy(() -> adminService.getUserById(nonExistentId))
                .isInstanceOf(EntryNotFoundException.class)
                .hasMessageContaining("Sorry, this user doesn't exist");
    }

    @Test
    @DisplayName("Test: Getting one user by Username")
    void getUserByUsername() {
        UserDTO user = adminService.getUserByUsername("user");
        assertThat(user.getUsername()).isEqualTo("user");
    }

    @Test
    @DisplayName("Test: Getting one user by Email")
    void getUserByEmail() {
        UserDTO user = adminService.getUserByEmail("admin@example.com");
        assertThat(user.getEmail()).isEqualTo("admin@example.com");
    }

    @Test
    @DisplayName("Test: Deleting one user by Id")
    void deleteUserById() {
        int userId = testUser1.getId();
        adminService.deleteUserById(userId);
        assertThat(userRepository.findById(userId)).isEmpty();
    }
}