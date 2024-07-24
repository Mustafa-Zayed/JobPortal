package com.mustafaz.JobPortal.sevices;

import com.mustafaz.JobPortal.entity.JobSeekerProfile;
import com.mustafaz.JobPortal.entity.RecruiterProfile;
import com.mustafaz.JobPortal.entity.Users;
import com.mustafaz.JobPortal.repository.JobSeekerProfileRepository;
import com.mustafaz.JobPortal.repository.RecruiterProfileRepository;
import com.mustafaz.JobPortal.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UsersService implements UserDetailsService {
    private final UsersRepository usersRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public Users save(Users user) {
        user.setIsActive(true);
        user.setRegistrationDate(new Date(System.currentTimeMillis()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Users savedUser = usersRepository.save(user); // user is saved first to ensure it has a generated ID

        if (user.getUserTypeId().getUserTypeId() == 1) // Recruiter
            recruiterProfileRepository.save(RecruiterProfile.builder().users(savedUser).build());
        else  // Job Seeker
            jobSeekerProfileRepository.save(JobSeekerProfile.builder().users(savedUser).build());

        return savedUser;
    }

    public Optional<Users> findUsersByEmail(String email) {
        return usersRepository.findByEmail(email);
    }


    public Object getCurrentUserProfile() {

        Users users = getCurrentUser();
        Integer id = users.getUserTypeId().getUserTypeId();

        if (id == 1)
            return recruiterProfileRepository.findById(users.getUserId()).orElse(new RecruiterProfile());
        else
            return jobSeekerProfileRepository.findById(users.getUserId()).orElse(new JobSeekerProfile());
    }

    public Users getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) { // AnonymousAuthenticationToken is a special token used by Spring Security to represent an anonymous (not logged-in) user. We ensure that only authenticated users’ names are processed and added to the model.
            String username = authentication.getName();

            return usersRepository.findByEmail(username).orElseThrow(
                    () -> new UsernameNotFoundException("Could not found " + "user")
            );
        }

        return null;
    }

}
