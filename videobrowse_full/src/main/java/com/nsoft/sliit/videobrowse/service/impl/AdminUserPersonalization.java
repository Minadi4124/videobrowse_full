package com.nsoft.sliit.videobrowse.service.impl;

import com.nsoft.sliit.videobrowse.entity.User;
import com.nsoft.sliit.videobrowse.entity.Video;
import com.nsoft.sliit.videobrowse.repository.VideoRepository;
import com.nsoft.sliit.videobrowse.service.PersonalizationService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Order(1)
public class AdminUserPersonalization implements PersonalizationService {
    private final VideoRepository videoRepository;
    public AdminUserPersonalization(VideoRepository videoRepository){ this.videoRepository = videoRepository; }

    @Override
    public boolean supports(User user) {
        return user.getRoles().stream().anyMatch(r -> "ROLE_ADMIN".equals(r.getName()));
    }

    @Override
    public List<Video> recommend(User user, int limit) {
        return videoRepository.findAll().stream().sorted((a,b) -> {
            String ca = a.getCategory() == null ? "" : a.getCategory();
            String cb = b.getCategory() == null ? "" : b.getCategory();
            return ca.compareToIgnoreCase(cb);
        }).limit(limit).toList();
    }
}


