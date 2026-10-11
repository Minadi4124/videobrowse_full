package com.nsoft.sliit.videobrowse.service.impl;

import com.nsoft.sliit.videobrowse.entity.Video;
import com.nsoft.sliit.videobrowse.entity.EducationalVideo;
import com.nsoft.sliit.videobrowse.entity.EntertainmentVideo;
import com.nsoft.sliit.videobrowse.repository.VideoRepository;
import com.nsoft.sliit.videobrowse.repository.WatchHistoryRepository;
import com.nsoft.sliit.videobrowse.repository.PlaybackSessionRepository;
import com.nsoft.sliit.videobrowse.repository.SubscriptionRepository;
import com.nsoft.sliit.videobrowse.service.VideoService;
import com.nsoft.sliit.videobrowse.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VideoServiceImpl implements VideoService {
    private final VideoRepository videoRepository;
    private final WatchHistoryRepository watchHistoryRepository;
    private final PlaybackSessionRepository playbackSessionRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final NotificationService notificationService;
    private final Path storage = Paths.get("data/videos");
    private final Path thumbStorage = Paths.get("data/thumbs");

    public VideoServiceImpl(VideoRepository videoRepository, WatchHistoryRepository watchHistoryRepository,
                          PlaybackSessionRepository playbackSessionRepository, SubscriptionRepository subscriptionRepository, NotificationService notificationService) {
        this.videoRepository = videoRepository;
        this.watchHistoryRepository = watchHistoryRepository;
        this.playbackSessionRepository = playbackSessionRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.notificationService = notificationService;
        try { 
            Files.createDirectories(storage); 
            Files.createDirectories(thumbStorage);
        } catch (Exception ignored) {}
    }

    @Override
    public Video upload(MultipartFile file, MultipartFile thumbnail, String title, String description, String category, String uploader, String tags, Video.Visibility visibility, String type) throws Exception {
        String original = file.getOriginalFilename();
        String stored = System.currentTimeMillis() + "-" + (original == null ? "upload.bin" : original.replaceAll("\\s+","_"));
        Files.copy(file.getInputStream(), storage.resolve(stored), StandardCopyOption.REPLACE_EXISTING);

        String thumbName = null;
        if (thumbnail != null && !thumbnail.isEmpty()){
            String torig = thumbnail.getOriginalFilename();
            thumbName = System.currentTimeMillis() + "-thumb-" + (torig == null ? "thumb.png" : torig.replaceAll("\\s+","_"));
            Files.copy(thumbnail.getInputStream(), thumbStorage.resolve(thumbName), StandardCopyOption.REPLACE_EXISTING);
        }

        Video v;
        if ("EDU".equalsIgnoreCase(type)) {
            EducationalVideo ev = new EducationalVideo();
            v = ev;
        } else if ("ENT".equalsIgnoreCase(type)) {
            EntertainmentVideo ev = new EntertainmentVideo();
            v = ev;
        } else {
            v = new Video();
        }
        v.setTitle(title);
        v.setDescription(description);
        v.setFilename(stored);
        v.setCategory(category);
        v.setUploader(uploader);
        v.setUploadedAt(LocalDateTime.now());
        v.setTags(tags);
        if (thumbName != null) v.setThumbnail(thumbName);
        if (visibility != null) v.setVisibility(visibility);
        Video saved = videoRepository.save(v);
        // Notify subscribers of uploader
        if (uploader != null) {
            subscriptionRepository.findByCreatorName(uploader).forEach(s -> {
                notificationService.notifyUser(s.getSubscriber(), "New video from " + uploader + ": " + title);
            });
        }
        return saved;
    }

    @Override public List<Video> search(String q){ return videoRepository.findByTitleContainingIgnoreCase(q); }
    @Override public Optional<Video> findById(Long id){ return videoRepository.findById(id); }
    @Override public List<Video> findByCategory(String category){ return videoRepository.findByCategoryIgnoreCase(category); }

    @Override
    public List<Video> findByUploader(String uploader) {
        return videoRepository.findByUploaderIgnoreCase(uploader);
    }

    @Override
    public Video update(Long id, String title, String description, String category, String tags, Video.Visibility visibility, MultipartFile thumbnail) throws Exception {
        Video v = videoRepository.findById(id).orElseThrow();
        v.setTitle(title);
        v.setDescription(description);
        v.setCategory(category);
        v.setTags(tags);
        if (visibility != null) v.setVisibility(visibility);
        
        // Handle thumbnail update
        if (thumbnail != null && !thumbnail.isEmpty()) {
            // Delete old thumbnail if exists
            if (v.getThumbnail() != null) {
                Path oldThumbPath = thumbStorage.resolve(v.getThumbnail());
                if (Files.exists(oldThumbPath)) {
                    Files.delete(oldThumbPath);
                }
            }
            
            // Save new thumbnail
            String torig = thumbnail.getOriginalFilename();
            String thumbName = System.currentTimeMillis() + "-thumb-" + (torig == null ? "thumb.png" : torig.replaceAll("\\s+","_"));
            Files.copy(thumbnail.getInputStream(), thumbStorage.resolve(thumbName), StandardCopyOption.REPLACE_EXISTING);
            v.setThumbnail(thumbName);
        }
        
        return videoRepository.save(v);
    }

    @Override
    @Transactional
    public void delete(Long id) throws Exception {
        Video video = videoRepository.findById(id).orElseThrow();
        
        try {
            System.out.println("Starting deletion of video: " + video.getTitle() + " (ID: " + id + ")");
            
            // Clear the many-to-many relationship with tags first
            if (video.getTagRefs() != null && !video.getTagRefs().isEmpty()) {
                System.out.println("Clearing " + video.getTagRefs().size() + " tag associations");
                video.getTagRefs().clear();
                videoRepository.save(video); // Save to persist the cleared tags
            }
            
            // Clear category reference if exists
            if (video.getCategoryRef() != null) {
                System.out.println("Clearing category reference");
                video.setCategoryRef(null);
                videoRepository.save(video);
            }
            
            // Delete related records first - WatchHistory
            try {
                watchHistoryRepository.deleteByVideo(video);
                System.out.println("Deleted watch history records");
            } catch (Exception e) {
                System.err.println("Warning: Could not delete watch history: " + e.getMessage());
            }
            
            // Delete related records - PlaybackSession
            try {
                playbackSessionRepository.deleteByVideoId(video.getId());
                System.out.println("Deleted playback session records");
            } catch (Exception e) {
                System.err.println("Warning: Could not delete playback sessions: " + e.getMessage());
            }
            
            // Delete video file
            if (video.getFilename() != null) {
                Path videoPath = Paths.get("data/videos/" + video.getFilename());
                if (Files.exists(videoPath)) {
                    Files.delete(videoPath);
                    System.out.println("Deleted video file: " + videoPath);
                } else {
                    System.out.println("Video file not found: " + videoPath);
                }
            }
            
            // Delete thumbnail file
            if (video.getThumbnail() != null) {
                Path thumbPath = Paths.get("data/thumbs/" + video.getThumbnail());
                if (Files.exists(thumbPath)) {
                    Files.delete(thumbPath);
                    System.out.println("Deleted thumbnail file: " + thumbPath);
                } else {
                    System.out.println("Thumbnail file not found: " + thumbPath);
                }
            }
            
            // Finally delete the video entity
            videoRepository.delete(video);
            System.out.println("Successfully deleted video entity with ID: " + id);
            
        } catch (Exception e) {
            System.err.println("Error deleting video: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to delete video: " + e.getMessage(), e);
        }
    }
}
