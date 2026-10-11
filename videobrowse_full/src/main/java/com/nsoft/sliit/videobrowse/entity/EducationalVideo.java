package com.nsoft.sliit.videobrowse.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("EDU")
public class EducationalVideo extends Video {
    private String subject;
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
}


