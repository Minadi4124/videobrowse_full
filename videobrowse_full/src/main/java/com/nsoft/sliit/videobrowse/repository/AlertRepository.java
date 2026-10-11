package com.nsoft.sliit.videobrowse.repository;

import com.nsoft.sliit.videobrowse.entity.Alert;
import com.nsoft.sliit.videobrowse.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByUserOrderByCreatedAtDesc(User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM Alert a WHERE a.user = :user")
    void deleteByUser(@Param("user") User user);
}


