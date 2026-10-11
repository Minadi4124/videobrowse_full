package com.nsoft.sliit.videobrowse.config;

import com.nsoft.sliit.videobrowse.service.SubscriptionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionPlanSeeder implements CommandLineRunner {

    @Autowired
    private SubscriptionPlanService subscriptionPlanService;

    @Override
    public void run(String... args) throws Exception {
        subscriptionPlanService.seedDefaultPlans();
    }
}

