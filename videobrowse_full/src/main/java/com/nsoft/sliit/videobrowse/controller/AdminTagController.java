package com.nsoft.sliit.videobrowse.controller;

import com.nsoft.sliit.videobrowse.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/tags")
public class AdminTagController {
    private final TagService tagService;
    public AdminTagController(TagService tagService){ this.tagService = tagService; }

    @GetMapping
    public String list(Model model){
        model.addAttribute("items", tagService.list());
        return "admin/tags";
    }

    @PostMapping
    public String create(@RequestParam String name){ tagService.create(name); return "redirect:/admin/tags"; }

    @PostMapping("/{id}/rename")
    public String rename(@PathVariable Long id, @RequestParam String name){ tagService.rename(id, name); return "redirect:/admin/tags"; }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id){ tagService.delete(id); return "redirect:/admin/tags"; }
}


