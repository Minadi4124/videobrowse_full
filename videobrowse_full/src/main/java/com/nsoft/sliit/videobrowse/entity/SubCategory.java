package com.nsoft.sliit.videobrowse.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("SUB")
public class SubCategory extends Category {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private MainCategory parent;

    public MainCategory getParent() { return parent; }
    public void setParent(MainCategory parent) { this.parent = parent; }
}


