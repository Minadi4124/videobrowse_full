# VideoBrowse System - Complete Technical Documentation

## 🎯 Project Overview

**Project Name:** VideoBrowse System (SLIITFLIX)  
**Technology Stack:** Spring Boot 3.2.6, Java 17, Maven, H2/MySQL, Thymeleaf, Bootstrap  
**Architecture:** MVC (Model-View-Controller) with Spring Security  
**Purpose:** A full-featured video streaming platform with user management, content management, and administrative capabilities

---

## 🏗️ Spring Boot Architecture Components

### 1. **Controllers** - The Traffic Directors
**What are Controllers?**
Controllers are the entry points of your application. They handle HTTP requests from users and coordinate between the frontend and backend services.

**How they work:**
- Receive HTTP requests (GET, POST, PUT, DELETE)
- Validate input data
- Call appropriate services
- Return responses (HTML pages, JSON data, redirects)

**Example in our project:**
```java
@Controller
@RequestMapping("/videos")
public class VideoController {
    @GetMapping("/upload")
    public String uploadForm() {
        return "videos/upload"; // Returns HTML template
    }
    
    @PostMapping("/upload")
    public String uploadVideo(@ModelAttribute VideoUploadRequest request) {
        videoService.uploadVideo(request); // Calls service
        return "redirect:/videos"; // Redirects user
    }
}
```

### 2. **Entities** - The Data Models
**What are Entities?**
Entities represent database tables as Java objects. They define the structure of your data and relationships between different data types.

**Key Features:**
- Map to database tables
- Define relationships (One-to-Many, Many-to-One)
- Include validation annotations
- Handle data persistence

**Example in our project:**
```java
@Entity
@Table(name = "users")
public class User {
    @Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserProfile profile;
    
    // Getters and setters
}
```

### 3. **Repositories** - The Data Access Layer
**What are Repositories?**
Repositories handle all database operations. They provide methods to save, find, update, and delete data without writing SQL queries.

**Types in our project:**
- **JpaRepository:** Provides basic CRUD operations
- **Custom Queries:** For complex database operations

**Example:**
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByRolesContaining(Role role);
}
```

### 4. **Services** - The Business Logic
**What are Services?**
Services contain the business logic of your application. They process data, implement business rules, and coordinate between different components.

**Responsibilities:**
- Business logic implementation
- Data validation
- Transaction management
- Integration with multiple repositories

**Example:**
```java
@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    public User registerUser(String username, String email, String password) {
        // Business logic: validate, encrypt password, save user
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }
}
```

### 5. **Security** - The Protection Layer
**What is Spring Security?**
Spring Security handles authentication (who you are) and authorization (what you can do) in your application.

**Components:**
- **Authentication:** Login/logout functionality
- **Authorization:** Role-based access control
- **Password Encryption:** BCrypt hashing
- **Session Management:** User sessions and remember-me

**Example:**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/profile/**").authenticated()
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/")
            )
            .build();
    }
}
```

---

## 🔄 How Components Work Together

### Request Flow:
1. **User** makes HTTP request → **Controller**
2. **Controller** validates input → **Service**
3. **Service** processes business logic → **Repository**
4. **Repository** interacts with **Database**
5. **Response** flows back through the same chain

### Example Flow (User Registration):
```
User fills form → AuthController.register() → UserService.registerUser() 
→ UserRepository.save() → Database → Success response → Redirect to login
```

---

## 📁 Project Structure Analysis

### **pom.xml** - The Project Configuration
**What does pom.xml do?**
The Project Object Model (POM) file defines your project's dependencies, build configuration, and metadata.

**Key Sections:**
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.6</version>
</parent>

<dependencies>
    <!-- Web framework -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Database -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <!-- Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
