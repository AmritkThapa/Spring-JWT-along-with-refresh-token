package com.amrit.jwt.repository;

import com.amrit.jwt.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Roles, Long> {
    Roles findByName(String name);
}
