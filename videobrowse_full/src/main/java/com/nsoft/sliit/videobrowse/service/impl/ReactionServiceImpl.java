package com.nsoft.sliit.videobrowse.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nsoft.sliit.videobrowse.entity.Reaction;
import com.nsoft.sliit.videobrowse.entity.Video;
import com.nsoft.sliit.videobrowse.repository.ReactionRepository;
import com.nsoft.sliit.videobrowse.service.ReactionService;
import com.nsoft.sliit.videobrowse.service.VideoService;

@Service
public class ReactionServiceImpl implements ReactionService {
    private final ReactionRepository reactionRepository;
    private final VideoService videoService;
    
    public ReactionServiceImpl(ReactionRepository reactionRepository, VideoService videoService) {
        this.reactionRepository = reactionRepository;
        this.videoService = videoService;
    }
    
    @Override
    public Reaction save(Reaction reaction) {
        return reactionRepository.save(reaction);
    }
    
    @Override
    public Optional<Reaction> findById(Long id) {
        return reactionRepository.findById(id);
    }
    
    @Override
    public List<Reaction> findByVideoId(Long videoId) {
        return reactionRepository.findByVideoIdOrderByCreatedAtDesc(videoId);
    }
    
    @Override
    public Optional<Reaction> findByVideoIdAndAuthor(Long videoId, String author) {
        return reactionRepository.findByVideoIdAndAuthor(videoId, author);
    }
    
    @Override
    public Reaction addOrUpdateReaction(Long videoId, String type, String author) {
        Video video = videoService.findById(videoId)
            .orElseThrow(() -> new RuntimeException("Video not found"));
        
        Optional<Reaction> existingReaction = reactionRepository.findByVideoIdAndAuthor(videoId, author);
        
        if (existingReaction.isPresent()) {
            Reaction reaction = existingReaction.get();
            reaction.setType(type);
            return reactionRepository.save(reaction);
        } else {
            Reaction reaction = new Reaction(type, author, video);
            return reactionRepository.save(reaction);
        }
    }
    
    @Override
    public void delete(Long id) {
        reactionRepository.deleteById(id);
    }
    
    @Override
    public void deleteByVideoId(Long videoId) {
        reactionRepository.deleteByVideoId(videoId);
    }
    
    @Override
    @Transactional
    public void deleteByVideoIdAndAuthor(Long videoId, String author) {
        reactionRepository.deleteByVideoIdAndAuthor(videoId, author);
    }
    
    @Override
    public Map<String, Long> getReactionCounts(Long videoId) {
        List<Object[]> results = reactionRepository.getReactionCountsByVideoId(videoId);
        Map<String, Long> counts = new HashMap<>();
        
        // Initialize all reaction types with 0
        counts.put("HEART", 0L);
        counts.put("THUMBS_UP", 0L);
        counts.put("HAPPY", 0L);
        counts.put("SAD", 0L);
        
        // Update with actual counts
        for (Object[] result : results) {
            String type = (String) result[0];
            Long count = (Long) result[1];
            counts.put(type, count);
        }
        
        return counts;
    }
}
