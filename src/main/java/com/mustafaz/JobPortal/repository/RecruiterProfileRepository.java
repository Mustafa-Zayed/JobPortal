package com.mustafaz.JobPortal.repository;

import com.mustafaz.JobPortal.entity.RecruiterProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RecruiterProfileRepository extends JpaRepository<RecruiterProfile, Integer> {
    @Query("select r from RecruiterProfile r where r.users.email = ?1")
    Optional<RecruiterProfile> findByUsers_Email(String email);
}