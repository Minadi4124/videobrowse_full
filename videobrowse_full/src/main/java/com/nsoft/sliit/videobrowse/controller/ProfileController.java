package com.nsoft.sliit.videobrowse.controller;

import com.nsoft.sliit.videobrowse.entity.User;
import com.nsoft.sliit.videobrowse.entity.UserProfile;
import com.nsoft.sliit.videobrowse.service.SearchHistoryService;
import com.nsoft.sliit.videobrowse.service.impl.PersonalizationFacade;
import com.nsoft.sliit.videobrowse.service.UserProfileService;
import com.nsoft.sliit.videobrowse.service.UserService;
import com.nsoft.sliit.videobrowse.service.VideoService;
import com.nsoft.sliit.videobrowse.service.WatchHistoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final UserService userService;
    private final UserProfileService userProfileService;
    private final WatchHistoryService watchHistoryService;
    private final SearchHistoryService searchHistoryService;
    private final PersonalizationFacade personalizationFacade;
    private final VideoService videoService;

    public ProfileController(UserService userService, UserProfileService userProfileService, WatchHistoryService watchHistoryService, SearchHistoryService searchHistoryService, PersonalizationFacade personalizationFacade, VideoService videoService) {
        this.userService = userService;
        this.userProfileService = userProfileService;
        this.watchHistoryService = watchHistoryService;
        this.searchHistoryService = searchHistoryService;
        this.personalizationFacade = personalizationFacade;
        this.videoService = videoService;
    }

    @GetMapping
    public String viewProfile(Model model){
        User current = userService.getCurrentUser().orElseThrow();
        UserProfile profile = userProfileService.getOrCreateProfile(current);
        model.addAttribute("profile", profile);
        model.addAttribute("recommendations", personalizationFacade.recommend(current, 6));
        return "profile/view";
    }

    @PostMapping("/display-name")
    public String updateDisplayName(@RequestParam String displayName){
        User current = userService.getCurrentUser().orElseThrow();
        userProfileService.updateDisplayName(current, displayName);
        return "redirect:/profile?updated";
    }

    @PostMapping("/preferences")
    public String updatePreferences(@RequestParam String preferencesJson){
        User current = userService.getCurrentUser().orElseThrow();
        userProfileService.updatePreferences(current, preferencesJson);
        return "redirect:/profile/preferences?updated";
    }

    @GetMapping("/preferences")
    public String preferences(Model model){
        User current = userService.getCurrentUser().orElseThrow();
        model.addAttribute("profile", userProfileService.getOrCreateProfile(current));
        return "profile/preferences";
    }

    @GetMapping("/watch-history")
    public String watchHistory(Model model){
        User current = userService.getCurrentUser().orElseThrow();
        model.addAttribute("items", watchHistoryService.list(current));
        return "profile/watch-history";
    }

    @PostMapping("/watch-history/clear")
    public String clearWatchHistory(){
        User current = userService.getCurrentUser().orElseThrow();
        watchHistoryService.clear(current);
        return "redirect:/profile/watch-history?cleared";
    }

    @GetMapping("/search-history")
    public String searchHistory(Model model){
        User current = userService.getCurrentUser().orElseThrow();
        model.addAttribute("items", searchHistoryService.list(current));
        return "profile/search-history";
    }

    @PostMapping("/search-history/clear")
    public String clearSearchHistory(){
        User current = userService.getCurrentUser().orElseThrow();
        searchHistoryService.clear(current);
        return "redirect:/profile/search-history?cleared";
    }

    @GetMapping("/my-uploads")
    public String myUploads(Model model){
        User current = userService.getCurrentUser().orElseThrow();
        model.addAttribute("videos", videoService.findByUploader(current.getUsername()));
        return "profile/my-uploads";
    }
}


