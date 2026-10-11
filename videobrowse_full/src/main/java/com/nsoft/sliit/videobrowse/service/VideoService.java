package com.nsoft.sliit.videobrowse.service;

import com.nsoft.sliit.videobrowse.entity.Video;
import java.util.List;
import java.util.Optional;

public interface VideoService {
    Video upload(org.springframework.web.multipart.MultipartFile file, org.springframework.web.multipart.MultipartFile thumbnail,
                 String title, String description, String category, String uploader, String tags,
                 com.nsoft.sliit.videobrowse.entity.Video.Visibility visibility, String type) throws Exception;
    List<Video> search(String q);
    Optional<Video> findById(Long id);
    List<Video> findByCategory(String category);
    List<Video> findByUploader(String uploader);
    Video update(Long id, String title, String description, String category, String tags, com.nsoft.sliit.videobrowse.entity.Video.Visibility visibility, org.springframework.web.multipart.MultipartFile thumbnail) throws Exception;
    void delete(Long id) throws Exception;
}
