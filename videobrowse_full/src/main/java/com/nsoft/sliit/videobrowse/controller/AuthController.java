package com.nsoft.sliit.videobrowse.controller;

import com.nsoft.sliit.videobrowse.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final UserService userService;
    public AuthController(UserService userService){ this.userService = userService; }

    public static class RegisterForm {
        public String username;
        public String email;
        public String password;
        public boolean admin;
        // getters/setters
        public String getUsername(){return username;}
        public void setUsername(String v){username=v;}
        public String getEmail(){return email;}
        public void setEmail(String v){email=v;}
        public String getPassword(){return password;}
        public void setPassword(String v){password=v;}
        public boolean isAdmin(){return admin;}
        public void setAdmin(boolean v){admin=v;}
    }

    @GetMapping("/login")
    public String login(@org.springframework.web.bind.annotation.RequestParam(value = "redirect", required = false) String redirect, org.springframework.ui.Model model){
        if (redirect != null && !redirect.isBlank()) model.addAttribute("redirect", redirect);
        return "auth/login";
    }

    @GetMapping("/register")
    public String register(Model m){
        m.addAttribute("form", new RegisterForm());
        return "auth/register";
    }

    @PostMapping("/register")
    public String doRegister(@ModelAttribute("form") RegisterForm form, BindingResult br, Model m){
        System.out.println("Registration attempt - Username: " + form.username + ", Email: " + form.email);
        
        // Manual validation
        if (form.username == null || form.username.trim().isEmpty()) {
            System.out.println("Validation failed: Username is empty");
            m.addAttribute("error", "Username is required");
            return "auth/register";
        }
        if (form.email == null || form.email.trim().isEmpty()) {
            System.out.println("Validation failed: Email is empty");
            m.addAttribute("error", "Email is required");
            return "auth/register";
        }
        if (!form.email.contains("@")) {
            System.out.println("Validation failed: Invalid email format");
            m.addAttribute("error", "Please enter a valid email address");
            return "auth/register";
        }
        if (form.password == null || form.password.trim().isEmpty()) {
            System.out.println("Validation failed: Password is empty");
            m.addAttribute("error", "Password is required");
            return "auth/register";
        }
        if (form.password.length() < 3) {
            System.out.println("Validation failed: Password too short");
            m.addAttribute("error", "Password must be at least 3 characters long");
            return "auth/register";
        }
        
        try {
            System.out.println("Attempting to register user: " + form.username.trim());
            userService.register(form.username.trim(), form.email.trim(), form.password, form.admin);
            System.out.println("User registration successful: " + form.username.trim());
        } catch (Exception ex){
            System.err.println("Registration failed: " + ex.getMessage());
            ex.printStackTrace();
            m.addAttribute("error", ex.getMessage());
            return "auth/register";
        }
        return "redirect:/login?registered";
    }
}
