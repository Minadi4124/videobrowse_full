package com.nsoft.sliit.videobrowse.service.impl;

import com.nsoft.sliit.videobrowse.entity.Video;
import com.nsoft.sliit.videobrowse.repository.VideoRepository;
import com.nsoft.sliit.videobrowse.service.SearchService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
    private final VideoRepository videoRepository;
    public SearchServiceImpl(VideoRepository videoRepository){ this.videoRepository = videoRepository; }

    @Override
    public List<Video> search(String query, String category, String sort) {
        List<Video> base;
        if (query != null && !query.isBlank()) base = videoRepository.findByTitleContainingIgnoreCase(query);
        else if (category != null && !category.isBlank()) base = videoRepository.findByCategoryIgnoreCase(category);
        else base = videoRepository.findAll();

        Comparator<Video> cmp = Comparator.comparing(Video::getUploadedAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed();
        if ("titleAsc".equalsIgnoreCase(sort)) cmp = Comparator.comparing(v -> v.getTitle() == null ? "" : v.getTitle(), String.CASE_INSENSITIVE_ORDER);
        if ("titleDesc".equalsIgnoreCase(sort)) cmp = Comparator.comparing((Video v) -> v.getTitle() == null ? "" : v.getTitle(), String.CASE_INSENSITIVE_ORDER).reversed();
        if ("uploadedAtAsc".equalsIgnoreCase(sort)) cmp = Comparator.comparing(Video::getUploadedAt, Comparator.nullsLast(Comparator.naturalOrder()));

        return base.stream().sorted(cmp).collect(Collectors.toList());
    }
}


