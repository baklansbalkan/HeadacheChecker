package org.baklansbalkan.HeadacheChecker.controllers;

import org.baklansbalkan.HeadacheChecker.models.Headache;
import org.baklansbalkan.HeadacheChecker.models.Localisation;
import org.baklansbalkan.HeadacheChecker.models.TimesOfDay;
import org.baklansbalkan.HeadacheChecker.repositories.HeadacheRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private HeadacheRepository headacheRepository;

    @BeforeEach
    void setUp() {
        headacheRepository.deleteAll();
        saveTestEntities();
    }

    @Test
    void TestGetStatisticsByMonth() throws Exception {
        LocalDate date = LocalDate.parse("2025-01-01");

        mockMvc.perform(get("/statistics/{date}/month", date))
                .andExpect(status().isOk())
                .andExpect(content().string("Entries for this month: 2"));
    }

    @Test
    void getAllStatisticsByMonth() throws Exception {
        LocalDate date = LocalDate.parse("2025-01-01");

        mockMvc.perform(get("/statistics/{date}/month/all", date))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].date", is("2025-01-01")))
                .andExpect(jsonPath("$[1].date", is("2025-01-02")));
    }

    @Test
    void getStatisticsByYear() throws Exception {
        LocalDate date = LocalDate.parse("2025-01-01");

        mockMvc.perform(get("/statistics/{date}/year", date))
                .andExpect(status().isOk())
                .andExpect(content().string("Entries for this year: 5"));
    }

    @Test
    void getAllStatisticsByYear() throws Exception {
        LocalDate date = LocalDate.parse("2025-01-01");

        mockMvc.perform(get("/statistics/{date}/year/all", date))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[*].date", containsInAnyOrder(
                        "2025-01-01", "2025-01-02", "2025-02-01", "2025-02-02", "2025-02-03")));
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
        headache03.setDate(LocalDate.parse("2025-02-01"));
        headache03.setIsHeadache(true);
        headache03.setIsMedicine(true);
        headache03.setMedicine("Medicine example");
        headache03.setIntensity(2);
        headache03.setLocalisation(Localisation.ALL);
        headache03.setTimesOfDay(TimesOfDay.AFTERNOON);
        headache03.setComment("Comment");

        Headache headache04 = new Headache();
        headache04.setDate(LocalDate.parse("2025-02-02"));
        headache04.setIsHeadache(false);
        headache03.setIsHeadache(true);
        headache03.setIsMedicine(true);
        headache03.setMedicine("Medicine example");
        headache03.setIntensity(2);
        headache03.setLocalisation(Localisation.ALL);
        headache03.setTimesOfDay(TimesOfDay.AFTERNOON);
        headache03.setComment("Comment");

        Headache headache05 = new Headache();
        headache05.setDate(LocalDate.parse("2025-02-03"));
        headache05.setIsHeadache(false);
        headache05.setIsHeadache(true);
        headache05.setIsMedicine(true);
        headache05.setMedicine("Medicine example");
        headache05.setIntensity(2);
        headache05.setLocalisation(Localisation.RIGHT);
        headache05.setTimesOfDay(TimesOfDay.NIGHT);
        headache05.setComment("Comment");

        Headache headache06 = new Headache();
        headache06.setDate(LocalDate.parse("2024-12-03"));
        headache06.setIsHeadache(false);
        headache06.setIsHeadache(true);
        headache06.setIsMedicine(true);
        headache06.setMedicine("Medicine example");
        headache06.setIntensity(4);
        headache06.setLocalisation(Localisation.LEFT);
        headache06.setTimesOfDay(TimesOfDay.NIGHT);
        headache06.setComment("Comment");

        headacheRepository.saveAll(Arrays.asList(headache01, headache02, headache03, headache04, headache05, headache06));
    }
}