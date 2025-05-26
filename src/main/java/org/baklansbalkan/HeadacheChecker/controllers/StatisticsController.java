package org.baklansbalkan.HeadacheChecker.controllers;

import org.baklansbalkan.HeadacheChecker.dto.HeadacheDTO;
import org.baklansbalkan.HeadacheChecker.security.UserDetailsImpl;
import org.baklansbalkan.HeadacheChecker.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/statistics")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/{date}/month")
    public String getStatisticsByMonth(@PathVariable("date") LocalDate date) {
        return "Entries for this month: " + statisticsService.showMonthStatistics(date, getCurrentUserId()).size();
    }

    @GetMapping("/{date}/month/all")
    public List<HeadacheDTO> getAllStatisticsByMonth(@PathVariable("date") LocalDate date) {
        return statisticsService.showMonthStatistics(date, getCurrentUserId());
    }

    @GetMapping("/{date}/year")
    public String getStatisticsByYear(@PathVariable("date") LocalDate date) {
        return "Entries for this year: " + statisticsService.showYearStatistics(date, getCurrentUserId()).size();
    }

    @GetMapping("/{date}/year/all")
    public List<HeadacheDTO> getAllStatisticsByYear(@PathVariable("date") LocalDate date) {
        return statisticsService.showYearStatistics(date, getCurrentUserId());
    }

    private Integer getCurrentUserId() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userDetails.getId();
    }
}
