package com.nsoft.sliit.videobrowse.service;

import com.nsoft.sliit.videobrowse.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {
    Tag create(String name);
    List<Tag> list();
    Optional<Tag> find(Long id);
    Tag rename(Long id, String name);
    void delete(Long id);
}


