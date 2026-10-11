package com.nsoft.sliit.videobrowse.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nsoft.sliit.videobrowse.entity.Comment;
import com.nsoft.sliit.videobrowse.repository.CommentRepository;
import com.nsoft.sliit.videobrowse.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    
    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }
    
    @Override
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }
    
    @Override
    public List<Comment> findByVideoId(Long videoId) {
        return commentRepository.findByVideoIdOrderByCreatedAtDesc(videoId);
    }
    
    @Override
    public Comment update(Long id, String content) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setContent(content);
        comment.setUpdatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }
    
    @Override
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }
    
    @Override
    public void deleteByVideoId(Long videoId) {
        commentRepository.deleteByVideoId(videoId);
    }
}
