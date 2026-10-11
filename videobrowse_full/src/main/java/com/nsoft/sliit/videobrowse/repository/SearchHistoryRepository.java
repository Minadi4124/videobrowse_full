package com.nsoft.sliit.videobrowse.repository;

import com.nsoft.sliit.videobrowse.entity.SearchHistory;
import com.nsoft.sliit.videobrowse.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findByUserOrderBySearchedAtDesc(User user);
    List<SearchHistory> findByUserAndQueryContainingIgnoreCaseOrderBySearchedAtDesc(User user, String query);

    @Modifying
    @Transactional
    @Query("DELETE FROM SearchHistory s WHERE s.user = :user")
    void deleteByUser(@Param("user") User user);
}


