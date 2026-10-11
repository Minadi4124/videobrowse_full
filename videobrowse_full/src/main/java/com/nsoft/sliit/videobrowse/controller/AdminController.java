package com.nsoft.sliit.videobrowse.controller;

import com.nsoft.sliit.videobrowse.repository.UserRepository;
import com.nsoft.sliit.videobrowse.repository.VideoRepository;
import com.nsoft.sliit.videobrowse.repository.SupportTicketRepository;
import com.nsoft.sliit.videobrowse.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.nsoft.sliit.videobrowse.service.VideoService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserRepository userRepo;
    private final VideoRepository videoRepo;
    private final SupportTicketRepository ticketRepo;
    private final UserService userService;
    private final VideoService videoService;

    public AdminController(UserRepository userRepo, VideoRepository videoRepo, SupportTicketRepository ticketRepo, UserService userService, VideoService videoService) {
        this.userRepo = userRepo;
        this.videoRepo = videoRepo;
        this.ticketRepo = ticketRepo;
        this.userService = userService;
        this.videoService = videoService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model m){
        m.addAttribute("userCount", userRepo.count());
        m.addAttribute("videoCount", videoRepo.count());
        m.addAttribute("ticketCount", ticketRepo.count());
        m.addAttribute("users", userRepo.findAll());
        m.addAttribute("videos", videoRepo.findAll());
        m.addAttribute("tickets", ticketRepo.findAll());
        return "admin/dashboard";
    }

    @PostMapping("/videos/{id}/delete")
    public String deleteVideo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("Admin attempting to delete video with ID: " + id);
            videoService.delete(id);
            redirectAttributes.addFlashAttribute("deleteSuccess", "Video deleted successfully!");
            System.out.println("Video deleted successfully by admin");
        } catch (Exception e) {
            System.err.println("Error deleting video: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("deleteError", "Failed to delete video: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
    
    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("Admin attempting to delete user with ID: " + id);
            userService.deleteUserAndRelatedData(id);
            redirectAttributes.addFlashAttribute("deleteSuccess", "User deleted successfully!");
            System.out.println("User deleted successfully by admin");
        } catch (Exception e) {
            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("deleteError", "Failed to delete user: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
}
