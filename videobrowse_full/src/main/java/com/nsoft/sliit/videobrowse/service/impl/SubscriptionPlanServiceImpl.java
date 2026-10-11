package com.nsoft.sliit.videobrowse.service.impl;

import com.nsoft.sliit.videobrowse.entity.SubscriptionPlan;
import com.nsoft.sliit.videobrowse.repository.SubscriptionPlanRepository;
import com.nsoft.sliit.videobrowse.service.SubscriptionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    public List<SubscriptionPlan> getAllActivePlans() {
        return subscriptionPlanRepository.findByActiveTrue();
    }

    @Override
    public SubscriptionPlan getPlanByCode(String code) {
        return subscriptionPlanRepository.findAll().stream()
                .filter(plan -> plan.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }

    @Override
    public SubscriptionPlan createPlan(String code, String name, String price, String features) {
        SubscriptionPlan plan = new SubscriptionPlan();
        plan.setCode(code);
        plan.setName(name);
        plan.setPrice(price);
        plan.setFeatures(features);
        plan.setActive(true);
        return subscriptionPlanRepository.save(plan);
    }

    @Override
    public void savePlan(SubscriptionPlan plan) {
        subscriptionPlanRepository.save(plan);
    }

    @Override
    public void seedDefaultPlans() {
        // Only create plans if they don't exist
        if (subscriptionPlanRepository.count() == 0) {
            System.out.println("Seeding default subscription plans...");
            
            createPlan("basic", "Basic", "Rs. 3000", "720p streaming, Single device");
            createPlan("standard", "Standard", "Rs. 5000", "1080p streaming, Two devices");
            createPlan("premium", "Premium", "Rs. 10000", "4K streaming, Four devices");
            
            System.out.println("Default subscription plans created successfully!");
        } else {
            System.out.println("Subscription plans already exist in database.");
        }
    }
}

