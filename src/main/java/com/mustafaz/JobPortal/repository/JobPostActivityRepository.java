package com.mustafaz.JobPortal.repository;

import com.mustafaz.JobPortal.entity.JobPostActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobPostActivityRepository extends JpaRepository<JobPostActivity, Integer> {
}
