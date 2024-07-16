package com.mustafaz.JobPortal.controller;

import com.mustafaz.JobPortal.entity.Users;
import com.mustafaz.JobPortal.repository.UsersRepository;
import com.mustafaz.JobPortal.sevices.UsersService;
import com.mustafaz.JobPortal.sevices.UsersTypeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UsersController {
    private final UsersService usersService;
    private final UsersTypeService usersTypeService;
    private final UsersRepository usersRepository;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new Users());
        model.addAttribute("getAllTypes", usersTypeService.findAll());
        return "register";
    }

    @PostMapping("/register/new")
    public String registerNewUser(@Valid @ModelAttribute("user") Users user, Model model) {

        if (usersService.findUsersByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("error", "Duplicate entry '%s' ,try to login or register with other email.".formatted(user.getEmail()));
            model.addAttribute("getAllTypes", usersTypeService.findAll());
            return "register";
        }

        usersService.save(user);
        return "dashboard";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        System.out.println("From logout method");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null)
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        return "redirect:/";
    }
}
