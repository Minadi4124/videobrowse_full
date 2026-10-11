package com.nsoft.sliit.videobrowse.controller;

import com.nsoft.sliit.videobrowse.repository.VideoRepository;
import com.nsoft.sliit.videobrowse.service.SearchHistoryService;
import com.nsoft.sliit.videobrowse.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    private final VideoRepository repo;
    private final UserService userService;
    private final SearchHistoryService searchHistoryService;
    public HomeController(VideoRepository repo, UserService userService, SearchHistoryService searchHistoryService){ this.repo = repo; this.userService = userService; this.searchHistoryService = searchHistoryService; }

    @GetMapping("/")
    public String index(@RequestParam(required=false) String q,
                        @RequestParam(required=false) String category,
                        Model m){
        if (q != null && !q.isBlank()) {
            m.addAttribute("videos", repo.findByTitleContainingIgnoreCase(q));
            m.addAttribute("q", q);
            userService.getCurrentUser().ifPresent(u -> searchHistoryService.log(u, q));
        } else if (category != null && !category.isBlank()){
            m.addAttribute("videos", repo.findByCategoryIgnoreCase(category));
            m.addAttribute("category", category);
        } else {
            m.addAttribute("videos", repo.findAll());
        }
        return "index";
    }
}
