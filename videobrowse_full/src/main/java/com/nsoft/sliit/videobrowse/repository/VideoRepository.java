package com.nsoft.sliit.videobrowse.repository;

import com.nsoft.sliit.videobrowse.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByTitleContainingIgnoreCase(String q);
    List<Video> findByCategoryIgnoreCase(String category);
    List<Video> findByUploaderIgnoreCase(String uploader);
}
