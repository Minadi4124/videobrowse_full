package com.nsoft.sliit.videobrowse.service.impl;

import com.nsoft.sliit.videobrowse.entity.User;
import com.nsoft.sliit.videobrowse.entity.UserProfile;
import com.nsoft.sliit.videobrowse.repository.UserProfileRepository;
import com.nsoft.sliit.videobrowse.service.UserProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;
    public UserProfileServiceImpl(UserProfileRepository userProfileRepository){
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public UserProfile getOrCreateProfile(User user) {
        return userProfileRepository.findByUser(user).orElseGet(() -> {
            UserProfile p = new UserProfile();
            p.setUser(user);
            p.setDisplayName(user.getUsername());
            p.setPreferencesJson("{}");
            return userProfileRepository.save(p);
        });
    }

    @Override
    public Optional<UserProfile> findByUser(User user) {
        return userProfileRepository.findByUser(user);
    }

    @Override
    public UserProfile updateDisplayName(User user, String displayName) {
        UserProfile p = getOrCreateProfile(user);
        p.setDisplayName(displayName);
        p.setUpdatedAt(LocalDateTime.now());
        return userProfileRepository.save(p);
    }

    @Override
    public UserProfile updatePreferences(User user, String preferencesJson) {
        UserProfile p = getOrCreateProfile(user);
        p.setPreferencesJson(preferencesJson);
        p.setUpdatedAt(LocalDateTime.now());
        return userProfileRepository.save(p);
    }

    @Override
    public void deleteProfile(User user) {
        userProfileRepository.findByUser(user).ifPresent(userProfileRepository::delete);
    }
}


