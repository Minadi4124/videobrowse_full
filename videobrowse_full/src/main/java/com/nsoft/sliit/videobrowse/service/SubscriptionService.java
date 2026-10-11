package com.nsoft.sliit.videobrowse.service;

import com.nsoft.sliit.videobrowse.entity.Subscription;
import com.nsoft.sliit.videobrowse.entity.User;

import java.util.List;

public interface SubscriptionService {
    List<Subscription> list(User user);
    Subscription subscribe(User user, String creatorName, String code, String price);
    Subscription subscribe(User user, String creatorName);  // Legacy method for backward compatibility
    void unsubscribe(User user, String creatorName);
}


