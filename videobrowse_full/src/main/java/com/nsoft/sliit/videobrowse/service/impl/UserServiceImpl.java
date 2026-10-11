package com.nsoft.sliit.videobrowse.service.impl;

import com.nsoft.sliit.videobrowse.entity.User;
import com.nsoft.sliit.videobrowse.entity.Role;
import com.nsoft.sliit.videobrowse.repository.UserRepository;
import com.nsoft.sliit.videobrowse.repository.RoleRepository;
import com.nsoft.sliit.videobrowse.repository.UserProfileRepository;
import com.nsoft.sliit.videobrowse.repository.WatchHistoryRepository;
import com.nsoft.sliit.videobrowse.repository.SearchHistoryRepository;
import com.nsoft.sliit.videobrowse.repository.SavedSearchRepository;
import com.nsoft.sliit.videobrowse.repository.SubscriptionRepository;
import com.nsoft.sliit.videobrowse.repository.AlertRepository;
import com.nsoft.sliit.videobrowse.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserProfileRepository userProfileRepository;
    private final WatchHistoryRepository watchHistoryRepository;
    private final SearchHistoryRepository searchHistoryRepository;
    private final SavedSearchRepository savedSearchRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final AlertRepository alertRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, 
                         PasswordEncoder passwordEncoder, UserProfileRepository userProfileRepository,
                         WatchHistoryRepository watchHistoryRepository, SearchHistoryRepository searchHistoryRepository,
                         SavedSearchRepository savedSearchRepository, SubscriptionRepository subscriptionRepository,
                         AlertRepository alertRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userProfileRepository = userProfileRepository;
        this.watchHistoryRepository = watchHistoryRepository;
        this.searchHistoryRepository = searchHistoryRepository;
        this.savedSearchRepository = savedSearchRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.alertRepository = alertRepository;
    }

    @Override
    public User register(String username, String email, String rawPassword, boolean admin) {
        System.out.println("UserService.register called with username: " + username + ", email: " + email + ", admin: " + admin);
        
        // Check for existing username
        if (userRepository.existsByUsername(username)) {
            System.out.println("Username already exists: " + username);
            throw new RuntimeException("Username '" + username + "' is already taken. Please choose a different username.");
        }
        
        // Check for existing email
        if (userRepository.existsByEmail(email)) {
            System.out.println("Email already exists: " + email);
            throw new RuntimeException("Email '" + email + "' is already registered. Please use a different email address.");
        }

        System.out.println("Creating new user...");
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));

        System.out.println("Setting up roles...");
        Set<Role> roles = new HashSet<>();
        if (admin) {
            System.out.println("Adding admin role...");
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow();
            roles.add(adminRole);
        }
        System.out.println("Adding user role...");
        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow();
        roles.add(userRole);
        user.setRoles(roles);

        System.out.println("Saving user to database...");
        User savedUser = userRepository.save(user);
        System.out.println("User saved successfully with ID: " + savedUser.getId());
        return savedUser;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return Optional.empty();
        String username = auth.getName();
        return userRepository.findByUsername(username);
    }

    @Override
    public void deleteUserAndRelatedData(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        
        try {
            // Delete related records first
            userProfileRepository.deleteByUser(user);
            watchHistoryRepository.deleteByUser(user);
            searchHistoryRepository.deleteByUser(user);
            savedSearchRepository.deleteByUser(user);
            subscriptionRepository.deleteBySubscriber(user);
            alertRepository.deleteByUser(user);
            
            // Finally delete the user
            userRepository.delete(user);
            System.out.println("Successfully deleted user and all related data for ID: " + id);
            
        } catch (Exception e) {
            System.err.println("Error deleting user and related data: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to delete user and related data: " + e.getMessage(), e);
        }
    }
}
