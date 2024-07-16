package com.mustafaz.JobPortal.config;

import com.mustafaz.JobPortal.entity.JobSeekerProfile;
import com.mustafaz.JobPortal.entity.RecruiterProfile;
import com.mustafaz.JobPortal.entity.Users;
import com.mustafaz.JobPortal.repository.JobSeekerProfileRepository;
import com.mustafaz.JobPortal.repository.RecruiterProfileRepository;
import com.mustafaz.JobPortal.repository.UsersRepository;
import com.mustafaz.JobPortal.sevices.UsersService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.IOException;
//@RequiredArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

//    private final UsersService usersService;
//    private final UsersRepository usersRepository;
//    private final RecruiterProfileRepository recruiterProfileRepository;
//    private final JobSeekerProfileRepository jobSeekerProfileRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
//        Object principal = authentication.getPrincipal();
        System.out.printf("The user name: %s is logged in with the principal: %s\n", authentication.getName(), authentication.getPrincipal());

//        boolean hasRecruiterRole = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("Recruiter"));
//        boolean hasJobSeekerRole = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("Job Seeker"));
        boolean hasRecruiterOrJobSeekerRole = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("Recruiter") || r.getAuthority().equals("Job Seeker"));

//        // Another approach, we can get the user from the DB and check the roles.
//        boolean b = usersService.findUsersByEmail(authentication.getName()).stream().anyMatch(r -> r.getUserTypeId().getUserTypeName().equals("Recruiter") || r.getUserTypeId().getUserTypeName().equals("Job Seeker"));

/*
        // We can use the CustomAuthenticationSuccessHandler to add the currentUserProfile and
        // username to the session instead of this searchJobs method
        String name = authentication.getName();
        Users users = usersRepository.findByEmail(name).orElseThrow
                (() -> new UsernameNotFoundException("User not found with email: " + name));

        Integer id = users.getUserTypeId().getUserTypeId();

        // Adding the current user profile to the session
        if (id == 1)
            request.getSession().setAttribute("currentUserProfile"
                    ,recruiterProfileRepository.findById(users.getUserId()).orElse(new RecruiterProfile()));
        else
            request.getSession().setAttribute("currentUserProfile"
                    ,jobSeekerProfileRepository.findById(users.getUserId()).orElse(new JobSeekerProfile()));

        // Adding the current user name to the session
        String currentUserName = authentication.getName();
        request.getSession().setAttribute("username",currentUserName);
*/
        if (hasRecruiterOrJobSeekerRole)
            response.sendRedirect(request.getContextPath() + "/dashboard/");
    }
}
