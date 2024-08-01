package com.mustafaz.JobPortal.controller;

import com.mustafaz.JobPortal.entity.JobPostActivity;
import com.mustafaz.JobPortal.entity.JobSeekerProfile;
import com.mustafaz.JobPortal.entity.JobSeekerSave;
import com.mustafaz.JobPortal.sevices.JobPostActivityService;
import com.mustafaz.JobPortal.sevices.JobSeekerSaveService;
import com.mustafaz.JobPortal.sevices.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class JobSeekerSaveController {

    private final UsersService usersService;
    private final JobPostActivityService jobPostActivityService;
    private final JobSeekerSaveService jobSeekerSaveService;


    // When clicking on Save Job button in job-details page.
    @PostMapping("/job-details/save/{id}")
    public String saveJob(@PathVariable Integer id) {

        JobSeekerProfile jobSeekerProfile = (JobSeekerProfile) usersService.getCurrentUserProfile();
        JobPostActivity jobPostActivity = jobPostActivityService.getJobPostActivityById(id).orElseThrow(
                () -> new RuntimeException("Job not found"));

        JobSeekerSave jobSeekerSave = new JobSeekerSave();
        jobSeekerSave.setJobSeekerProfile(jobSeekerProfile);
        jobSeekerSave.setJobPostActivity(jobPostActivity);

        // It's not mandatory as isSaved is marked as @Transient  so it will not saved in database. For saving
        // which job is saved or applied by user , here we have entity class JobSeekerApply and JobSeekerSave.
        // so we would set this feature after fetching from the DB and before displaying in the dashboard.
        // jobPostActivity.setIsSaved(true);

        jobSeekerSaveService.save(jobSeekerSave);

        return "redirect:/dashboard/";
    }

    // When clicking on View Saved Jobs button in dashboard page.
    @GetMapping("/saved-jobs/")
    public String viewSavedJobs(Model model) {

        Object currentUserProfile = usersService.getCurrentUserProfile();
        List<JobSeekerSave> jobSeekerSaveList = jobSeekerSaveService
                .getCandidateJobs((JobSeekerProfile)currentUserProfile);

        List<JobPostActivity> jobPostList = new ArrayList<>();
        jobSeekerSaveList.forEach(
                jobSeekerSave -> jobPostList.add(jobSeekerSave.getJobPostActivity())
        );

        model.addAttribute("jobPost", jobPostList);
        model.addAttribute("user", currentUserProfile);

        return "saved-jobs";
    }
}
