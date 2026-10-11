package com.nsoft.sliit.videobrowse.service.impl;

import com.nsoft.sliit.videobrowse.entity.User;
import com.nsoft.sliit.videobrowse.entity.Video;
import com.nsoft.sliit.videobrowse.repository.VideoRepository;
import com.nsoft.sliit.videobrowse.service.PersonalizationService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Order(2)
public class RegularUserPersonalization implements PersonalizationService {
    private final VideoRepository videoRepository;
    public RegularUserPersonalization(VideoRepository videoRepository){ this.videoRepository = videoRepository; }

    @Override
    public boolean supports(User user) { return true; }

    @Override
    public List<Video> recommend(User user, int limit) {
        return videoRepository.findAll().stream().limit(limit).toList();
    }
}


