package org.baklansbalkan.HeadacheChecker.repositories;

import org.baklansbalkan.HeadacheChecker.models.Headache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HeadacheRepository extends JpaRepository<Headache, Integer> {
    List<Headache> findByUserId(Integer userId);
    Optional<Headache> findByDateAndUserId(LocalDate date, Integer userId);
    List<Headache> findAllByUserId(Integer id);
    List<Headache> findAllByDateBetweenAndUserId(LocalDate firstDate, LocalDate lastDate, Integer userId);
}
