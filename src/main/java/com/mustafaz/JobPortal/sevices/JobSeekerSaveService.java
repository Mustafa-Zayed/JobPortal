package com.mustafaz.JobPortal.sevices;

import com.mustafaz.JobPortal.entity.JobPostActivity;
import com.mustafaz.JobPortal.entity.JobSeekerProfile;
import com.mustafaz.JobPortal.entity.JobSeekerSave;
import com.mustafaz.JobPortal.repository.JobSeekerSaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class JobSeekerSaveService {
    private final JobSeekerSaveRepository repository;

    public List<JobSeekerSave> getCandidateJobs(JobSeekerProfile jobSeekerProfile) {
        return repository.findByJobSeekerProfile(jobSeekerProfile);
    }

    public List<JobSeekerSave> getCandidateJobs(JobPostActivity jobSeekerProfile) {
        return repository.findByJobPostActivity(jobSeekerProfile);
    }
}
