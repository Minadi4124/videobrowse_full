package com.nsoft.sliit.videobrowse.service;

import com.nsoft.sliit.videobrowse.entity.SubscriptionPlan;
import java.util.List;

public interface SubscriptionPlanService {
    List<SubscriptionPlan> getAllActivePlans();
    SubscriptionPlan getPlanByCode(String code);
    SubscriptionPlan createPlan(String code, String name, String price, String features);
    void savePlan(SubscriptionPlan plan);
    void seedDefaultPlans();
}

