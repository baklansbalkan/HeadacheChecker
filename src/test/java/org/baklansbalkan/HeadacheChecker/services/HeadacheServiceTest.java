package org.baklansbalkan.HeadacheChecker.services;

import org.baklansbalkan.HeadacheChecker.dto.HeadacheDTO;
import org.baklansbalkan.HeadacheChecker.models.Headache;
import org.baklansbalkan.HeadacheChecker.models.Localisation;
import org.baklansbalkan.HeadacheChecker.models.TimesOfDay;
import org.baklansbalkan.HeadacheChecker.repositories.HeadacheRepository;
import org.baklansbalkan.HeadacheChecker.util.HeadacheMapper;
import org.baklansbalkan.HeadacheChecker.util.HeadacheNotCreatedException;
import org.baklansbalkan.HeadacheChecker.util.HeadacheNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionSystemException;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class HeadacheServiceTest {

    @Autowired
    private HeadacheRepository headacheRepository;
    @Autowired
    private HeadacheService headacheService;
    @Autowired
    HeadacheMapper headacheMapper;

    @BeforeEach
    public void setUp() {
        headacheRepository.deleteAll();
    }

    @Test
    @DisplayName("Test: Getting all the entries")
    void testFindAllHeadache() {
        Headache headache01 = setHeadache(LocalDate.parse("2025-01-01"));
        headacheRepository.save(headache01);
        int id01 = headache01.getId();
        Headache headache02 = setHeadache(LocalDate.parse("2025-01-02"));
        headacheRepository.save(headache02);
        int id02 = headache02.getId();

        List<HeadacheDTO> allHeadache = headacheService.findAllHeadache();

        List<HeadacheDTO> controlHeadache = List.of(
                headacheMapper.convertToHeadacheDTO(new Headache(id01, LocalDate.parse("2025-01-01"), true, true, "Medicine example", 4, Localisation.RIGHT, TimesOfDay.MORNING, "Comment")),
                headacheMapper.convertToHeadacheDTO(new Headache(id02, LocalDate.parse("2025-01-02"), true, true, "Medicine example", 4, Localisation.RIGHT, TimesOfDay.MORNING, "Comment"))
        );

        Assertions.assertNotNull(allHeadache);
        Assertions.assertEquals(controlHeadache.toString(), allHeadache.toString());
    }

    @Test
    @DisplayName("Test: Getting an entry by date")
    void findHeadacheByDate() {
        Headache headache = setHeadache(LocalDate.parse("2025-01-01"));
        headache.setDate(LocalDate.parse("2025-01-01"));
        headacheRepository.save(headache);
        LocalDate date = headache.getDate();
        int id = headache.getId();

        HeadacheDTO headacheByDate = headacheService.findHeadacheByDate(date);

        Assertions.assertNotNull(headacheByDate);
        Assertions.assertEquals(id, headacheByDate.getId());
        Assertions.assertTrue(headacheByDate.isIsHeadache());
        Assertions.assertTrue(headacheByDate.isIsMedicine());
        Assertions.assertEquals("Medicine example", headacheByDate.getMedicine());
        Assertions.assertEquals(4, headacheByDate.getIntensity());
        Assertions.assertEquals("RIGHT", headacheByDate.getLocalisation());
        Assertions.assertEquals("MORNING", headacheByDate.getTimesOfDay());
        Assertions.assertEquals("Comment", headacheByDate.getComment());
    }

    @Test
    @DisplayName("Test: Getting an exception when searching an entry by date")
    void testFindHeadacheByDateException() {
        Assertions.assertThrows(
                HeadacheNotFoundException.class,
                () -> headacheService.findHeadacheByDate(LocalDate.parse("1900-01-01"))
        );
    }

    @Test
    @DisplayName("Test: Creating new entry")
    void testSaveHeadache() {
        Headache headache = setHeadache(LocalDate.parse("2025-01-01"));
        headacheRepository.save(headache);
        int id = headache.getId();

        Assertions.assertNotNull(headache);
        Assertions.assertEquals(
                new Headache(id, LocalDate.parse("2025-01-01"), true, true, "Medicine example", 4, Localisation.RIGHT, TimesOfDay.MORNING, "Comment").toString(),
                headache.toString()
        );
    }

    @Test
    @DisplayName("Test: Getting an exception when creating new entry")
    void testSaveHeadacheException() {
        Headache headache = setHeadache(LocalDate.parse("2025-01-01"));
        headache.setMedicine("Medicine example with toooooooooooooo long description");

        Assertions.assertThrows(
                HeadacheNotCreatedException.class,
                () -> headacheService.saveHeadache(headacheMapper.convertToHeadacheDTO(headache))
        );
    }

    @Test
    @DisplayName("Test: Updating an entry")
    void testUpdateHeadache() {
        Headache headache = setHeadache(LocalDate.parse("2025-01-01"));
        headacheRepository.save(headache);
        int id = headache.getId();

        headache.setComment("This entry has been updated");
        headache.setIntensity(5);

        headacheService.updateHeadache(id, headacheMapper.convertToHeadacheDTO(headache));

        Assertions.assertNotNull(headache);
        Assertions.assertEquals(
                new Headache(id, LocalDate.parse("2025-01-01"), true, true, "Medicine example", 5, Localisation.RIGHT, TimesOfDay.MORNING, "This entry has been updated").toString(),
                headache.toString()
        );
    }

    @Test
    @DisplayName("Test: Getting an exception when updating an entry")
    void testUpdateHeadacheException() {
        Headache headache = setHeadache(LocalDate.parse("2025-01-01"));
        headacheRepository.save(headache);
        int id = headache.getId();

        headache.setMedicine("Medicine example with toooooooooooooo long description");

        Assertions.assertThrows(
                TransactionSystemException.class,
                () -> headacheService.updateHeadache(id, headacheMapper.convertToHeadacheDTO(headache))
        );
    }

    @Test
    @DisplayName("Test: Deleting an entry")
    void testDeleteHeadache() {
        Headache headache = setHeadache(LocalDate.parse("2025-01-01"));
        headacheRepository.save(headache);

        headacheService.deleteHeadache(headacheMapper.convertToHeadacheDTO(headache));
    }

    @Test
    @DisplayName("Test: Getting an exception when deleting an entry")
    void testDeleteHeadacheException() {
        Assertions.assertThrows(
                HeadacheNotFoundException.class,
                () -> headacheService.deleteHeadache(new HeadacheDTO())
        );
    }

    private Headache setHeadache(LocalDate date) {
        Headache headache = new Headache();
        headache.setDate(date);
        headache.setIsHeadache(true);
        headache.setIsMedicine(true);
        headache.setMedicine("Medicine example");
        headache.setIntensity(4);
        headache.setLocalisation(Localisation.RIGHT);
        headache.setTimesOfDay(TimesOfDay.MORNING);
        headache.setComment("Comment");
        return headache;
    }
}