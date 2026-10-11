package com.nsoft.sliit.videobrowse.service;

import com.nsoft.sliit.videobrowse.entity.User;
import com.nsoft.sliit.videobrowse.entity.Video;
import com.nsoft.sliit.videobrowse.entity.WatchHistory;

import java.util.List;

public interface WatchHistoryService {
    WatchHistory logWatch(User user, Video video, Long watchedSeconds);
    List<WatchHistory> list(User user);
    void clear(User user);
}


