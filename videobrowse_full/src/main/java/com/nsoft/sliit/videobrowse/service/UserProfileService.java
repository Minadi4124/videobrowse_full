package com.nsoft.sliit.videobrowse.service;

import com.nsoft.sliit.videobrowse.entity.User;
import com.nsoft.sliit.videobrowse.entity.UserProfile;

import java.util.Optional;

public interface UserProfileService {
    UserProfile getOrCreateProfile(User user);
    Optional<UserProfile> findByUser(User user);
    UserProfile updateDisplayName(User user, String displayName);
    UserProfile updatePreferences(User user, String preferencesJson);
    void deleteProfile(User user);
}


