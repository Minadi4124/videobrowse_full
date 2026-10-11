package com.nsoft.sliit.videobrowse.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="playback_sessions")
public class PlaybackSession {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long videoId;
    private String username;
    private LocalDateTime startedAt = LocalDateTime.now();
    private Long watchedSeconds = 0L;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getVideoId() { return videoId; }
    public void setVideoId(Long videoId) { this.videoId = videoId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    public Long getWatchedSeconds() { return watchedSeconds; }
    public void setWatchedSeconds(Long watchedSeconds) { this.watchedSeconds = watchedSeconds; }
}
