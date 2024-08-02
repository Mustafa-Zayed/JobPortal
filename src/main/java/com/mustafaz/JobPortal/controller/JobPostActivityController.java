package com.mustafaz.JobPortal.controller;

import com.mustafaz.JobPortal.entity.*;
import com.mustafaz.JobPortal.sevices.JobPostActivityService;
import com.mustafaz.JobPortal.sevices.JobSeekerApplyService;
import com.mustafaz.JobPortal.sevices.JobSeekerSaveService;
import com.mustafaz.JobPortal.sevices.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Controller
public class JobPostActivityController {

    private final UsersService usersService;
    private final JobPostActivityService jobPostActivityService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobSeekerSaveService;

    @GetMapping("/dashboard/")
    public String searchJobs(
            Model model,
            @RequestParam(name = "job", required = false) String job,
            @RequestParam(name = "location", required = false) String location,
            @RequestParam(name = "partTime", required = false) String partTime,
            @RequestParam(name = "fullTime", required = false) String fullTime,
            @RequestParam(name = "freelance", required = false) String freelance,
            @RequestParam(name = "internship", required = false) String internship,
            @RequestParam(name = "remoteOnly", required = false) String remoteOnly,
            @RequestParam(name = "officeOnly", required = false) String officeOnly,
            @RequestParam(name = "partialRemote", required = false) String partialRemote,
            @RequestParam(name = "today", required = false) boolean today,
            @RequestParam(name = "days7", required = false) boolean days7,
            @RequestParam(name = "days30", required = false) boolean days30
    ) {

        List<JobPostActivity> searchedJobList = getSearchedJobList(
                model, job,
                location, partTime, fullTime, freelance, internship,
                remoteOnly, officeOnly, partialRemote,
                today, days7, days30
        );

        Object currentUserProfile = usersService.getCurrentUserProfile();
        model.addAttribute("user",currentUserProfile);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUserName = authentication.getName();
            model.addAttribute("username",currentUserName);

/*
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))){
                Users currentUser = usersService.getCurrentUser();
                List<JobPostActivity> searchedJobList = currentUser.getJobPostActivities();
                Integer totalCandidates = jobPostActivityService.getTotalCandidates(currentUser.getUserId());
                model.addAttribute("searchedJobList", searchedJobList);
                model.addAttribute("totalCandidates", totalCandidates);
            }
*/
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                List<RecruiterJobsDto> recruiterJobs = jobPostActivityService
                        .getRecruiterJobs(((RecruiterProfile) currentUserProfile).getUserAccountId());
                model.addAttribute("jobPost", recruiterJobs);
            } else {
                List<JobSeekerApply> applyList = jobSeekerApplyService
                        .getCandidateJobs(((JobSeekerProfile) currentUserProfile));

                List<JobSeekerSave> saveList = jobSeekerSaveService.
                        getCandidateJobs(((JobSeekerProfile) currentUserProfile));

                boolean exist;
                boolean save;

                // set the status of each job in the searchedJobList to active or saved.
                for (JobPostActivity searchedJob : searchedJobList) {
                    exist = false;
                    save = false;

                    // if the searchedJob is in the applyList, then it should be active or applied.
                    for (JobSeekerApply apply : applyList){
                        if (Objects.equals(searchedJob.getJobPostId(),
                                apply.getJobPostActivity().getJobPostId())){
                            searchedJob.setIsActive(true);
                            exist = true;
                            break;
                        }
                    }

                    // if the searchedJob is in the saveList, then it should be saved.
                    for (JobSeekerSave saved : saveList){
                        if (Objects.equals(searchedJob.getJobPostId(),
                                saved.getJobPostActivity().getJobPostId())){
                            searchedJob.setIsSaved(true);
                            save = true;
                            break;
                        }
                    }

                    if (!exist){
                        searchedJob.setIsActive(false);
                    }

                    if (!save){
                        searchedJob.setIsSaved(false);
                    }
                }

                model.addAttribute("jobPost", searchedJobList);
            }
        }
        return "dashboard";
