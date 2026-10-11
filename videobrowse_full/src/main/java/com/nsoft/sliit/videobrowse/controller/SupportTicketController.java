package com.nsoft.sliit.videobrowse.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nsoft.sliit.videobrowse.entity.SupportTicket;
import com.nsoft.sliit.videobrowse.service.SupportTicketService;

@Controller
@RequestMapping("/support")
public class SupportTicketController {
    private final SupportTicketService service;
    
    public SupportTicketController(SupportTicketService service){ 
        this.service = service; 
    }

    @GetMapping("/new")
    public String form(Model m, Authentication auth, RedirectAttributes redirectAttributes){
        // Check if user is admin - admins cannot create tickets
        if (auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            redirectAttributes.addFlashAttribute("error", "Admins cannot create support tickets");
            return "redirect:/";
        }
        
        m.addAttribute("ticket", new SupportTicket());
        return "support/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute SupportTicket ticket,
                         @RequestParam(required=false, defaultValue="GEN") String type,
                         @RequestParam(required=false) String extra,
                         Authentication auth,
                         RedirectAttributes redirectAttributes){
        // Check if user is admin - admins cannot create tickets
        if (auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            redirectAttributes.addFlashAttribute("error", "Admins cannot create support tickets");
            return "redirect:/";
        }
        
        String username = auth != null ? auth.getName() : ticket.getUsername();
        service.createTyped(username, ticket.getEmail(), ticket.getMessage(), type, extra);
        redirectAttributes.addFlashAttribute("success", "Support ticket submitted successfully!");
        return "redirect:/support/my";
    }

    @GetMapping("/list")
    public String list(Model m, Authentication auth, RedirectAttributes redirectAttributes){
        // Check if user is admin - only admins can view ticket list
        if (auth == null || !auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            redirectAttributes.addFlashAttribute("error", "Access denied. Admin privileges required.");
            return "redirect:/";
        }
        
        m.addAttribute("tickets", service.findAll());
        return "support/list";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam String status, 
                              Authentication auth, RedirectAttributes redirectAttributes){
        // Check if user is admin - only admins can update ticket status
        if (auth == null || !auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            redirectAttributes.addFlashAttribute("error", "Access denied. Admin privileges required.");
            return "redirect:/";
        }
        
        service.updateStatus(id, status);
        redirectAttributes.addFlashAttribute("success", "Ticket status updated successfully!");
        return "redirect:/support/list";
    }

    @PostMapping("/{id}/note")
    public String addNote(@PathVariable Long id, @RequestParam String note, 
                         Authentication auth, RedirectAttributes redirectAttributes){
        // Check if user is admin - only admins can add notes
        if (auth == null || !auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            redirectAttributes.addFlashAttribute("error", "Access denied. Admin privileges required.");
            return "redirect:/";
        }
        
        service.addNote(id, note);
        redirectAttributes.addFlashAttribute("success", "Note added successfully!");
        return "redirect:/support/list";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Authentication auth, RedirectAttributes redirectAttributes){
        // Allow admins to delete any ticket, and users to delete their own tickets
        boolean isAdmin = auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (!isAdmin) {
            try {
                String username = auth != null ? auth.getName() : null;
                service.findByIdAndUsername(id, username).orElseThrow();
                service.delete(id);
                redirectAttributes.addFlashAttribute("success", "Ticket deleted successfully!");
                return "redirect:/support/my";
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Access denied or ticket not found.");
                return "redirect:/";
            }
        } else {
            service.delete(id);
            redirectAttributes.addFlashAttribute("success", "Ticket deleted successfully!");
            return "redirect:/support/list";
        }
    }

    @GetMapping("/my")
    public String myTickets(Model m, Authentication auth){
        String username = auth != null ? auth.getName() : null;
        m.addAttribute("tickets", service.findByUsername(username));
        return "support/my";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model m, Authentication auth, RedirectAttributes redirectAttributes){
        String username = auth != null ? auth.getName() : null;
        return service.findByIdAndUsername(id, username)
                .map(t -> { m.addAttribute("ticket", t); return "support/edit"; })
                .orElseGet(() -> { redirectAttributes.addFlashAttribute("error", "Ticket not found or access denied"); return "redirect:/support/my"; });
    }

    @PostMapping("/{id}/edit")
    public String editSubmit(@PathVariable Long id, @RequestParam String message, Authentication auth, RedirectAttributes redirectAttributes){
        String username = auth != null ? auth.getName() : null;
        try {
            service.updateMessage(id, username, message);
            redirectAttributes.addFlashAttribute("success", "Ticket updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ticket not found or access denied");
        }
        return "redirect:/support/my";
    }
}
