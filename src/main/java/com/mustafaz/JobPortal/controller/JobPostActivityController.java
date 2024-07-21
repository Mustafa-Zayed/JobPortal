package com.mustafaz.JobPortal.controller;

import com.mustafaz.JobPortal.entity.JobPostActivity;
import com.mustafaz.JobPortal.entity.Users;
import com.mustafaz.JobPortal.sevices.JobPostActivityService;
import com.mustafaz.JobPortal.sevices.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

@RequiredArgsConstructor
@Controller
public class JobPostActivityController {

    private final UsersService usersService;
    private final JobPostActivityService jobPostActivityService;

    @GetMapping("/dashboard/")
    public String searchJobs(Model model){
        Object currentUserProfile = usersService.getCurrentUserProfile();
        model.addAttribute("user",currentUserProfile);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUserName = authentication.getName();
            model.addAttribute("username",currentUserName);
        }
        return "dashboard";
    }

    // Post New Job button in the dashboard page
    @GetMapping("/dashboard/add")
    public String postNewJob(Model model){

        model.addAttribute("jobPostActivity", new JobPostActivity());
        model.addAttribute("user", usersService.getCurrentUserProfile());

        return "add-jobs";
    }

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
}
