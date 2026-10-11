package com.nsoft.sliit.videobrowse.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ENT")
public class EntertainmentVideo extends Video {
    private String rating; // e.g., PG, PG-13
    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }
}


