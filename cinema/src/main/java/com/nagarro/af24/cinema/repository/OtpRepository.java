package com.nagarro.af24.cinema.repository;

import com.nagarro.af24.cinema.model.Otp;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByUserEmail(String email);

    @Modifying
    @Transactional
    @Query("delete from Otp o where o.expireAt < ?1")
    void deleteByExpireAtIsBefore(LocalDateTime now);
}