package com.nsoft.sliit.videobrowse.controller;

import com.nsoft.sliit.videobrowse.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private final CategoryService categoryService;
    public AdminCategoryController(CategoryService categoryService){ this.categoryService = categoryService; }

    @GetMapping
    public String list(Model model){
        model.addAttribute("items", categoryService.list());
        return "admin/categories";
    }

    @PostMapping("/main")
    public String createMain(@RequestParam String name, @RequestParam(required=false) String description){
        categoryService.createMain(name, description);
        return "redirect:/admin/categories";
    }

    @PostMapping("/sub")
    public String createSub(@RequestParam String name, @RequestParam(required=false) String description, @RequestParam Long parentId){
        categoryService.createSub(name, description, parentId);
        return "redirect:/admin/categories";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, @RequestParam String name, @RequestParam(required=false) String description){
        categoryService.update(id, name, description);
        return "redirect:/admin/categories";
    }

    @PostMapping("/merge")
    public String merge(@RequestParam Long fromId, @RequestParam Long intoId){
        categoryService.merge(fromId, intoId);
        return "redirect:/admin/categories";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id){
        categoryService.delete(id);
        return "redirect:/admin/categories";
    }
}


