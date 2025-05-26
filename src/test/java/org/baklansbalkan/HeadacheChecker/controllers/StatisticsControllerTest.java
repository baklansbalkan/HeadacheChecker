package org.baklansbalkan.HeadacheChecker.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.baklansbalkan.HeadacheChecker.models.*;
import org.baklansbalkan.HeadacheChecker.repositories.HeadacheRepository;
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
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private HeadacheRepository headacheRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    private String token;

    @BeforeEach
    void setUp() throws Exception {
        headacheRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        createTestRoles();
        createTestUser();
        saveTestEntities();
        token = obtainAccessToken("testusername", "testpassword");
    }

    @Test
    @DisplayName("Test: Getting the count of entries per month")
    void testGetStatisticsByMonth() throws Exception {
        LocalDate date = LocalDate.parse("2025-01-01");

        mockMvc.perform(get("/statistics/{date}/month", date)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("Entries for this month: 2"));
    }

    @Test
    @DisplayName("Test: Getting all the entries for month")
    void testGetAllStatisticsByMonth() throws Exception {
        LocalDate date = LocalDate.parse("2025-01-01");

        mockMvc.perform(get("/statistics/{date}/month/all", date)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].date", is("2025-01-01")))
                .andExpect(jsonPath("$[1].date", is("2025-01-02")));
    }

    @Test
    @DisplayName("Test: Getting the count of entries per year")
    void testGetStatisticsByYear() throws Exception {
        LocalDate date = LocalDate.parse("2025-01-01");

        mockMvc.perform(get("/statistics/{date}/year", date)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("Entries for this year: 5"));
    }

    @Test
    @DisplayName("Test: Getting all the entries for year")
    void testGetAllStatisticsByYear() throws Exception {
        LocalDate date = LocalDate.parse("2025-01-01");

        mockMvc.perform(get("/statistics/{date}/year/all", date)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[*].date", containsInAnyOrder(
                        "2025-01-01", "2025-01-02", "2025-02-01", "2025-02-02", "2025-02-03")));
    }

    private void createTestRoles() {
        Role userRole = new Role();
        userRole.setName(RoleEnum.ROLE_USER);
        roleRepository.save(userRole);
    }

    private void createTestUser() {
        User user = new User();
        user.setUsername("testusername");
        user.setEmail("testemail@email.com");
        user.setPassword(passwordEncoder.encode("testpassword"));
        Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        user.setRoles(Collections.singleton(userRole));
        userRepository.save(user);
    }

    private void saveTestEntities() throws Exception {
        User testUser = userRepository.findByUsername("testusername")
                .orElseThrow(() -> new RuntimeException("Test user not found"));
        Integer userId = testUser.getId();

        Headache headache01 = new Headache();
        headache01.setDate(LocalDate.parse("2025-01-01"));
        headache01.setIsHeadache(true);
        headache01.setIsMedicine(true);
        headache01.setMedicine("Medicine example");
        headache01.setIntensity(4);
        headache01.setLocalisation(Localisation.RIGHT);
        headache01.setTimesOfDay(TimesOfDay.MORNING);
        headache01.setComment("Comment");
        headache01.setUserId(userId);

        Headache headache02 = new Headache();
        headache02.setDate(LocalDate.parse("2025-01-02"));
        headache02.setIsHeadache(true);
        headache02.setIsMedicine(true);
        headache02.setMedicine("Medicine example");
        headache02.setIntensity(3);
        headache02.setLocalisation(Localisation.LEFT);
        headache02.setTimesOfDay(TimesOfDay.EVENING);
        headache02.setComment("Comment");
        headache02.setUserId(userId);

        Headache headache03 = new Headache();
        headache03.setDate(LocalDate.parse("2025-02-01"));
        headache03.setIsHeadache(true);
        headache03.setIsMedicine(true);
        headache03.setMedicine("Medicine example");
        headache03.setIntensity(4);
        headache03.setLocalisation(Localisation.ALL);
        headache03.setTimesOfDay(TimesOfDay.AFTERNOON);
        headache03.setComment("Comment");
        headache03.setUserId(userId);

        Headache headache04 = new Headache();
        headache04.setDate(LocalDate.parse("2025-02-02"));
        headache04.setIsHeadache(true);
        headache04.setIsMedicine(true);
        headache04.setMedicine("Medicine example");
        headache04.setIntensity(2);
        headache04.setLocalisation(Localisation.ALL);
        headache04.setTimesOfDay(TimesOfDay.AFTERNOON);
        headache04.setComment("Comment");
        headache04.setUserId(userId);

        Headache headache05 = new Headache();
        headache05.setDate(LocalDate.parse("2025-02-03"));
        headache05.setIsHeadache(true);
        headache05.setIsMedicine(true);
        headache05.setMedicine("Medicine example");
        headache05.setIntensity(2);
        headache05.setLocalisation(Localisation.ALL);
        headache05.setTimesOfDay(TimesOfDay.AFTERNOON);
        headache05.setComment("Comment");
        headache05.setUserId(userId);

        headacheRepository.saveAll(Arrays.asList(headache01, headache02, headache03, headache04, headache05));
    }

    private String obtainAccessToken(String username, String password) throws Exception {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", username);
        loginRequest.put("password", password);

        String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

        ResultActions result = mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isOk());

        String response = result.andReturn().getResponse().getContentAsString();
        return new ObjectMapper().readTree(response).get("token").asText();
    }
}