package com.nsoft.sliit.videobrowse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nsoft.sliit.videobrowse.entity.Reaction;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    List<Reaction> findByVideoIdOrderByCreatedAtDesc(Long videoId);
    Optional<Reaction> findByVideoIdAndAuthor(Long videoId, String author);
    void deleteByVideoId(Long videoId);
    
    @Modifying
    @Query("DELETE FROM Reaction r WHERE r.video.id = :videoId AND r.author = :author")
    void deleteByVideoIdAndAuthor(@Param("videoId") Long videoId, @Param("author") String author);
    
    @Query("SELECT r.type, COUNT(r) FROM Reaction r WHERE r.video.id = :videoId GROUP BY r.type")
    List<Object[]> getReactionCountsByVideoId(@Param("videoId") Long videoId);
}
