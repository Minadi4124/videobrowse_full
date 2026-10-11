package com.nsoft.sliit.videobrowse.service;

import com.nsoft.sliit.videobrowse.entity.Category;
import com.nsoft.sliit.videobrowse.entity.SubCategory;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Category createMain(String name, String description);
    SubCategory createSub(String name, String description, Long parentId);
    List<Category> list();
    Optional<Category> find(Long id);
    Category update(Long id, String name, String description);
    void merge(Long fromId, Long intoId);
    void delete(Long id);
}


