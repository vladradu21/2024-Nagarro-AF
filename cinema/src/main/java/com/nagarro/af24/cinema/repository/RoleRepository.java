package com.nagarro.af24.cinema.repository;

import com.nagarro.af24.cinema.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByAuthority(String authority);

    List<Role> findAllByAuthorityIn(List<String> authorities);
}