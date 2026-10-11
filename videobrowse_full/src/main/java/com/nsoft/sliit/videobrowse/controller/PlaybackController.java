package com.nsoft.sliit.videobrowse.controller;

import com.nsoft.sliit.videobrowse.entity.PlaybackSession;
import com.nsoft.sliit.videobrowse.repository.PlaybackSessionRepository;
import com.nsoft.sliit.videobrowse.entity.Video;
import com.nsoft.sliit.videobrowse.repository.VideoRepository;
import com.nsoft.sliit.videobrowse.service.UserService;
import com.nsoft.sliit.videobrowse.service.WatchHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/playback")
public class PlaybackController {
    private final PlaybackSessionRepository repo;
    private final VideoRepository videoRepository;
    private final UserService userService;
    private final WatchHistoryService watchHistoryService;
    public PlaybackController(PlaybackSessionRepository repo, VideoRepository videoRepository, UserService userService, WatchHistoryService watchHistoryService){ this.repo = repo; this.videoRepository = videoRepository; this.userService = userService; this.watchHistoryService = watchHistoryService; }

    @PostMapping("/start/{videoId}")
    public ResponseEntity<PlaybackSession> start(@PathVariable Long videoId, Authentication auth){
        PlaybackSession s = new PlaybackSession();
        s.setVideoId(videoId);
        s.setUsername(auth != null ? auth.getName() : "guest");
        return ResponseEntity.ok(repo.save(s));
    }

    @PostMapping("/{id}/progress")
    public ResponseEntity<PlaybackSession> progress(@PathVariable Long id, @RequestParam Long seconds){
        PlaybackSession s = repo.findById(id).orElseThrow();
        s.setWatchedSeconds(seconds);
        PlaybackSession saved = repo.save(s);
        userService.getCurrentUser().ifPresent(u -> {
            Video v = videoRepository.findById(s.getVideoId()).orElse(null);
            if (v != null) {
                watchHistoryService.logWatch(u, v, seconds);
            }
        });
        return ResponseEntity.ok(saved);
    }
}
