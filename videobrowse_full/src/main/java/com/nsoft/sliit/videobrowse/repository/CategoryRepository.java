package com.nsoft.sliit.videobrowse.repository;

import com.nsoft.sliit.videobrowse.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}


