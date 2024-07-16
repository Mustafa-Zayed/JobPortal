package com.mustafaz.JobPortal.sevices;

import com.mustafaz.JobPortal.entity.UsersType;
import com.mustafaz.JobPortal.repository.UsersTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UsersTypeService {
    private final UsersTypeRepository usersTypeRepository;

    public List<UsersType> findAll() {
        return usersTypeRepository.findAll();
    }
}
