package com.nsoft.sliit.videobrowse.service;

import com.nsoft.sliit.videobrowse.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User register(String username, String email, String rawPassword, boolean admin);
    List<User> findAll();
    Optional<User> findById(Long id);
    void delete(Long id);
    void deleteUserAndRelatedData(Long id);
    Optional<User> getCurrentUser();
}
