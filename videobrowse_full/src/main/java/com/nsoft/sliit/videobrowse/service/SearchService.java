package com.nsoft.sliit.videobrowse.service;

import com.nsoft.sliit.videobrowse.entity.Video;

import java.util.List;

public interface SearchService {
    List<Video> search(String query, String category, String sort);
}


