package com.mustafaz.JobPortal.controller;

import com.mustafaz.JobPortal.entity.JobSeekerProfile;
import com.mustafaz.JobPortal.entity.Skills;
import com.mustafaz.JobPortal.entity.Users;
import com.mustafaz.JobPortal.sevices.JobSeekerProfileService;
import com.mustafaz.JobPortal.sevices.UsersService;
import com.mustafaz.JobPortal.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/job-seeker-profile")
public class JobSeekerProfileController {

    private final JobSeekerProfileService jobSeekerProfileService;
    private final UsersService usersService;

    // Edit Profile button in the dashboard page
    @GetMapping("/")
    public String showJobSeekerProfileForm(Model model) {
        Users currentUser = usersService.getCurrentUser();

        if (currentUser.getUserTypeId().getUserTypeId() == 2) { // not necessary to check, as the link that redirects to this controller is shown for candidates only.
            Optional<JobSeekerProfile> jobSeekerProfile = jobSeekerProfileService
                    .getJobSeekerProfile(currentUser.getUserId());

            if (jobSeekerProfile.isPresent()){

                JobSeekerProfile profile = jobSeekerProfile.get();

                List<Skills> skills = profile.getSkills(); // returns a reference to the original skills list

                if (skills.isEmpty())
                    skills.add(new Skills());

                // profile.setSkills(skills); // not necessary as modifying skills list modifies the list within the jobSeekerProfile as it's a reference not a copy, then adding a Skills object directly to this list is sufficient. There is no need to set the list back to the jobSeekerProfile object.

                model.addAttribute("profile", profile);

                return "job-seeker-profile";
            }
        }

        return null;
    }


    @PostMapping("/addNew")
    public String addNew(@ModelAttribute(name = "profile") JobSeekerProfile profile,
                         @RequestParam("image") MultipartFile image,
                         @RequestParam("pdf") MultipartFile pdf,
                         Model model) {

        Users currentUser = usersService.getCurrentUser();

        profile.setUsers(currentUser);
        profile.setUserAccountId(currentUser.getUserId());

        List<Skills> skills = profile.getSkills();
        // set the jobSeekerProfile to each skill
        for (Skills skill : skills) {
            skill.setJobSeekerProfile(profile);
        }

        String imageName = "";
        String pdfName = "";
        if (!Objects.requireNonNull(image.getOriginalFilename()).isEmpty()
                && !Objects.requireNonNull(pdf.getOriginalFilename()).isEmpty()) {
            imageName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            profile.setProfilePhoto(imageName);

            pdfName = StringUtils.cleanPath(Objects.requireNonNull(pdf.getOriginalFilename()));
            profile.setResume(pdfName);
        }

        model.addAttribute("profile", profile);
        JobSeekerProfile savedJobSeekerProfile = jobSeekerProfileService.save(profile);

        String uploadDir = "photos/candidate/" + savedJobSeekerProfile.getUserAccountId();
        try {
            FileUploadUtil.saveFile(uploadDir, imageName, image);
            FileUploadUtil.saveFile(uploadDir, pdfName, pdf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/dashboard/";
    }
}
