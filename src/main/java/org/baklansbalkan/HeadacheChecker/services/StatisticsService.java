package org.baklansbalkan.HeadacheChecker.services;

import org.baklansbalkan.HeadacheChecker.dto.HeadacheDTO;
import org.baklansbalkan.HeadacheChecker.repositories.HeadacheRepository;
import org.baklansbalkan.HeadacheChecker.util.HeadacheMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class StatisticsService {

    private final HeadacheRepository headacheRepository;
    private final HeadacheMapper headacheMapper;

    public StatisticsService(HeadacheRepository headacheRepository, HeadacheMapper headacheMapper) {
        this.headacheRepository = headacheRepository;
        this.headacheMapper = headacheMapper;
    }

    public List<HeadacheDTO> showMonthStatistics(LocalDate date) {
        return headacheRepository.findAllByDateBetween(date.with(TemporalAdjusters.firstDayOfMonth()),
                        date.with(TemporalAdjusters.lastDayOfMonth())).stream()
                .map(headacheMapper::convertToHeadacheDTO)
                .toList();
    }

    public List<HeadacheDTO> showYearStatistics(LocalDate date) {
        return headacheRepository.findAllByDateBetween(date.with(TemporalAdjusters.firstDayOfYear()),
                        date.with(TemporalAdjusters.lastDayOfYear())).stream()
                .map(headacheMapper::convertToHeadacheDTO)
                .toList();
    }
}
