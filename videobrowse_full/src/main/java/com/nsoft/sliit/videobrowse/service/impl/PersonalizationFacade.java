package com.nsoft.sliit.videobrowse.service.impl;

import com.nsoft.sliit.videobrowse.entity.User;
import com.nsoft.sliit.videobrowse.entity.Video;
import com.nsoft.sliit.videobrowse.service.PersonalizationService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class PersonalizationFacade {
    private final List<PersonalizationService> strategies;
    public PersonalizationFacade(List<PersonalizationService> strategies){ this.strategies = strategies; }

    public List<Video> recommend(User user, int limit){
        return strategies.stream()
                .sorted(Comparator.comparingInt(s -> s.getClass().isAnnotationPresent(org.springframework.core.annotation.Order.class) ? s.getClass().getAnnotation(org.springframework.core.annotation.Order.class).value() : Integer.MAX_VALUE))
                .filter(s -> s.supports(user))
                .findFirst()
                .map(s -> s.recommend(user, limit))
                .orElse(List.of());
    }
}


