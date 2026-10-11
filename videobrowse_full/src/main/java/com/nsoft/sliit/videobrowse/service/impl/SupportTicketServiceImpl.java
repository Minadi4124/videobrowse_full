package com.nsoft.sliit.videobrowse.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nsoft.sliit.videobrowse.entity.GeneralInquiryTicket;
import com.nsoft.sliit.videobrowse.entity.SupportNote;
import com.nsoft.sliit.videobrowse.entity.SupportTicket;
import com.nsoft.sliit.videobrowse.entity.TechnicalSupportTicket;
import com.nsoft.sliit.videobrowse.repository.SupportNoteRepository;
import com.nsoft.sliit.videobrowse.repository.SupportTicketRepository;
import com.nsoft.sliit.videobrowse.service.SupportTicketService;

@Service
public class SupportTicketServiceImpl implements SupportTicketService {
    private final SupportTicketRepository repo;
    private final SupportNoteRepository noteRepo;
    public SupportTicketServiceImpl(SupportTicketRepository repo, SupportNoteRepository noteRepo){ this.repo = repo; this.noteRepo = noteRepo; }

    @Override public SupportTicket create(String username, String email, String message){
        SupportTicket t = new SupportTicket();
        t.setUsername(username);
        t.setEmail(email);
        t.setMessage(message);
        t.setStatus("OPEN");
        return repo.save(t);
    }
    @Override public List<SupportTicket> findAll(){ return repo.findAll(); }
    @Override public Optional<SupportTicket> findById(Long id){ return repo.findById(id); }
    @Override public SupportTicket updateStatus(Long id, String status){
        SupportTicket t = repo.findById(id).orElseThrow();
        t.setStatus(status);
        return repo.save(t);
    }
    @Override public void addNote(Long id, String note){
        SupportTicket t = repo.findById(id).orElseThrow();
        SupportNote n = new SupportNote();
        n.setTicket(t);
        n.setNote(note);
        noteRepo.save(n);
    }
    @Override public void delete(Long id){ repo.deleteById(id); }

    @Override
    public SupportTicket createTyped(String username, String email, String message, String type, String extra) {
        SupportTicket t;
        if ("TECH".equalsIgnoreCase(type)){
            TechnicalSupportTicket tt = new TechnicalSupportTicket();
            tt.setAffectedFeature(extra);
            t = tt;
        } else if ("GEN".equalsIgnoreCase(type)){
            GeneralInquiryTicket gt = new GeneralInquiryTicket();
            gt.setTopic(extra);
            t = gt;
        } else {
            t = new SupportTicket();
        }
        t.setUsername(username);
        t.setEmail(email);
        t.setMessage(message);
        t.setStatus("OPEN");
        return repo.save(t);
    }

    @Override
    public List<SupportTicket> findByUsername(String username) {
        return repo.findByUsernameOrderByCreatedAtDesc(username);
    }

    @Override
    public Optional<SupportTicket> findByIdAndUsername(Long id, String username) {
        return repo.findByIdAndUsername(id, username);
    }

    @Override
    public SupportTicket updateMessage(Long id, String username, String message) {
        SupportTicket t = repo.findByIdAndUsername(id, username).orElseThrow();
        t.setMessage(message);
        return repo.save(t);
    }
}
