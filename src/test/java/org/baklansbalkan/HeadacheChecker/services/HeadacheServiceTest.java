package org.baklansbalkan.HeadacheChecker.services;

import org.baklansbalkan.HeadacheChecker.dto.HeadacheDTO;
import org.baklansbalkan.HeadacheChecker.models.Headache;
import org.baklansbalkan.HeadacheChecker.models.Localisation;
import org.baklansbalkan.HeadacheChecker.models.TimesOfDay;
import org.baklansbalkan.HeadacheChecker.repositories.HeadacheRepository;
import org.baklansbalkan.HeadacheChecker.util.EntryNotCreatedException;
import org.baklansbalkan.HeadacheChecker.util.EntryNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionSystemException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class HeadacheServiceTest {

    @Autowired
    private HeadacheRepository headacheRepository;
    @Autowired
    private HeadacheService headacheService;
    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        headacheRepository.deleteAll();
    }

    @Test
    @DisplayName("Test: Getting all the entries")
    void testFindAllHeadacheByUserId() {
        Headache headache01 = setHeadache(LocalDate.parse("2025-01-01"));
        Headache headache02 = setHeadache(LocalDate.parse("2025-01-02"));
        headacheRepository.saveAll(List.of(headache01, headache02));

        List<HeadacheDTO> user1Headaches = headacheService.findAllHeadacheByUserId(1);

        assertThat(user1Headaches)
                .hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(
                        modelMapper.map(headache01, HeadacheDTO.class),
                        modelMapper.map(headache02, HeadacheDTO.class)
                );
    }

    @Test
    @DisplayName("Test: Getting an entry by date")
    void findHeadacheByDate() {
        Headache headache = setHeadache(LocalDate.parse("2025-01-01"));
        headacheRepository.save(headache);
        LocalDate date = headache.getDate();

        HeadacheDTO headacheByDate = headacheService.findHeadacheByDateAndUserId(date, 1);

        assertThat(headacheByDate)
                .usingRecursiveComparison()
                .isEqualTo(modelMapper.map(headache, HeadacheDTO.class));
    }

    @Test
    @DisplayName("Test: Getting an exception when searching an entry by date")
    void testFindHeadacheByDateException() {
        assertThatThrownBy(() -> headacheService.findHeadacheByDateAndUserId(LocalDate.parse("1900-01-01"), 1))
                .isInstanceOf(EntryNotFoundException.class)
                .hasMessageContaining("Sorry, this entry doesn't exist");
    }

    @Test
    @DisplayName("Test: Creating new entry")
    void testSaveHeadache() {
        Headache headache = setHeadache(LocalDate.parse("2025-01-01"));
        headacheRepository.save(headache);

        assertThat(headache)
                .usingRecursiveComparison()
                .isEqualTo(new Headache(
                        headache.getId(),
                        LocalDate.parse("2025-01-01"),
                        true, true, "Medicine example", 4,
                        Localisation.RIGHT, TimesOfDay.MORNING, "Comment", 1
                ));
    }

    @Test
    @DisplayName("Test: Getting an exception when creating new entry")
    void testSaveHeadacheException() {
        Headache headache = setHeadache(LocalDate.parse("2025-01-01"));
        headache.setMedicine("Medicine example with toooooooooooooo long description");

        assertThatThrownBy(() -> headacheService.saveHeadache(modelMapper.map(headache, HeadacheDTO.class)))
                .isInstanceOf(EntryNotCreatedException.class);
    }

    @Test
    @DisplayName("Test: Updating an entry")
    void testUpdateHeadache() {
        Headache headache = setHeadache(LocalDate.parse("2025-01-01"));
        headacheRepository.save(headache);

        headache.setComment("This entry has been updated");
        headache.setIntensity(5);

        headacheService.updateHeadache(headache.getId(), modelMapper.map(headache, HeadacheDTO.class));
        Headache updatedHeadache = headacheRepository.findById(headache.getId()).orElseThrow();

        assertThat(updatedHeadache)
                .usingRecursiveComparison()
                .ignoringFields("createdAt")
                .isEqualTo(headache);
    }

    @Test
    @DisplayName("Test: Getting an exception when updating an entry")
    void testUpdateHeadacheException() {
        Headache headache = setHeadache(LocalDate.parse("2025-01-01"));
        headacheRepository.save(headache);

        headache.setMedicine("Medicine example with toooooooooooooo long description");

        assertThatThrownBy(() -> headacheService.updateHeadache(
                headache.getId(),
                modelMapper.map(headache, HeadacheDTO.class)
        )).isInstanceOf(TransactionSystemException.class);
    }

    @Test
    @DisplayName("Test: Deleting an entry")
    void testDeleteHeadache() {
        Headache headache = setHeadache(LocalDate.parse("2025-01-01"));
        headacheRepository.save(headache);

        headacheService.deleteHeadache(modelMapper.map(headache, HeadacheDTO.class));

        assertThat(headacheRepository.findById(headache.getId())).isEmpty();
    }

    @Test
    @DisplayName("Test: Getting an exception when deleting an entry")
    void testDeleteHeadacheException() {
        assertThatThrownBy(() -> headacheService.deleteHeadache(new HeadacheDTO()))
                .isInstanceOf(RuntimeException.class);
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
        headache.setUserId(1);
        return headache;
    }
}