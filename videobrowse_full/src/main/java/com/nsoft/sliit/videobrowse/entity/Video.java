package com.nsoft.sliit.videobrowse.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="videos")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "video_type")
@DiscriminatorValue("BASE")
public class Video {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(length=2000)
    private String description;
    private String filename;
    private String thumbnail;
    private String format;
    private Integer durationSeconds;
    private String category;
    private String uploader;
    private LocalDateTime uploadedAt;

    @Column(length=1000)
    private String tags; // comma-separated

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Visibility visibility = Visibility.PUBLIC;

    public enum Visibility { PUBLIC, PRIVATE, UNLISTED }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_ref_id")
    private Category categoryRef;

    @ManyToMany
    @JoinTable(name = "video_tags",
        joinColumns = @JoinColumn(name = "video_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tagRefs = new HashSet<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }
    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }
    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
    public Integer getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getUploader() { return uploader; }
    public void setUploader(String uploader) { this.uploader = uploader; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public Visibility getVisibility() { return visibility; }
    public void setVisibility(Visibility visibility) { this.visibility = visibility; }
    public Category getCategoryRef() { return categoryRef; }
    public void setCategoryRef(Category categoryRef) { this.categoryRef = categoryRef; }
    public Set<Tag> getTagRefs() { return tagRefs; }
    public void setTagRefs(Set<Tag> tagRefs) { this.tagRefs = tagRefs; }
}