</dependencies>
```

**What each dependency does:**
- **spring-boot-starter-web:** Provides web server and REST capabilities
- **spring-boot-starter-data-jpa:** Database operations and ORM
- **spring-boot-starter-security:** Authentication and authorization
- **spring-boot-starter-thymeleaf:** Template engine for HTML pages
- **h2:** Embedded database for development
- **mysql-connector-java:** MySQL database driver

---

## 🗄️ Database Configuration

### **application.properties** - The Configuration File
```properties
# Application settings
spring.application.name=SLIITFLIX
server.port=8080

# H2 Database (Default)
spring.datasource.url=jdbc:h2:file:./data/h2db/videobrowse
spring.datasource.username=sa
spring.datasource.password=

# JPA/Hibernate settings
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# File upload settings
spring.servlet.multipart.max-file-size=2GB
spring.servlet.multipart.max-request-size=2GB
```

**What each setting does:**
- **ddl-auto=update:** Automatically creates/updates database tables
- **show-sql=true:** Displays SQL queries in console (for debugging)
- **max-file-size=2GB:** Allows large video file uploads

---

## 📂 File-by-File Analysis

### **Java Files:**

#### 1. **VideoBrowseApplication.java** - The Main Class
```java
@SpringBootApplication
public class VideoBrowseApplication {
    public static void main(String[] args) {
        SpringApplication.run(VideoBrowseApplication.class, args);
    }
    
    @Bean
    @EventListener(ApplicationReadyEvent.class)
    public void seedData() {
        // Creates default admin and user accounts
        userService.register("admin", "admin@example.com", "adminpass", true);
        userService.register("user", "user@example.com", "userpass", false);
    }
}
```
**Purpose:** Application entry point and initial data seeding

#### 2. **User.java** - User Entity
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserProfile profile;
}
```
**Purpose:** Defines user data structure and database mapping

#### 3. **UserController.java** - User Management Controller
```java
@Controller
@RequestMapping("/profile")
public class ProfileController {
    @GetMapping("/view")
    public String viewProfile(Model model, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        model.addAttribute("user", user);
        return "profile/view";
    }
    
    @PostMapping("/update")
    public String updateProfile(@ModelAttribute User user) {
        userService.updateUser(user);
        return "redirect:/profile/view";
    }
}
```
**Purpose:** Handles user profile-related HTTP requests

#### 4. **UserService.java** - User Business Logic
```java
@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public User register(String username, String email, String password, boolean isAdmin) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        
        if (isAdmin) {
            user.getRoles().add(Role.ROLE_ADMIN);
        } else {
            user.getRoles().add(Role.ROLE_USER);
        }
        
        return userRepository.save(user);
    }
}
```
**Purpose:** Implements user-related business logic and operations

#### 5. **UserRepository.java** - Data Access Layer
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByRolesContaining(Role role);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
```
**Purpose:** Provides database access methods for User entity

### **HTML Templates:**

#### 1. **login.html** - Login Page
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Login - SLIITFLIX</title>
    <link href="/css/theme.css" rel="stylesheet">
</head>
<body>
    <div class="container">
        <form th:action="@{/login}" method="post">
            <input type="text" name="username" placeholder="Username" required>
            <input type="password" name="password" placeholder="Password" required>
            <button type="submit">Login</button>
        </form>
    </div>
</body>
</html>
```
**Purpose:** User authentication interface

#### 2. **register.html** - Registration Page
```html
<form th:action="@{/register}" method="post" th:object="${user}">
    <input type="text" th:field="*{username}" placeholder="Username" required>
    <input type="email" th:field="*{email}" placeholder="Email" required>
    <input type="password" th:field="*{password}" placeholder="Password" required>
    <input type="checkbox" name="isAdmin"> Make me an admin
    <button type="submit">Register</button>
</form>
```
**Purpose:** New user registration interface

#### 3. **profile/view.html** - User Profile Page
```html
<div class="profile-container">
    <h2>Welcome, <span th:text="${user.username}"></span></h2>
    <div class="profile-info">
        <p>Email: <span th:text="${user.email}"></span></p>
        <p>Member since: <span th:text="${#dates.format(user.createdAt, 'MMM dd, yyyy')}"></span></p>
    </div>
    <div class="profile-actions">
        <a href="/profile/watch-history">Watch History</a>
        <a href="/profile/preferences">Preferences</a>
        <a href="/profile/my-uploads">My Uploads</a>
    </div>
</div>
```
**Purpose:** User profile display and navigation

