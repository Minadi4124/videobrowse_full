package com.nsoft.sliit.videobrowse.repository;

import com.nsoft.sliit.videobrowse.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
}


