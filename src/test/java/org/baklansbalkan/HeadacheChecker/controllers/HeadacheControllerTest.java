package org.baklansbalkan.HeadacheChecker.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.baklansbalkan.HeadacheChecker.dto.HeadacheDTO;
import org.baklansbalkan.HeadacheChecker.models.*;
import org.baklansbalkan.HeadacheChecker.repositories.HeadacheRepository;
import org.baklansbalkan.HeadacheChecker.repositories.RoleRepository;
import org.baklansbalkan.HeadacheChecker.repositories.UserRepository;
import org.baklansbalkan.HeadacheChecker.util.EntryNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
class HeadacheControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private HeadacheRepository headacheRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;
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
    @DisplayName("Test: Getting all the entries")
    void testGetAllHeadache() throws Exception {
        mockMvc.perform(get("/headache")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Test: Getting one entry by date")
    void testGetHeadacheByDate() throws Exception {
        User testUser = userRepository.findByUsername("testusername")
                .orElseThrow(() -> new EntryNotFoundException("Test user not found"));
        List<Headache> headaches = headacheRepository.findAllByUserId(testUser.getId());
        assertFalse(headaches.isEmpty(), "No headaches found for test user");
        Headache headache = headacheRepository.findAll().get(0);
        LocalDate date = headache.getDate();

        mockMvc.perform(get("/headache/{date}", date)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date", is(date.toString())));
    }

    @Test
    @DisplayName("Test: Getting an exception when searching by date")
    void testGetHeadacheByDateException() throws Exception {
        mockMvc.perform(get("/headache/{date}", LocalDate.parse("1990-01-01"))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test: Creating new entry")
    public void testCreateHeadache() throws Exception {
        HeadacheDTO headacheDTO = new HeadacheDTO();
        headacheDTO.setDate(LocalDate.parse("2025-01-05"));
        headacheDTO.setIsHeadache(true);
        headacheDTO.setIsMedicine(true);
        headacheDTO.setMedicine("Medicine example");
        headacheDTO.setIntensity(3);
        headacheDTO.setLocalisation("RIGHT");
        headacheDTO.setTimesOfDay("MORNING");
        headacheDTO.setComment("Comment");
        String headacheDTOJson = objectMapper.writeValueAsString(headacheDTO);

        mockMvc.perform(post("/headache")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(headacheDTOJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value("2025-01-05"))
                .andExpect(jsonPath("$.isHeadache").value(true))
                .andExpect(jsonPath("$.isMedicine").value(true))
                .andExpect(jsonPath("$.medicine").value("Medicine example"))
                .andExpect(jsonPath("$.intensity").value(3))
                .andExpect(jsonPath("$.localisation").value("RIGHT"))
                .andExpect(jsonPath("$.timesOfDay").value("MORNING"))
                .andExpect(jsonPath("$.comment").value("Comment"));
    }

    @Test
    @DisplayName("Test: Getting an exception when creating new entry")
    public void testCreateHeadacheException() throws Exception {
        HeadacheDTO headacheDTO = new HeadacheDTO();
        headacheDTO.setDate(LocalDate.parse("2025-01-05"));
        headacheDTO.setIsHeadache(true);
        headacheDTO.setIsMedicine(true);
        headacheDTO.setMedicine("Medicine example with toooooooooooooo long description");
        headacheDTO.setIntensity(3);
        headacheDTO.setLocalisation("RIGHT");
        headacheDTO.setTimesOfDay("MORNING");
        headacheDTO.setComment("Comment");

        String headacheDTOJson = objectMapper.writeValueAsString(headacheDTO);

        mockMvc.perform(post("/headache")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(headacheDTOJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test: Updating an entry")
    void testUpdateHeadache() throws Exception {
        User testUser = userRepository.findByUsername("testusername")
                .orElseThrow(() -> new EntryNotFoundException("Test user not found"));
        List<Headache> headaches = headacheRepository.findAllByUserId(testUser.getId());
        assertFalse(headaches.isEmpty(), "No headaches found for test user");
        Headache headache = headacheRepository.findAll().get(0);
        LocalDate date = headache.getDate();

        HeadacheDTO headacheDTO = modelMapper.map(headache, HeadacheDTO.class);
        headacheDTO.setComment("This entry has been updated");
        String headacheDTOJson = objectMapper.writeValueAsString(headacheDTO);

        mockMvc.perform(put("/headache/{date}", date)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(headacheDTOJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("This entry has been updated"));
    }

    @Test
    @DisplayName("Test: Getting an exception when updating an entry")
    void testUpdateHeadacheException() throws Exception {
        Headache headache = headacheRepository.findAll().get(0);
        LocalDate date = headache.getDate();
        HeadacheDTO headacheDTO = modelMapper.map(headache, HeadacheDTO.class);
        headacheDTO.setMedicine("This entry has been updated and this description is tooooo long");
        String headacheDTOJson = objectMapper.writeValueAsString(headacheDTO);

        mockMvc.perform(put("/headache/{date}", date)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(headacheDTOJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test: Deleting an entry")
    void testDeleteHeadache() throws Exception {
        User testUser = userRepository.findByUsername("testusername")
                .orElseThrow(() -> new EntryNotFoundException("Test user not found"));
        List<Headache> headaches = headacheRepository.findAllByUserId(testUser.getId());
        assertFalse(headaches.isEmpty(), "No headaches found for test user");
        Headache headache = headacheRepository.findAll().get(0);
        LocalDate date = headache.getDate();

        mockMvc.perform(delete("/headache/{date}", date)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test: Getting an exception when deleting an entry")
    void testDeleteTaskException() throws Exception {
        mockMvc.perform(delete("/headache/{date}", LocalDate.parse("1990-01-01"))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
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
                .orElseThrow(() -> new EntryNotFoundException("Error: Role is not found."));
        user.setRoles(Collections.singleton(userRole));
        userRepository.save(user);
    }

    private void saveTestEntities() throws Exception {
        User testUser = userRepository.findByUsername("testusername")
                .orElseThrow(() -> new EntryNotFoundException("Test user not found"));
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
        headache03.setDate(LocalDate.parse("2025-01-03"));
        headache03.setIsHeadache(true);
        headache03.setIsMedicine(true);
        headache03.setMedicine("Medicine example");
        headache03.setIntensity(2);
        headache03.setLocalisation(Localisation.ALL);
        headache03.setTimesOfDay(TimesOfDay.AFTERNOON);
        headache03.setComment("Comment");
        headache03.setUserId(userId);

        Headache headache04 = new Headache();
        headache04.setDate(LocalDate.parse("2025-01-04"));
        headache04.setIsHeadache(false);

        headacheRepository.saveAll(Arrays.asList(headache01, headache02, headache03, headache04));
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
        return new ObjectMapper().readTree(response).get("token").asText();
    }
}