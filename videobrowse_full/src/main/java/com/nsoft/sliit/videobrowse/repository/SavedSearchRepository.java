package com.nsoft.sliit.videobrowse.repository;

import com.nsoft.sliit.videobrowse.entity.SavedSearch;
import com.nsoft.sliit.videobrowse.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SavedSearchRepository extends JpaRepository<SavedSearch, Long> {
    List<SavedSearch> findByUserOrderByCreatedAtDesc(User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM SavedSearch s WHERE s.user = :user")
    void deleteByUser(@Param("user") User user);
}


