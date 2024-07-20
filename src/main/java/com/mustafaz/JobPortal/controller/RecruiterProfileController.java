package com.mustafaz.JobPortal.controller;

import com.mustafaz.JobPortal.entity.RecruiterProfile;
import com.mustafaz.JobPortal.entity.Users;
import com.mustafaz.JobPortal.repository.UsersRepository;
import com.mustafaz.JobPortal.sevices.RecruiterProfileService;
import com.mustafaz.JobPortal.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {

    private final RecruiterProfileService recruiterProfileService;
    private final UsersRepository usersRepository;

    // Edit Account button in the dashboard page
    @GetMapping("/")
    public String showRecruiterProfileForm(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserEmail = authentication.getName();

            Optional<RecruiterProfile> recruiterProfile = recruiterProfileService
                    .findByUsers_Email(currentUserEmail);

            recruiterProfile.ifPresent(
                    profile -> model.addAttribute("profile", profile)
            );
        }
        return "recruiter_profile";
    }


    @PostMapping("/addNew")
    public String addNew(@ModelAttribute(name = "profile") RecruiterProfile profile,
                       @RequestParam("image") MultipartFile multipartFile, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            Users users = usersRepository.findByEmail(email).orElseThrow
                    (() -> new UsernameNotFoundException("User not found with email: " + email));

            // Associates the recruiter profile with the existing user account.
            profile.setUsers(users);
            profile.setUserAccountId(users.getUserId());
        }
        model.addAttribute("profile", profile);

        // Associates the image uploaded with the existing user account.
        String fileName = "";
        if (! Objects.requireNonNull(multipartFile.getOriginalFilename()).isEmpty()){
            fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            profile.setProfilePhoto(fileName);
        }
        RecruiterProfile savedRecruiterProfile = recruiterProfileService.save(profile);

        // Setting up the upload directory of where we want to save the image profile.
        String uploadDir = "photos/recruiter/" + savedRecruiterProfile.getUserAccountId();

        try {
            // Read the profile image from the request, the multipart file.
            // And then we save that image on the server and that directory.
            FileUploadUtil.saveFile(uploadDir, multipartFile.getOriginalFilename(), multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return "redirect:/dashboard/";
    }
}
