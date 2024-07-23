package com.mustafaz.JobPortal.sevices;

import com.mustafaz.JobPortal.entity.*;
import com.mustafaz.JobPortal.repository.JobPostActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class JobPostActivityService {

    private final JobPostActivityRepository repository;

    public JobPostActivity save(JobPostActivity jobPostActivity){
        return repository.save(jobPostActivity);
    }

    public List<RecruiterJobsDto> getRecruiterJobs(int recruiter) {

        List<IRecruiterJobs> recruiterJobs = repository.getRecruiterJobs(recruiter);
        List<RecruiterJobsDto> jobsDtoList = new ArrayList<>();

        for (IRecruiterJobs recruiterJob : recruiterJobs) {

            JobLocation jobLocation = JobLocation.builder()
                    .id(recruiterJob.getLocationId())
                    .city(recruiterJob.getCity())
                    .country(recruiterJob.getCountry())
                    .state(recruiterJob.getState())
                    .build();

            JobCompany jobCompany = JobCompany.builder()
                    .id(recruiterJob.getCompanyId())
                    .name(recruiterJob.getName())
                    .logo("")
                    .build();

            RecruiterJobsDto recruiterJobsDto = new RecruiterJobsDto(recruiterJob.getTotalCandidates()
                    , recruiterJob.getJob_post_id(), recruiterJob.getJob_title(), jobLocation, jobCompany);

            jobsDtoList.add(recruiterJobsDto);
        }

        return jobsDtoList;
    }

/*
    public Integer getTotalCandidates(int recruiter) {
        return repository.getTotalCandidates(recruiter);
    }
*/

    public Optional<JobPostActivity> getJobPostActivityById(int jobPostId) {
        return repository.findById(jobPostId);
    }
}
