package com.nsoft.sliit.videobrowse.service;

import com.nsoft.sliit.videobrowse.entity.User;
import com.nsoft.sliit.videobrowse.entity.Video;

import java.util.List;

public interface PersonalizationService {
    boolean supports(User user);
    List<Video> recommend(User user, int limit);
}


