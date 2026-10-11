package com.nsoft.sliit.videobrowse.service.impl;

import com.nsoft.sliit.videobrowse.entity.Tag;
import com.nsoft.sliit.videobrowse.repository.TagRepository;
import com.nsoft.sliit.videobrowse.service.TagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    public TagServiceImpl(TagRepository tagRepository){ this.tagRepository = tagRepository; }

    @Override
    public Tag create(String name) { return tagRepository.save(new Tag(name)); }

    @Override
    public List<Tag> list() { return tagRepository.findAll(); }

    @Override
    public Optional<Tag> find(Long id) { return tagRepository.findById(id); }

    @Override
    public Tag rename(Long id, String name) {
        Tag t = tagRepository.findById(id).orElseThrow();
        t.setName(name);
        return tagRepository.save(t);
    }

    @Override
    public void delete(Long id) { tagRepository.deleteById(id); }
}


