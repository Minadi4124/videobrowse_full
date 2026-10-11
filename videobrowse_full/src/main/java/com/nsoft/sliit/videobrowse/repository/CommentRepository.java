package com.nsoft.sliit.videobrowse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nsoft.sliit.videobrowse.entity.Comment;
import com.nsoft.sliit.videobrowse.entity.Video;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByVideoOrderByCreatedAtDesc(Video video);
    List<Comment> findByVideoIdOrderByCreatedAtDesc(Long videoId);
    void deleteByVideoId(Long videoId);
}
