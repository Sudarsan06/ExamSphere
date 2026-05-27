package com.sudarsan.onlineexam.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.sudarsan.onlineexam.entity.User;
import com.sudarsan.onlineexam.repository.UserRepository;

@Controller
public class StudentProfileController {

    private final UserRepository userRepository;

    public StudentProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Show profile page
    @GetMapping("/student/profile")
    public String showProfile(
            @RequestParam String username,
            Model model) {

        Optional<User> optionalUser =
                userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return "redirect:/login";
        }

        model.addAttribute("user", optionalUser.get());
        return "student-profile";
    }

    // Save or update profile
    @PostMapping("/student/profile")
    public String saveProfile(
            @RequestParam Long id,
            @RequestParam String mobileNumber,
            @RequestParam String hobbies,
            @RequestParam String address,
            @RequestParam(required = false) String dateOfBirth,
            Model model) {

        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return "redirect:/login";
        }

        User user = optionalUser.get();

        user.setMobileNumber(mobileNumber);
        user.setHobbies(hobbies);
        user.setAddress(address);

        if (user.getDateOfBirth() == null &&
        	    dateOfBirth != null &&
        	    !dateOfBirth.isBlank()) {
        	    user.setDateOfBirth(java.time.LocalDate.parse(dateOfBirth));
        	}

        userRepository.save(user);

        model.addAttribute("user", user);
        model.addAttribute("success", "Profile updated successfully.");

        return "student-profile";
    }

    // Deactivate account
    @PostMapping("/student/deactivate")
    public String deactivateAccount(
            @RequestParam Long id,
            Model model) {

        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return "redirect:/login";
        }

        User user = optionalUser.get();
        user.setStatus("INACTIVE");
        userRepository.save(user);

        model.addAttribute("course", user.getCourse());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("status", user.getStatus());

        return "student-dashboard";
    }
}