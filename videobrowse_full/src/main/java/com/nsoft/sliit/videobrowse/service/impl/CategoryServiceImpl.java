package com.nsoft.sliit.videobrowse.service.impl;

import com.nsoft.sliit.videobrowse.entity.*;
import com.nsoft.sliit.videobrowse.repository.CategoryRepository;
import com.nsoft.sliit.videobrowse.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    public CategoryServiceImpl(CategoryRepository categoryRepository){ this.categoryRepository = categoryRepository; }

    @Override
    public Category createMain(String name, String description) {
        MainCategory c = new MainCategory();
        c.setName(name);
        c.setDescription(description);
        return categoryRepository.save(c);
    }

    @Override
    public SubCategory createSub(String name, String description, Long parentId) {
        MainCategory parent = (MainCategory) categoryRepository.findById(parentId).orElseThrow();
        SubCategory c = new SubCategory();
        c.setName(name);
        c.setDescription(description);
        c.setParent(parent);
        return categoryRepository.save(c);
    }

    @Override
    public List<Category> list() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> find(Long id) { return categoryRepository.findById(id); }

    @Override
    public Category update(Long id, String name, String description) {
        Category c = categoryRepository.findById(id).orElseThrow();
        c.setName(name);
        c.setDescription(description);
        return categoryRepository.save(c);
    }

    @Override
    public void merge(Long fromId, Long intoId) {
        if (fromId.equals(intoId)) return;
        Category from = categoryRepository.findById(fromId).orElseThrow();
        categoryRepository.findById(intoId).orElseThrow();
        // Simple merge: just delete 'from'. In a richer model, we'd reassign videos.
        categoryRepository.delete(from);
    }

    @Override
    public void delete(Long id) { categoryRepository.deleteById(id); }
}


