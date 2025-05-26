package org.baklansbalkan.HeadacheChecker.repositories;

import org.baklansbalkan.HeadacheChecker.models.Role;
import org.baklansbalkan.HeadacheChecker.models.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleEnum name);
}
