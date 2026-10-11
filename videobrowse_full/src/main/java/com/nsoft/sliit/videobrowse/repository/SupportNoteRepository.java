package com.nsoft.sliit.videobrowse.repository;

import com.nsoft.sliit.videobrowse.entity.SupportNote;
import com.nsoft.sliit.videobrowse.entity.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportNoteRepository extends JpaRepository<SupportNote, Long> {
    List<SupportNote> findByTicketOrderByCreatedAtAsc(SupportTicket ticket);
}


