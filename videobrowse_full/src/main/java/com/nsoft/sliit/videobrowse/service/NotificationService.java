package com.nsoft.sliit.videobrowse.service;

import com.nsoft.sliit.videobrowse.entity.User;

public interface NotificationService {
    void notifyUser(User user, String message);
}


