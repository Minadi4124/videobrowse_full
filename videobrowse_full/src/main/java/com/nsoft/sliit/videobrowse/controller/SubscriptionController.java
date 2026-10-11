package com.nsoft.sliit.videobrowse.controller;

import com.nsoft.sliit.videobrowse.repository.AlertRepository;
import com.nsoft.sliit.videobrowse.repository.NotificationPreferenceRepository;
import com.nsoft.sliit.videobrowse.service.SubscriptionService;
import com.nsoft.sliit.videobrowse.service.SubscriptionPlanService;
import com.nsoft.sliit.videobrowse.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    private final SubscriptionPlanService subscriptionPlanService;
    private final UserService userService;
    private final NotificationPreferenceRepository prefRepo;
    private final AlertRepository alertRepository;

    public SubscriptionController(SubscriptionService subscriptionService, SubscriptionPlanService subscriptionPlanService, UserService userService, NotificationPreferenceRepository prefRepo, AlertRepository alertRepository) {
        this.subscriptionService = subscriptionService;
        this.subscriptionPlanService = subscriptionPlanService;
        this.userService = userService;
        this.prefRepo = prefRepo;
        this.alertRepository = alertRepository;
    }

    @GetMapping
    public String list(Model model){
        var user = userService.getCurrentUser().orElseThrow();
        model.addAttribute("subs", subscriptionService.list(user));
        model.addAttribute("prefs", prefRepo.findByUser(user).orElse(null));
        model.addAttribute("alerts", alertRepository.findByUserOrderByCreatedAtDesc(user));
        return "subscriptions/list";
    }

    @GetMapping("/plans")
    public String plans(Model model){
        // Get plans from database
        var plans = subscriptionPlanService.getAllActivePlans();
        model.addAttribute("plans", plans);
        return "subscriptions/plans";
    }

    @GetMapping("/demo-payment")
    public String demoPayment(@RequestParam String plan, Model model){
        model.addAttribute("plan", plan);
        return "subscriptions/demo-payment";
    }

    @PostMapping
    public String subscribe(@RequestParam String creatorName, @RequestParam(defaultValue = "basic") String code){
        var user = userService.getCurrentUser().orElseThrow();
        
        // Set price based on code
        String price = switch (code) {
            case "basic" -> "Rs. 3000";
            case "standard" -> "Rs. 5000";
            case "premium" -> "Rs. 10000";
            default -> "Rs. 3000";
        };
        
        subscriptionService.subscribe(user, creatorName, code, price);
        return "redirect:/subscriptions";
    }

    @PostMapping("/unsubscribe")
    public String unsubscribe(@RequestParam String creatorName){
        var user = userService.getCurrentUser().orElseThrow();
        subscriptionService.unsubscribe(user, creatorName);
        return "redirect:/subscriptions";
    }

    @PostMapping("/prefs")
    public String updatePrefs(@RequestParam(required=false, defaultValue="false") boolean email,
                              @RequestParam(required=false, defaultValue="true") boolean inapp){
        var user = userService.getCurrentUser().orElseThrow();
        var pref = prefRepo.findByUser(user).orElseGet(() -> {
            var p = new com.nsoft.sliit.videobrowse.entity.NotificationPreference();
            p.setUser(user);
            return p;
        });
        pref.setEmailEnabled(email);
        pref.setInAppEnabled(inapp);
        prefRepo.save(pref);
        return "redirect:/subscriptions";
    }

    @PostMapping("/alerts/{id}/read")
    public String markRead(@PathVariable Long id){
        alertRepository.findById(id).ifPresent(a -> { a.setReadFlag(true); alertRepository.save(a); });
        return "redirect:/subscriptions";
    }
}


