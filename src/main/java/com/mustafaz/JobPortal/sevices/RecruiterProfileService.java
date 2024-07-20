package com.mustafaz.JobPortal.sevices;

import com.mustafaz.JobPortal.entity.RecruiterProfile;
import com.mustafaz.JobPortal.repository.RecruiterProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RecruiterProfileService {
    private final RecruiterProfileRepository recruiterProfileRepository;

    public Optional<RecruiterProfile> findByUsers_Email(String email) {
        return recruiterProfileRepository.findByUsers_Email(email);
    }

    public RecruiterProfile save(RecruiterProfile recruiterProfile){
        return recruiterProfileRepository.save(recruiterProfile);
    }
}
