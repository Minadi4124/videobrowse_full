package com.nsoft.sliit.videobrowse.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "subscription_plans")
public class SubscriptionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "price", nullable = false, length = 20)
    private String price;

    @Column(name = "features", nullable = false, length = 500)
    private String features;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getFeatures() { return features; }
    public void setFeatures(String features) { this.features = features; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}

