package com.mustafaz.JobPortal.sevices;

import com.mustafaz.JobPortal.entity.JobSeekerProfile;
import com.mustafaz.JobPortal.repository.JobSeekerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class JobSeekerProfileService {

    private final JobSeekerProfileRepository repository;

    public Optional<JobSeekerProfile> getJobSeekerProfile(Integer userId) {
        return repository.findById(userId);
    }

    public JobSeekerProfile save(JobSeekerProfile profile) {
        return repository.save(profile);
    }
}
