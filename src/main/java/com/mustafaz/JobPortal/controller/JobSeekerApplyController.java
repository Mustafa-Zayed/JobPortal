package com.mustafaz.JobPortal.controller;

import com.mustafaz.JobPortal.entity.*;
import com.mustafaz.JobPortal.sevices.JobPostActivityService;
import com.mustafaz.JobPortal.sevices.JobSeekerApplyService;
import com.mustafaz.JobPortal.sevices.JobSeekerSaveService;
import com.mustafaz.JobPortal.sevices.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Controller
public class JobSeekerApplyController {

    private final JobPostActivityService jobPostActivityService;
    private final UsersService usersService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobSeekerSaveService;

    // When clicking on a job in dashboard page.
    @GetMapping("/job-details-apply/{id}")
    public String displayJob(@PathVariable Integer id, Model model) {

        JobPostActivity jobPostActivity = jobPostActivityService.getJobPostActivityById(id).orElseThrow(
                () -> new RuntimeException("Job not found"));

        Users currentUser = usersService.getCurrentUser();
        Object currentUserProfile = usersService.getCurrentUserProfile();

        RecruiterProfile recruiterProfile;
        JobSeekerProfile jobSeekerProfile;

        List<JobSeekerApply> applyList = jobSeekerApplyService.getCandidateJobs(jobPostActivity);
        List<JobSeekerSave> saveList = jobSeekerSaveService.getCandidateJobs(jobPostActivity);

        if (currentUser.getUserTypeId().getUserTypeId() == 1) {

            recruiterProfile = (RecruiterProfile) currentUserProfile;
            model.addAttribute("user", recruiterProfile);
            model.addAttribute("applyList", applyList); // Candidates Applied for Job

        } else {

            jobSeekerProfile = (JobSeekerProfile) currentUserProfile;
            model.addAttribute("user", jobSeekerProfile);

            boolean alreadyApplied = false;
            boolean alreadySaved = false;

            for (JobSeekerApply apply : applyList) {
                if (Objects.equals(apply.getJobSeekerProfile().getUserAccountId(),
                        jobSeekerProfile.getUserAccountId()) // also by jobPostActivity Objects.equals(apply.getJobPostActivity().getJobPostId(), jobPostActivity.getJobPostId())
                ) {
                    alreadyApplied = true;
                    break;
                }
            }
            for (JobSeekerSave save : saveList) {
                if (Objects.equals(save.getJobSeekerProfile().getUserAccountId(),
                        jobSeekerProfile.getUserAccountId())
                ) {
                    alreadySaved = true;
                    break;
                }
            }

            model.addAttribute("alreadyApplied", alreadyApplied);
            model.addAttribute("alreadySaved", alreadySaved);
        }

        JobSeekerApply jobSeekerApply = new JobSeekerApply();
        model.addAttribute("applyJob", jobSeekerApply);

        model.addAttribute("jobDetails", jobPostActivity);

        return "job-details";
    }

    // When clicking on Apply button in job-details page.
    @PostMapping("/job-details/apply/{id}")
    public String applyForJob(@PathVariable Integer id) {

        JobSeekerProfile jobSeekerProfile = (JobSeekerProfile) usersService.getCurrentUserProfile();
        JobPostActivity jobPostActivity = jobPostActivityService.getJobPostActivityById(id).orElseThrow(
                () -> new RuntimeException("Job not found"));

        JobSeekerApply jobSeekerApply = new JobSeekerApply();
        jobSeekerApply.setJobSeekerProfile(jobSeekerProfile);
        jobSeekerApply.setJobPostActivity(jobPostActivity);
        jobSeekerApply.setApplyDate(new Date());

        // It's not mandatory as isActive is marked as @Transient  so it will not saved in database. For saving
        // which job is saved or applied by user , here we have entity class JobSeekerApply and JobSeekerSave.
        // so we would set this feature after fetching from the DB and before displaying in the dashboard.
        // jobPostActivity.setIsActive(true);

        jobSeekerApplyService.save(jobSeekerApply);

        return "redirect:/dashboard/";
    }
}
