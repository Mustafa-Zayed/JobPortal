package com.mustafaz.JobPortal.repository;

import com.mustafaz.JobPortal.entity.JobPostActivity;
import com.mustafaz.JobPortal.entity.JobSeekerApply;
import com.mustafaz.JobPortal.entity.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSeekerApplyRepository extends JpaRepository<JobSeekerApply, Integer> {

    List<JobSeekerApply> findByJobSeekerProfile(JobSeekerProfile jobSeekerProfile);

    List<JobSeekerApply> findByJobPostActivity(JobPostActivity jobPostActivity);

}
