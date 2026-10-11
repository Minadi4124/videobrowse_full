package com.nsoft.sliit.videobrowse.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.nsoft.sliit.videobrowse.entity.Comment;
import com.nsoft.sliit.videobrowse.entity.Video;
import com.nsoft.sliit.videobrowse.service.CommentService;
import com.nsoft.sliit.videobrowse.service.ReactionService;
import com.nsoft.sliit.videobrowse.service.VideoService;

@Controller
@RequestMapping("/videos")
public class VideoController {
    private final VideoService videoService;
    private final CommentService commentService;
    private final ReactionService reactionService;
    
    public VideoController(VideoService videoService, CommentService commentService, ReactionService reactionService){ 
        this.videoService = videoService;
        this.commentService = commentService;
        this.reactionService = reactionService;
    }

    @GetMapping("/upload")
    public String uploadForm(){ return "videos/upload"; }

    @PostMapping("/upload")
    public String doUpload(@RequestParam("file") MultipartFile file,
                           @RequestParam(value="thumbnail", required=false) MultipartFile thumbnail,
                           @RequestParam String title,
                           @RequestParam(required=false) String description,
                           @RequestParam(required=false) String category,
                           @RequestParam(required=false) String tags,
                           @RequestParam(required=false, defaultValue="PUBLIC") com.nsoft.sliit.videobrowse.entity.Video.Visibility visibility,
                           @RequestParam(required=false, defaultValue="BASE") String type,
                           Authentication auth,
                           Model model) throws Exception {
        if (title == null || title.trim().isEmpty()) {
            model.addAttribute("error", "Video title is required");
            model.addAttribute("description", description);
            model.addAttribute("category", category);
            model.addAttribute("tags", tags);
            model.addAttribute("visibility", visibility);
            model.addAttribute("type", type);
            return "videos/upload";
        }
        String uploader = auth != null ? auth.getName() : "guest";
        videoService.upload(file, thumbnail, title.trim(), description, category, uploader, tags, visibility, type);
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model m, Authentication auth){
        Video v = videoService.findById(id).orElseThrow();
        m.addAttribute("video", v);
        m.addAttribute("comments", commentService.findByVideoId(id));
        m.addAttribute("reactionCounts", reactionService.getReactionCounts(id));
        boolean isOwner = auth != null && v.getUploader() != null && v.getUploader().equals(auth.getName());
        boolean isAdmin = auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        m.addAttribute("canManage", isOwner || isAdmin);
        return "videos/view";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model m, Authentication auth){
        Video v = videoService.findById(id).orElseThrow();
        boolean allowed = auth != null && (v.getUploader() != null && v.getUploader().equals(auth.getName())
                || auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        if (!allowed) {
            return "redirect:/videos/" + id + "?error=forbidden";
        }
        m.addAttribute("video", v);
        return "videos/edit";
    }

    @PostMapping("/{id}/edit")
    public String doEdit(@PathVariable Long id,
                         @RequestParam String title,
                         @RequestParam String description,
                         @RequestParam String category,
                         @RequestParam(required=false) String tags,
                         @RequestParam(required=false, defaultValue="PUBLIC") com.nsoft.sliit.videobrowse.entity.Video.Visibility visibility,
                         @RequestParam(value="thumbnail", required=false) MultipartFile thumbnail,
                         Authentication auth) throws Exception {
        Video v = videoService.findById(id).orElseThrow();
        boolean allowed = auth != null && (v.getUploader() != null && v.getUploader().equals(auth.getName())
                || auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        if (!allowed) {
            return "redirect:/videos/" + id + "?error=forbidden";
        }
        videoService.update(id, title, description, category, tags, visibility, thumbnail);
        return "redirect:/videos/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Authentication auth) throws Exception {
        Video v = videoService.findById(id).orElseThrow();
        boolean allowed = auth != null && (v.getUploader() != null && v.getUploader().equals(auth.getName())
                || auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        if (!allowed) {
            return "redirect:/videos/" + id + "?error=forbidden";
        }
        try {
            videoService.delete(id);
            return "redirect:/";
        } catch (Exception e) {
            System.err.println("Error deleting video: " + e.getMessage());
            return "redirect:/?error=delete_failed";
        }
    }
    
    // Comment endpoints
    @PostMapping("/{id}/comments")
    public String addComment(@PathVariable Long id, 
                           @RequestParam String content,
                           Authentication auth) {
        if (content == null || content.trim().isEmpty()) {
            return "redirect:/videos/" + id + "?error=comment_empty";
        }
        
        Video video = videoService.findById(id).orElseThrow();
        String author = auth != null ? auth.getName() : "Anonymous";
        
        Comment comment = new Comment(content.trim(), author, video);
        commentService.save(comment);
        
        return "redirect:/videos/" + id;
    }
    
    @PostMapping("/{id}/comments/{commentId}/edit")
    public String editComment(@PathVariable Long id,
                            @PathVariable Long commentId,
                            @RequestParam String content) {
        if (content == null || content.trim().isEmpty()) {
            return "redirect:/videos/" + id + "?error=comment_empty";
        }
        
        commentService.update(commentId, content.trim());
        return "redirect:/videos/" + id;
    }
    
    @PostMapping("/{id}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long id,
                              @PathVariable Long commentId) {
        commentService.delete(commentId);
        return "redirect:/videos/" + id;
    }
    
    // Reaction endpoints
    @PostMapping("/{id}/reactions")
    public String addReaction(@PathVariable Long id,
                            @RequestParam String type,
                            Authentication auth) {
        String author = auth != null ? auth.getName() : "Anonymous";
        reactionService.addOrUpdateReaction(id, type, author);
        return "redirect:/videos/" + id;
    }
    
    @PostMapping("/{id}/reactions/remove")
    public String removeReaction(@PathVariable Long id,
                               Authentication auth) {
        String author = auth != null ? auth.getName() : "Anonymous";
        reactionService.deleteByVideoIdAndAuthor(id, author);
        return "redirect:/videos/" + id;
    }
}
