package org.baklansbalkan.HeadacheChecker.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.baklansbalkan.HeadacheChecker.dto.HeadacheDTO;
import org.baklansbalkan.HeadacheChecker.models.Headache;
import org.baklansbalkan.HeadacheChecker.services.HeadacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    private final HeadacheService headacheService;
    private final ObjectMapper objectMapper;

    @Autowired
    public StatisticsController(HeadacheService headacheService, ObjectMapper objectMapper) {
        this.headacheService = headacheService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/{date}/month")
    public String getStatisticsByMonth(@PathVariable("date") LocalDate date) {
        return "Entries for this month: " + headacheService.showMonthStatistics(date).size();
    }

    @GetMapping("/{date}/month/all")
    public List<HeadacheDTO> getAllStatisticsByMonth(@PathVariable("date") LocalDate date) {
        return headacheService.showMonthStatistics(date).stream()
                .map(this::convertToHeadacheDTO)
                .toList();
    }

    @GetMapping("/{date}/year")
    public String getStatisticsByYear(@PathVariable("date") LocalDate date) {
        return "Entries for this year: " + headacheService.showYearStatistics(date).size();
    }

    @GetMapping("/{date}/year/all")
    public List<HeadacheDTO> getAllStatisticsByYear(@PathVariable("date") LocalDate date) {
        return headacheService.showYearStatistics(date).stream()
                .map(this::convertToHeadacheDTO)
                .toList();
    }

    private HeadacheDTO convertToHeadacheDTO(Headache headache){
        return objectMapper.convertValue(headache, HeadacheDTO.class);
    }
}
