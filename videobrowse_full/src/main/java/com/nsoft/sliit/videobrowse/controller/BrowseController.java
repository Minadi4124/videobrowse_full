package com.nsoft.sliit.videobrowse.controller;

import com.nsoft.sliit.videobrowse.entity.SavedSearch;
import com.nsoft.sliit.videobrowse.entity.User;
import com.nsoft.sliit.videobrowse.repository.SavedSearchRepository;
import com.nsoft.sliit.videobrowse.repository.VideoRepository;
import com.nsoft.sliit.videobrowse.service.SearchHistoryService;
import com.nsoft.sliit.videobrowse.service.SearchService;
import com.nsoft.sliit.videobrowse.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/browse")
public class BrowseController {
    private final SearchService searchService;
    private final UserService userService;
    private final SearchHistoryService searchHistoryService;
    private final SavedSearchRepository savedSearchRepository;
    private final VideoRepository videoRepository;

    public BrowseController(SearchService searchService, UserService userService, SearchHistoryService searchHistoryService, SavedSearchRepository savedSearchRepository, VideoRepository videoRepository) {
        this.searchService = searchService;
        this.userService = userService;
        this.searchHistoryService = searchHistoryService;
        this.savedSearchRepository = savedSearchRepository;
        this.videoRepository = videoRepository;
    }

    @GetMapping
    public String dashboard(Model model){
        User user = userService.getCurrentUser().orElse(null);
        if (user != null) model.addAttribute("saved", savedSearchRepository.findByUserOrderByCreatedAtDesc(user));
        
        // Load all videos for display
        model.addAttribute("videos", videoRepository.findAll());
        return "browse/dashboard";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required=false) String q,
                         @RequestParam(required=false) String category,
                         @RequestParam(required=false, defaultValue="uploadedAtDesc") String sort,
                         Model model){
        model.addAttribute("videos", searchService.search(q, category, sort));
        model.addAttribute("q", q);
        model.addAttribute("category", category);
        model.addAttribute("sort", sort);
        userService.getCurrentUser().ifPresent(u -> { if (q != null && !q.isBlank()) searchHistoryService.log(u, q); });
        return "browse/search";
    }

    @PostMapping("/save")
    public String save(@RequestParam(required=false) String q,
                       @RequestParam(required=false) String category,
                       @RequestParam(required=false) String sort){
        User user = userService.getCurrentUser().orElseThrow();
        SavedSearch s = new SavedSearch();
        s.setUser(user);
        s.setQuery(q);
        s.setCategory(category);
        s.setSort(sort);
        savedSearchRepository.save(s);
        return "redirect:/browse";
    }

    @PostMapping("/saved/{id}/delete")
    public String deleteSaved(@PathVariable Long id){
        savedSearchRepository.deleteById(id);
        return "redirect:/browse";
    }
}


