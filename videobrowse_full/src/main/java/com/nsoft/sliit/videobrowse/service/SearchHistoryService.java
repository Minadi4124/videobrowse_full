package com.nsoft.sliit.videobrowse.service;

import com.nsoft.sliit.videobrowse.entity.SearchHistory;
import com.nsoft.sliit.videobrowse.entity.User;

import java.util.List;

public interface SearchHistoryService {
    SearchHistory log(User user, String query);
    List<SearchHistory> list(User user);
    void clear(User user);
}


