package com.nsoft.sliit.videobrowse.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "subscriptions", uniqueConstraints = @UniqueConstraint(columnNames = {"subscriber_id", "creator_name"}))
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id", nullable = false)
    private User subscriber;

    @Column(name = "creator_name", nullable = false, length = 100)
    private String creatorName;

    @Column(name = "code", length = 50)
    private String code;

    @Column(name = "price", length = 20)
    private String price;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getSubscriber() { return subscriber; }
    public void setSubscriber(User subscriber) { this.subscriber = subscriber; }
    public String getCreatorName() { return creatorName; }
    public void setCreatorName(String creatorName) { this.creatorName = creatorName; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
}


