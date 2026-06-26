package com.sudarsan.onlineexam.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
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
            HttpSession session,
            Model model) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null ||
                !"STUDENT".equalsIgnoreCase(loggedInUser.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("user", loggedInUser);

        return "student-profile";
    }

    // Save or update profile
    @PostMapping("/student/profile")
    public String saveProfile(
            HttpSession session,
            @RequestParam String mobileNumber,
            @RequestParam String hobbies,
            @RequestParam String address,
            @RequestParam(required = false) String dateOfBirth,
            Model model) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null ||
                !"STUDENT".equalsIgnoreCase(loggedInUser.getRole())) {
            return "redirect:/login";
        }

        User user = userRepository.findById(loggedInUser.getId()).orElseThrow();

        user.setMobileNumber(mobileNumber);
        user.setHobbies(hobbies);
        user.setAddress(address);

        if (user.getDateOfBirth() == null &&
                dateOfBirth != null &&
                !dateOfBirth.isBlank()) {

            user.setDateOfBirth(java.time.LocalDate.parse(dateOfBirth));
        }

        userRepository.save(user);

        session.setAttribute("loggedInUser", user);

        model.addAttribute("user", user);
        model.addAttribute("success", "Profile updated successfully.");

        return "student-profile";
    }

    // Deactivate account
    @PostMapping("/student/deactivate")
    public String deactivateAccount(HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null ||
                !"STUDENT".equalsIgnoreCase(loggedInUser.getRole())) {
            return "redirect:/login";
        }

        User user = userRepository.findById(loggedInUser.getId()).orElseThrow();

        user.setStatus("INACTIVE");
        userRepository.save(user);

        session.invalidate();

        return "redirect:/login";
    }
}