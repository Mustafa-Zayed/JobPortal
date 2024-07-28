package com.mustafaz.JobPortal.repository;

import com.mustafaz.JobPortal.entity.JobPostActivity;
import com.mustafaz.JobPortal.entity.JobSeekerProfile;
import com.mustafaz.JobPortal.entity.JobSeekerSave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSeekerSaveRepository extends JpaRepository<JobSeekerSave, Integer> {

    List<JobSeekerSave> findByJobSeekerProfile(JobSeekerProfile jobSeekerProfile);

    List<JobSeekerSave> findByJobPostActivity(JobPostActivity jobSeekerProfile);

}
