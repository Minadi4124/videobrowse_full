package com.nsoft.sliit.videobrowse.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("MAIN")
public class MainCategory extends Category {
}


