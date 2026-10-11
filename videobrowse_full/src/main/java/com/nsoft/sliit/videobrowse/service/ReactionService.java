package com.nsoft.sliit.videobrowse.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.nsoft.sliit.videobrowse.entity.Reaction;

public interface ReactionService {
    Reaction save(Reaction reaction);
    Optional<Reaction> findById(Long id);
    List<Reaction> findByVideoId(Long videoId);
    Optional<Reaction> findByVideoIdAndAuthor(Long videoId, String author);
    Reaction addOrUpdateReaction(Long videoId, String type, String author);
    void delete(Long id);
    void deleteByVideoId(Long videoId);
    void deleteByVideoIdAndAuthor(Long videoId, String author);
    Map<String, Long> getReactionCounts(Long videoId);
}
