package com.nsoft.sliit.videobrowse.service.impl;

import com.nsoft.sliit.videobrowse.entity.SearchHistory;
import com.nsoft.sliit.videobrowse.entity.User;
import com.nsoft.sliit.videobrowse.repository.SearchHistoryRepository;
import com.nsoft.sliit.videobrowse.service.SearchHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SearchHistoryServiceImpl implements SearchHistoryService {
    private final SearchHistoryRepository searchHistoryRepository;
    public SearchHistoryServiceImpl(SearchHistoryRepository searchHistoryRepository){
        this.searchHistoryRepository = searchHistoryRepository;
    }

    @Override
    public SearchHistory log(User user, String query) {
        SearchHistory h = new SearchHistory();
        h.setUser(user);
        h.setQuery(query);
        return searchHistoryRepository.save(h);
    }

    @Override
    public List<SearchHistory> list(User user) {
        return searchHistoryRepository.findByUserOrderBySearchedAtDesc(user);
    }

    @Override
    public void clear(User user) {
        searchHistoryRepository.deleteAll(searchHistoryRepository.findByUserOrderBySearchedAtDesc(user));
    }
}


