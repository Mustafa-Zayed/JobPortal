package com.mustafaz.JobPortal.sevices;

import com.mustafaz.JobPortal.entity.JobPostActivity;
import com.mustafaz.JobPortal.entity.JobSeekerApply;
import com.mustafaz.JobPortal.entity.JobSeekerProfile;
import com.mustafaz.JobPortal.repository.JobSeekerApplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class JobSeekerApplyService {

    private final JobSeekerApplyRepository repository;

    public List<JobSeekerApply> getCandidateJobs(JobSeekerProfile jobSeekerProfile) {
        return repository.findByJobSeekerProfile(jobSeekerProfile);
    }

    public List<JobSeekerApply> getCandidateJobs(JobPostActivity jobPostActivity) {
        return repository.findByJobPostActivity(jobPostActivity);
    }

    public void save(JobSeekerApply jobSeekerApply) {
        repository.save(jobSeekerApply);
    }
}
