package com.mustafaz.JobPortal.sevices;

import com.mustafaz.JobPortal.entity.JobPostActivity;
import com.mustafaz.JobPortal.repository.JobPostActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JobPostActivityService {

    private final JobPostActivityRepository repository;

    public JobPostActivity save(JobPostActivity jobPostActivity){
        return repository.save(jobPostActivity);
    }
}
