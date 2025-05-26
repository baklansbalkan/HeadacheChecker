package org.baklansbalkan.HeadacheChecker.services;

import org.baklansbalkan.HeadacheChecker.dto.HeadacheDTO;
import org.baklansbalkan.HeadacheChecker.repositories.HeadacheRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class StatisticsService {

    private final HeadacheRepository headacheRepository;
    private final ModelMapper modelMapper;


    public StatisticsService(HeadacheRepository headacheRepository, ModelMapper modelMapper) {
        this.headacheRepository = headacheRepository;
        this.modelMapper = modelMapper;
    }

    public List<HeadacheDTO> showMonthStatistics(LocalDate date, Integer userId) {
        return headacheRepository.findAllByDateBetweenAndUserId(
                        date.with(TemporalAdjusters.firstDayOfMonth()),
                        date.with(TemporalAdjusters.lastDayOfMonth()),
                        userId)
                .stream()
                .map(headache -> modelMapper.map(headache, HeadacheDTO.class))
                .toList();
    }

    public List<HeadacheDTO> showYearStatistics(LocalDate date, Integer userId) {
        return headacheRepository.findAllByDateBetweenAndUserId(
                        date.with(TemporalAdjusters.firstDayOfYear()),
                        date.with(TemporalAdjusters.lastDayOfYear()),
                        userId)
                .stream()
                .map(headache -> modelMapper.map(headache, HeadacheDTO.class))
                .toList();
    }
}
