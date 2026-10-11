# VideoBrowseSystem — Full 6 Components (Spring Boot 3, Java 17)

Includes:
1) Video Content Management (upload, edit, delete)
2) User Browsing & Search (title search, category filter)
3) Playback & Interaction (basic session tracking via AJAX)
4) User Accounts (register/login, roles ROLE_USER/ROLE_ADMIN)
5) Support & Communication (tickets CRUD)
6) Administration (dashboard, manage users/videos/tickets)

## Run (H2 default)
1. Open in IntelliJ (File -> Open -> this folder), ensure Java 17.
2. Run `VideoBrowseApplication`.
3. Visit http://localhost:8080
4. Register an account; check 'admin' to create an admin user.

Default accounts created: admin/adminpass, user/userpass

## MySQL (optional)
- Configure docker/mysql or local MySQL; then run with `-Dspring.profiles.active=mysql`.

## Notes
- Uploaded files go to `data/videos/`.
- H2 console at `/h2`.
- To change default passwords, edit VideoBrowseApplication init bean.
