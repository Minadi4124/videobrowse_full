package com.nsoft.sliit.videobrowse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nsoft.sliit.videobrowse.entity.SupportTicket;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    List<SupportTicket> findByUsernameOrderByCreatedAtDesc(String username);
    Optional<SupportTicket> findByIdAndUsername(Long id, String username);
}
