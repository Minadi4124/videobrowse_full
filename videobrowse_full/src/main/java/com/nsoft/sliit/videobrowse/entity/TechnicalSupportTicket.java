package com.nsoft.sliit.videobrowse.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("TECH")
public class TechnicalSupportTicket extends SupportTicket {
    private String affectedFeature;
    public String getAffectedFeature() { return affectedFeature; }
    public void setAffectedFeature(String affectedFeature) { this.affectedFeature = affectedFeature; }
}


