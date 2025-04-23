package org.baklansbalkan.HeadacheChecker.controllers;

import org.baklansbalkan.HeadacheChecker.dto.HeadacheDTO;
import org.baklansbalkan.HeadacheChecker.services.StatisticsService;
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

    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/{date}/month")
    public String getStatisticsByMonth(@PathVariable("date") LocalDate date) {
        return "Entries for this month: " + statisticsService.showMonthStatistics(date).size();
    }

    @GetMapping("/{date}/month/all")
    public List<HeadacheDTO> getAllStatisticsByMonth(@PathVariable("date") LocalDate date) {
        return statisticsService.showMonthStatistics(date);
    }

    @GetMapping("/{date}/year")
    public String getStatisticsByYear(@PathVariable("date") LocalDate date) {
        return "Entries for this year: " + statisticsService.showYearStatistics(date).size();
    }

    @GetMapping("/{date}/year/all")
    public List<HeadacheDTO> getAllStatisticsByYear(@PathVariable("date") LocalDate date) {
        return statisticsService.showYearStatistics(date);
    }
}
