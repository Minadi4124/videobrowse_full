package com.nsoft.sliit.videobrowse.service;

import java.util.List;
import java.util.Optional;

import com.nsoft.sliit.videobrowse.entity.SupportTicket;

public interface SupportTicketService {
    SupportTicket create(String username, String email, String message);
    SupportTicket createTyped(String username, String email, String message, String type, String extra);
    List<SupportTicket> findAll();
    Optional<SupportTicket> findById(Long id);
    SupportTicket updateStatus(Long id, String status);
    void addNote(Long id, String note);
    void delete(Long id);
    List<SupportTicket> findByUsername(String username);
    Optional<SupportTicket> findByIdAndUsername(Long id, String username);
    SupportTicket updateMessage(Long id, String username, String message);
}
