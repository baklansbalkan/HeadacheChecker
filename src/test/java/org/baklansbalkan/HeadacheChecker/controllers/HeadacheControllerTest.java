package org.baklansbalkan.HeadacheChecker.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.baklansbalkan.HeadacheChecker.dto.HeadacheDTO;
import org.baklansbalkan.HeadacheChecker.models.Headache;
import org.baklansbalkan.HeadacheChecker.models.Localisation;
import org.baklansbalkan.HeadacheChecker.models.TimesOfDay;
import org.baklansbalkan.HeadacheChecker.repositories.HeadacheRepository;
import org.baklansbalkan.HeadacheChecker.util.HeadacheMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

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
    private HeadacheMapper headacheMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        headacheRepository.deleteAll();
        saveTestEntities();
    }

    @Test
    @DisplayName("Test: Getting all the entries")
    void testGetAllHeadache() throws Exception {
        mockMvc.perform(get("/headache"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Test: Getting one entry by date")
    void testGetHeadacheByDate() throws Exception {
        Headache headache = headacheRepository.findAll().get(0);
        LocalDate date = headache.getDate();

        mockMvc.perform(get("/headache/{date}", date))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date", is(date.toString())));
    }

    @Test
    @DisplayName("Test: Getting an exception when searching by date")
    void testGetHeadacheByDateException() throws Exception {
        mockMvc.perform(get("/headache/{date}", LocalDate.parse("1990-01-01")))
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(headacheDTOJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test: Updating an entry")
    void testUpdateHeadache() throws Exception {
        Headache headache = headacheRepository.findAll().get(0);
        LocalDate date = headache.getDate();
        HeadacheDTO headacheDTO = headacheMapper.convertToHeadacheDTO(headache);
        headacheDTO.setComment("This entry has been updated");
        String headacheDTOJson = objectMapper.writeValueAsString(headacheDTO);

        mockMvc.perform(put("/headache/{date}", date)
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
        HeadacheDTO headacheDTO = headacheMapper.convertToHeadacheDTO(headache);
        headacheDTO.setMedicine("This entry has been updated and this description is tooooo long");
        String headacheDTOJson = objectMapper.writeValueAsString(headacheDTO);

        mockMvc.perform(put("/headache/{date}", date)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(headacheDTOJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test: Deleting an entry")
    void testDeleteHeadache() throws Exception {
        Headache headache = headacheRepository.findAll().get(0);
        LocalDate date = headache.getDate();

        mockMvc.perform(delete("/headache/{date}", date))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test: Getting an exception when deleting an entry")
    void testDeleteTaskException() throws Exception {
        mockMvc.perform(delete("/headache/{date}", LocalDate.parse("1990-01-01")))
                .andExpect(status().isNotFound());
    }

    private void saveTestEntities() {
        Headache headache01 = new Headache();
        headache01.setDate(LocalDate.parse("2025-01-01"));
        headache01.setIsHeadache(true);
        headache01.setIsMedicine(true);
        headache01.setMedicine("Medicine example");
        headache01.setIntensity(4);
        headache01.setLocalisation(Localisation.RIGHT);
        headache01.setTimesOfDay(TimesOfDay.MORNING);
        headache01.setComment("Comment");

        Headache headache02 = new Headache();
        headache02.setDate(LocalDate.parse("2025-01-02"));
        headache02.setIsHeadache(true);
        headache02.setIsMedicine(true);
        headache02.setMedicine("Medicine example");
        headache02.setIntensity(3);
        headache02.setLocalisation(Localisation.LEFT);
        headache02.setTimesOfDay(TimesOfDay.EVENING);
        headache02.setComment("Comment");

        Headache headache03 = new Headache();
        headache03.setDate(LocalDate.parse("2025-01-03"));
        headache03.setIsHeadache(true);
        headache03.setIsMedicine(true);
        headache03.setMedicine("Medicine example");
        headache03.setIntensity(2);
        headache03.setLocalisation(Localisation.ALL);
        headache03.setTimesOfDay(TimesOfDay.AFTERNOON);
        headache03.setComment("Comment");

        Headache headache04 = new Headache();
        headache04.setDate(LocalDate.parse("2025-01-04"));
        headache04.setIsHeadache(false);

        headacheRepository.saveAll(Arrays.asList(headache01, headache02, headache03, headache04));
    }
}