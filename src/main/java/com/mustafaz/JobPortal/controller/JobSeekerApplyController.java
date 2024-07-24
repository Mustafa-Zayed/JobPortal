package com.mustafaz.JobPortal.controller;

import com.mustafaz.JobPortal.entity.JobPostActivity;
import com.mustafaz.JobPortal.entity.RecruiterProfile;
import com.mustafaz.JobPortal.sevices.JobPostActivityService;
import com.mustafaz.JobPortal.sevices.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class JobSeekerApplyController {

    private final JobPostActivityService jobPostActivityService;
    private final UsersService usersService;

    // When clicking on a job in dashboard page.
    @GetMapping("/job-details-apply/{id}")
    public String displayJob(@PathVariable Integer id, Model model) {

        JobPostActivity jobPostActivity = jobPostActivityService.getJobPostActivityById(id).orElseThrow(
                () -> new RuntimeException("Job not found"));

        model.addAttribute("jobDetails", jobPostActivity);
        model.addAttribute("user", (RecruiterProfile)usersService.getCurrentUserProfile());

        return "job-details";
    }
}
