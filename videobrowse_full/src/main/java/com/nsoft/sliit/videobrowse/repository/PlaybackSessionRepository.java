package com.nsoft.sliit.videobrowse.repository;

import com.nsoft.sliit.videobrowse.entity.PlaybackSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PlaybackSessionRepository extends JpaRepository<PlaybackSession, Long> {
    List<PlaybackSession> findByUsernameOrderByStartedAtDesc(String username);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM PlaybackSession p WHERE p.videoId = :videoId")
    void deleteByVideoId(@Param("videoId") Long videoId);
}
