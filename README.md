# 📱 Smart Campus Health & Security Notification Application

## 📌 About the Project

This project was developed as a **final term project** for the
**Mobile Programming Course** in the **2025–2026 Fall Semester**
at **Atatürk University, Department of Computer Engineering**.

The application aims to enable fast and effective reporting, tracking, and management of incidents such as **health issues, security concerns, environmental problems, lost-and-found cases, and technical failures** occurring within the university campus through a mobile platform.

The system is designed as a **centralized digital campus notification platform** with **User** and **Admin** roles.

---

## 🎯 Project Objectives

* To allow rapid reporting of incidents occurring on campus
* To visualize notifications on a map based on location
* To enable users to follow and track reported incidents
* To allow administrators to manage notifications efficiently
* To increase **campus safety, communication, and awareness**

---

## 🔑 Default Test Accounts

For demonstration and evaluation purposes, the application includes two default test accounts.

These accounts are provided solely for academic use during the project evaluation process.
They do not represent real users and are not intended for production environments.

### 👤 User Account
- Email: user@mail.com
- Password: user123

### 🛠️ Admin Account
- Email: admin@mail.com
- Password: admin123

---

## 👥 User Roles

### 👤 User

* Can create incident notifications
* Can list, filter, and search notifications
* Can view notifications on a map based on location
* Can view detailed notification information
* Can follow or unfollow notifications
* Receives notifications when the status of followed incidents changes
* Can manage profile and notification preferences

---

### 🛠️ Administrator (Admin)

* Can view all user-generated notifications
* Can update notification statuses:

  * **Open → In Review → Resolved**
* Can edit notification descriptions if necessary
* Can close invalid or inappropriate notifications
* Can publish emergency alerts and send instant notifications to all users

---

## 📱 Application Screens and Features

### 🔐 Login and Registration Screen

* Login using email and password
* Automatic role-based redirection (User/Admin)
* New user registration
* Password reset simulation screen

---

### 🏠 Home Screen – Notification Feed

* Chronological listing of notifications
* Filtering by type, status, and followed notifications
* Keyword-based search within titles and descriptions

---

### 🗺️ Map Screen

* Notifications displayed with different icons based on their type
* Zoom in / zoom out support
* Access to notification details via map pins

---

### 📄 Notification Detail Screen

* Notification title, type, description, and creation time
* Location information (mini map component)
* Status update option for Admin
* Follow / unfollow option for User

---

### ➕ Create New Notification

* Notification type selection
* Title and description input
* Location selection (device location or manual map selection)
* Optional photo attachment
* Form validation and success feedback

---

### 🧑‍💼 Admin Panel

* Centralized notification management
* Real-time notification status updates
* Emergency alert broadcasting module

---

### ⚙️ Profile and Settings

* Display of user profile information
* Notification preference management
* List of followed notifications
* Logout functionality

---

## 🔔 Notification System

* Instant notifications when the status of followed incidents changes
* Emergency alerts sent to all users by the Admin

---

## 🧰 Technologies Used

* **Android Studio**
* **Kotlin**
* **XML (UI Design)**
* **Git & GitHub**
* (Planned: Map and notification services)

---

## 📂 Project Management

* The project was developed using **GitHub** for version control
* A structured and regular commit history was maintained
* The development process can be followed step by step

---

## 📅 Submission Information

* Course: Mobile Programming
* Semester: 2025–2026 Fall
* Project Type: Final Term Project
* Submission Platform: DBS & GitHub
