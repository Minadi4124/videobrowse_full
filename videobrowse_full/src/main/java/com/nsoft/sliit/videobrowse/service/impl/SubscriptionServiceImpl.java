package com.nsoft.sliit.videobrowse.service.impl;

import com.nsoft.sliit.videobrowse.entity.Subscription;
import com.nsoft.sliit.videobrowse.entity.User;
import com.nsoft.sliit.videobrowse.repository.SubscriptionRepository;
import com.nsoft.sliit.videobrowse.service.SubscriptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository){ this.subscriptionRepository = subscriptionRepository; }

    @Override
    public List<Subscription> list(User user) { return subscriptionRepository.findBySubscriber(user); }

    @Override
    public Subscription subscribe(User user, String creatorName, String code, String price) {
        Subscription existingSubscription = subscriptionRepository.findBySubscriberAndCreatorName(user, creatorName)
                .orElse(null);
        
        if (existingSubscription != null) {
            // Update existing subscription with code and price
            existingSubscription.setCode(code);
            existingSubscription.setPrice(price);
            return subscriptionRepository.save(existingSubscription);
        } else {
            // Create new subscription
            Subscription s = new Subscription();
            s.setSubscriber(user);
            s.setCreatorName(creatorName);
            s.setCode(code);
            s.setPrice(price);
            return subscriptionRepository.save(s);
        }
    }

    @Override
    public Subscription subscribe(User user, String creatorName) {
        // Legacy method - subscribe without code and price
        return subscriptionRepository.findBySubscriberAndCreatorName(user, creatorName)
                .orElseGet(() -> {
                    Subscription s = new Subscription();
                    s.setSubscriber(user);
                    s.setCreatorName(creatorName);
                    return subscriptionRepository.save(s);
                });
    }

    @Override
    public void unsubscribe(User user, String creatorName) {
        subscriptionRepository.findBySubscriberAndCreatorName(user, creatorName)
                .ifPresent(subscriptionRepository::delete);
    }
}


