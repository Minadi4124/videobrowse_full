package com.nsoft.sliit.videobrowse.repository;

import com.nsoft.sliit.videobrowse.entity.Subscription;
import com.nsoft.sliit.videobrowse.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findBySubscriber(User subscriber);
    Optional<Subscription> findBySubscriberAndCreatorName(User subscriber, String creatorName);
    List<Subscription> findByCreatorName(String creatorName);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Subscription s WHERE s.subscriber = :subscriber")
    void deleteBySubscriber(@Param("subscriber") User subscriber);
}