---

## 🔐 Security Implementation

### **SecurityConfig.java** - Security Configuration
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/profile/**").authenticated()
                .requestMatchers("/videos/upload").authenticated()
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error=true")
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
            )
            .build();
    }
}
```

**Security Features:**
- **Role-based access:** Admin vs regular user permissions
- **Protected routes:** Certain pages require authentication
- **Form-based login:** Custom login page
- **Session management:** Automatic session handling

---

## 🎯 Key Features Implementation

### 1. **User Management System**
- **Registration:** New user signup with role assignment
- **Authentication:** Secure login with password encryption
- **Profile Management:** User profile viewing and editing
- **Role-based Access:** Admin and user permission levels

### 2. **Video Content Management**
- **Upload:** Large file upload support (up to 2GB)
- **Storage:** Local file system storage in `./data/videos/`
- **Metadata:** Video information storage in database
- **Categories:** Video categorization and tagging

### 3. **Search and Browse**
- **Title Search:** Full-text search across video titles
- **Category Filtering:** Browse videos by category
- **Pagination:** Efficient handling of large video collections

### 4. **User Interaction**
- **Watch History:** Track user viewing history
- **Search History:** Remember user search queries
- **Preferences:** User-specific settings storage

### 5. **Administrative Features**
- **User Management:** Admin can manage all users
- **Content Management:** Admin can manage all videos
- **Dashboard:** Administrative overview and statistics

---

## 🚀 How to Run the Project

### **Prerequisites:**
1. Java 17 or higher
2. Maven 3.6+
3. IDE (IntelliJ IDEA recommended)

### **Setup Steps:**
1. Extract the project ZIP file
2. Open in IDE (File → Open → Select project folder)
3. Wait for Maven dependencies to download
4. Run `VideoBrowseApplication.java`
5. Open browser to `http://localhost:8080`

### **Default Accounts:**
- **Admin:** username: `admin`, password: `adminpass`
- **User:** username: `user`, password: `userpass`

### **Database Access:**
- **H2 Console:** `http://localhost:8080/h2`
- **URL:** `jdbc:h2:file:./data/h2db/videobrowse`
- **Username:** `sa`
- **Password:** (leave empty)

---

## 📊 Database Schema

### **Main Tables:**
- **users:** User accounts and authentication
- **user_profiles:** Extended user information
- **videos:** Video metadata and file information
- **categories:** Video categorization
- **watch_history:** User viewing history
- **search_history:** User search queries
- **support_tickets:** Customer support system

### **Relationships:**
- User → UserProfile (One-to-One)
- User → WatchHistory (One-to-Many)
- User → Videos (One-to-Many, for uploads)
- Video → Category (Many-to-One)

---

## 🎨 Frontend Technology

### **Thymeleaf Templates:**
- **Server-side rendering:** HTML templates with dynamic content
- **Form binding:** Automatic form-to-object mapping
- **Security integration:** Built-in CSRF protection
- **Bootstrap styling:** Responsive and modern UI

### **Static Resources:**
- **CSS:** Custom styling in `/static/css/theme.css`
- **JavaScript:** Client-side functionality
- **Images:** UI icons and graphics

---

## 🔧 Configuration Management

### **Environment Profiles:**
- **Default:** H2 embedded database (development)
- **MySQL:** Production database configuration
- **Properties:** Externalized configuration for different environments

### **File Upload Configuration:**
- **Maximum file size:** 2GB for video uploads
- **Storage location:** `./data/videos/` directory
- **File validation:** Type and size checking

---

This comprehensive documentation covers all aspects of the VideoBrowse System, from architecture components to implementation details, providing a complete understanding of how a Spring Boot application works and how all the pieces fit together to create a functional video streaming platform.
