package org.baklansbalkan.HeadacheChecker.services;

import org.baklansbalkan.HeadacheChecker.models.Headache;
import org.baklansbalkan.HeadacheChecker.repositories.HeadacheRepository;
import org.baklansbalkan.HeadacheChecker.util.HeadacheNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class HeadacheService {

    private final HeadacheRepository headacheRepository;

    @Autowired
    public HeadacheService(HeadacheRepository headacheRepository) {
        this.headacheRepository = headacheRepository;
    }

    public List<Headache> findAllHeadache() {
        return headacheRepository.findAll();
    }

    public Headache findHeadacheByDate(LocalDate date) {
        if (Optional.ofNullable(headacheRepository.findByDate(date)).isPresent()) {
            return headacheRepository.findByDate(date);
        } else {
            throw new HeadacheNotFoundException("Sorry, this entry doesn't exist");
        }
    }

    @Transactional
    public void saveHeadache(Headache headache){
        enrichHeadache(headache);
        headacheRepository.save(headache);
    }

    @Transactional
    public void updateHeadache(int id, Headache updatedHeadache) {
        enrichHeadache(updatedHeadache);
        updatedHeadache.setId(id);
        headacheRepository.save(updatedHeadache);
    }

    @Transactional
    public void deleteHeadache(Headache headache){
        headacheRepository.delete(headache);
    }

    public List<Headache> showMonthStatistics(LocalDate date) {
        return headacheRepository.findAllByDateBetween(date.with(TemporalAdjusters.firstDayOfMonth()),
                                                        date.with(TemporalAdjusters.lastDayOfMonth()));
    }

    public List<Headache> showYearStatistics(LocalDate date) {
        return headacheRepository.findAllByDateBetween(date.with(TemporalAdjusters.firstDayOfYear()),
                date.with(TemporalAdjusters.lastDayOfYear()));
    }

    private void enrichHeadache(Headache headache) {
        headache.setCreatedAt(LocalDateTime.now());
    }
}
