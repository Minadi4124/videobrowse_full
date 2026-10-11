package com.nsoft.sliit.videobrowse.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("GEN")
public class GeneralInquiryTicket extends SupportTicket {
    private String topic;
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
}


