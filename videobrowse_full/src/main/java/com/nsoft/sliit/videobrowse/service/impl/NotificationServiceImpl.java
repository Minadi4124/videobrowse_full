package com.nsoft.sliit.videobrowse.service.impl;

import com.nsoft.sliit.videobrowse.entity.Alert;
import com.nsoft.sliit.videobrowse.entity.NotificationPreference;
import com.nsoft.sliit.videobrowse.entity.User;
import com.nsoft.sliit.videobrowse.repository.AlertRepository;
import com.nsoft.sliit.videobrowse.repository.NotificationPreferenceRepository;
import com.nsoft.sliit.videobrowse.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {
    private final AlertRepository alertRepository;
    private final NotificationPreferenceRepository preferenceRepository;
    public NotificationServiceImpl(AlertRepository alertRepository, NotificationPreferenceRepository preferenceRepository){
        this.alertRepository = alertRepository;
        this.preferenceRepository = preferenceRepository;
    }

    @Override
    public void notifyUser(User user, String message) {
        NotificationPreference pref = preferenceRepository.findByUser(user).orElseGet(() -> {
            NotificationPreference p = new NotificationPreference();
            p.setUser(user);
            return preferenceRepository.save(p);
        });
        if (pref.isInAppEnabled()){
            Alert a = new Alert();
            a.setUser(user);
            a.setMessage(message);
            alertRepository.save(a);
        }
        // Email strategy could be added here later if configured
    }
}


