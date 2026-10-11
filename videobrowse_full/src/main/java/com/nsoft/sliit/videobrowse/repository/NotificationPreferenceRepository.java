package com.nsoft.sliit.videobrowse.repository;

import com.nsoft.sliit.videobrowse.entity.NotificationPreference;
import com.nsoft.sliit.videobrowse.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, Long> {
    Optional<NotificationPreference> findByUser(User user);
}


