package com.nsoft.sliit.videobrowse.controller;

import com.nsoft.sliit.videobrowse.entity.Category;
import com.nsoft.sliit.videobrowse.entity.Tag;
import com.nsoft.sliit.videobrowse.entity.Video;
import com.nsoft.sliit.videobrowse.repository.CategoryRepository;
import com.nsoft.sliit.videobrowse.repository.TagRepository;
import com.nsoft.sliit.videobrowse.repository.VideoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin/videos")
public class AdminVideoController {
    private final VideoRepository videoRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public AdminVideoController(VideoRepository videoRepository, CategoryRepository categoryRepository, TagRepository tagRepository) {
        this.videoRepository = videoRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    @GetMapping("/{id}/classify")
    public String classifyForm(@PathVariable Long id, Model model){
        Video v = videoRepository.findById(id).orElseThrow();
        model.addAttribute("video", v);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("tags", tagRepository.findAll());
        return "admin/video-classify";
    }

    @PostMapping("/{id}/classify")
    public String classify(@PathVariable Long id,
                           @RequestParam(required=false) Long categoryId,
                           @RequestParam(required=false, name="tagIds") Set<Long> tagIds){
        Video v = videoRepository.findById(id).orElseThrow();
        if (categoryId != null) {
            Category c = categoryRepository.findById(categoryId).orElse(null);
            v.setCategoryRef(c);
        } else {
            v.setCategoryRef(null);
        }
        Set<Tag> newTags = new HashSet<>();
        if (tagIds != null) {
            for (Long tid : tagIds) tagRepository.findById(tid).ifPresent(newTags::add);
        }
        v.setTagRefs(newTags);
        videoRepository.save(v);
        return "redirect:/admin/dashboard";
    }
}