//        return "dashboardTest";
    }

    @GetMapping("/global-search/")
    public String globalSearch(
            Model model,
            @RequestParam(name = "job", required = false) String job,
            @RequestParam(name = "location", required = false) String location,

            @RequestParam(name = "partTime", required = false) String partTime,
            @RequestParam(name = "fullTime", required = false) String fullTime,
            @RequestParam(name = "freelance", required = false) String freelance,
            @RequestParam(name = "internship", required = false) String internship,
            @RequestParam(name = "remoteOnly", required = false) String remoteOnly,
            @RequestParam(name = "officeOnly", required = false) String officeOnly,
            @RequestParam(name = "partialRemote", required = false) String partialRemote,
            @RequestParam(name = "today", required = false) boolean today,
            @RequestParam(name = "days7", required = false) boolean days7,
            @RequestParam(name = "days30", required = false) boolean days30
    ) {

        List<JobPostActivity> searchedJobList = getSearchedJobList(
                model, job,
                location, partTime, fullTime, freelance, internship,
                remoteOnly, officeOnly, partialRemote,
                today, days7, days30
        );

        model.addAttribute("jobPost", searchedJobList);
        return "global-search";
    }

    private List<JobPostActivity> getSearchedJobList(
            Model model,
            String job,
            String location,
            String partTime,
            String fullTime,
            String freelance,
            String internship,
            String remoteOnly,
            String officeOnly,
            String partialRemote,
            boolean today,
            boolean days7,
            boolean days30
    ) {

        model.addAttribute("job", job);
        model.addAttribute("location", location);

        model.addAttribute("partTime", partTime);
        model.addAttribute("fullTime", fullTime);
        model.addAttribute("freelance", freelance);
        model.addAttribute("internship", internship);

        model.addAttribute("remoteOnly", remoteOnly);
        model.addAttribute("officeOnly", officeOnly);
        model.addAttribute("partialRemote", partialRemote);

        model.addAttribute("today", today);
        model.addAttribute("days7", days7);
        model.addAttribute("days30", days30);

        LocalDate searchDate = null;
        List<JobPostActivity> searchedJobList;

        // boolean flags to know if we need to search by date, remote or the type of job accordingly.
        boolean dateSearchFlag = true;
        boolean remoteFlag = true;
        boolean typeFlag = true;

        if (today){
            searchDate = LocalDate.now();
        } else if (days7) {
            searchDate = LocalDate.now().minusDays(7);
        } else if (days30) {
            searchDate = LocalDate.now().minusDays(30);
        } else {
            dateSearchFlag = false;
        }

        // in case these values didn't come in, we'll set in the appropriate defaults accordingly.
        if (fullTime == null && partTime == null && freelance == null && internship == null) {
            partTime = "Part-Time";
            fullTime = "Full-Time";
            freelance = "Freelance";
            internship = "Internship";
            remoteFlag = false;
        }

        if (officeOnly == null && remoteOnly == null && partialRemote == null) {
            officeOnly = "Office-Only";
            remoteOnly = "Remote-Only";
            partialRemote = "Partial-Remote";
            typeFlag = false;
        }

        // if we don't have any of the flags selected, we'll get all jobs.
        if (!dateSearchFlag && !remoteFlag && !typeFlag && !StringUtils.hasText(job)
                && !StringUtils.hasText(location)) {

            searchedJobList = jobPostActivityService.getAll();
        } else {

            // otherwise, we'll search for jobs based on the appropriate flags
            searchedJobList = jobPostActivityService.search(job, location,
                    Arrays.asList(officeOnly, remoteOnly, partialRemote),
                    Arrays.asList(partTime, fullTime, freelance, internship),
                    searchDate);
        }
        return searchedJobList;
    }

    // Post New Job button in the dashboard page
    @GetMapping("/dashboard/add")
    public String postNewJob(Model model){

        model.addAttribute("jobPostActivity", new JobPostActivity());
        model.addAttribute("user", usersService.getCurrentUserProfile());

        return "add-jobs";
    }

    // When clicking on Save button in add-jobs page.
    @PostMapping("/dashboard/addNew")
    public String saveNewJob(@ModelAttribute JobPostActivity jobPostActivity){

        Users currentUser = usersService.getCurrentUser();

        if (currentUser != null){
            jobPostActivity.setPostedById(currentUser);
            jobPostActivity.setPostedDate(new Date(System.currentTimeMillis()));
        }

        jobPostActivityService.save(jobPostActivity);

        return "redirect:/dashboard/";
    }

    // When clicking on Edit Job button in job-details page.
    @PostMapping("/dashboard/edit/{id}")
    public String editJob(@PathVariable Integer id, Model model){

        JobPostActivity jobPostActivity = jobPostActivityService.getJobPostActivityById(id).orElseThrow(
                () -> new RuntimeException("Job not found")
        );

        model.addAttribute("jobPostActivity", jobPostActivity);
        model.addAttribute("user", usersService.getCurrentUserProfile());

        return "add-jobs";
    }
}
