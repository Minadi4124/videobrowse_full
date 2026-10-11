package com.nsoft.sliit.videobrowse.service.impl;

import com.nsoft.sliit.videobrowse.entity.User;
import com.nsoft.sliit.videobrowse.entity.Video;
import com.nsoft.sliit.videobrowse.entity.WatchHistory;
import com.nsoft.sliit.videobrowse.repository.WatchHistoryRepository;
import com.nsoft.sliit.videobrowse.service.WatchHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WatchHistoryServiceImpl implements WatchHistoryService {
    private final WatchHistoryRepository watchHistoryRepository;
    public WatchHistoryServiceImpl(WatchHistoryRepository watchHistoryRepository){
        this.watchHistoryRepository = watchHistoryRepository;
    }

    @Override
    public WatchHistory logWatch(User user, Video video, Long watchedSeconds) {
        WatchHistory h = new WatchHistory();
        h.setUser(user);
        h.setVideo(video);
        h.setWatchedSeconds(watchedSeconds != null ? watchedSeconds : 0L);
        return watchHistoryRepository.save(h);
    }

    @Override
    public List<WatchHistory> list(User user) {
        return watchHistoryRepository.findByUserOrderByWatchedAtDesc(user);
    }

    @Override
    public void clear(User user) {
        watchHistoryRepository.deleteAll(watchHistoryRepository.findByUserOrderByWatchedAtDesc(user));
    }
}


