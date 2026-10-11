package com.nsoft.sliit.videobrowse.service;

import java.util.List;
import java.util.Optional;

import com.nsoft.sliit.videobrowse.entity.Comment;

public interface CommentService {
    Comment save(Comment comment);
    Optional<Comment> findById(Long id);
    List<Comment> findByVideoId(Long videoId);
    Comment update(Long id, String content);
    void delete(Long id);
    void deleteByVideoId(Long videoId);
}
