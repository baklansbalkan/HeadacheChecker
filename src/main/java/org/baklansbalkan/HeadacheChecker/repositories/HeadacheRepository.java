package org.baklansbalkan.HeadacheChecker.repositories;

import org.baklansbalkan.HeadacheChecker.models.Headache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface HeadacheRepository extends JpaRepository<Headache, Integer> {

    Headache findByDate(LocalDate date);

    List<Headache> findAllByDateBetween(LocalDate firstDate, LocalDate lastDate);
}
