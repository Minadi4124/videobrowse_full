package com.nsoft.sliit.videobrowse.repository;

import com.nsoft.sliit.videobrowse.entity.User;
import com.nsoft.sliit.videobrowse.entity.Video;
import com.nsoft.sliit.videobrowse.entity.WatchHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Long> {
    List<WatchHistory> findByUserOrderByWatchedAtDesc(User user, Pageable pageable);
    List<WatchHistory> findByUserAndVideoOrderByWatchedAtDesc(User user, Video video, Pageable pageable);
    List<WatchHistory> findByVideoOrderByWatchedAtDesc(Video video, Pageable pageable);
    List<WatchHistory> findByUserOrderByWatchedAtDesc(User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM WatchHistory w WHERE w.user = :user")
    void deleteByUser(@Param("user") User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM WatchHistory w WHERE w.video = :video")
    void deleteByVideo(@Param("video") Video video);
}


