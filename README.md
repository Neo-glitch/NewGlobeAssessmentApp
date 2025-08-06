# Bridge International Academies App

A mobile application for administering pupil information, developed as part of the **NewGlobe** technical assessment.

---

## Overview

This Android application allows users to manage pupil records via a RESTful API with offline capabilities and automatic synchronization. It demonstrates production-grade Android development practices, focusing on:

- Error resilience
- Offline-first architecture
- Clean MVVM structure
- Local and remote data handling

---

## Features

- **View Pupils List**: Display all pupil records (Name, Country, Image)
- **Add New Pupil**: Create new pupil entries with real-time form validation
- **Offline Support**: Full functionality without internet; automatic sync when back online
- **Error Handling**: Graceful handling of API/network && Database failures
- **Data Synchronization**: Seamless local-to-remote sync when connectivity is restored

---

## Technical Implementation

### Architecture

- **MVVM Pattern**: ViewModel and LiveData to separate UI from logic
- **Repository Pattern**: Centralized handling of local and remote data
- **Room Database**: For local data persistence
- **Retrofit**: For HTTP API communication
- **Kotlin Coroutines**: For background/asynchronous processing

### Key Components

| Component      | Description |
|----------------|-------------|
| **Room DB**    | Offline storage for pupil records |
| **Retrofit**   | Handles all HTTP requests and responses |
| **WorkManager**| Manages background sync tasks |
| **Sync Manager** | Synchronizes local and remote data |
| **Validation Layer** | Ensures valid inputs (name, country, image, coordinates) |

---

## API Integration

- **Base URL**: `https://androidtechnicaltestapi-test.bridgeinternationalacademies.com/`
- **Documentation**: Available via Swagger UI
- **Endpoints**: Supports `GET`, `POST`, `PUT`, `DELETE`

---

## ✅ Requirements Implemented

- ✅ View Pupils List
- ✅ Add New Pupil with validation
- ✅ Full Offline Support
- ✅ Robust Error Handling
- ✅ Client-side Input Validation
- ✅ Data Synchronization

---

## Design Decisions & Assumptions

### Architecture Choices

- **Offline-First**: Prioritize local data usage
- **Single Source of Truth**: Room DB acts as the master data source
- **Background Sync**: Triggers automatically when network is available

### Assumptions

- Pupil IDs are generated and managed by the server
- Image URLs from the API are valid and accessible
- Intermittent network connectivity is expected
- Users require responsive UI even when offline

### Validation Rules

- **Name**: Required, non-empty
- **Country**: Required, valid
- **Coordinates**: Must be within latitude (-90 to 90) and longitude (-180 to 180)
- **Image**: Required, non-empty

---

## Installation & Setup

```bash
git clone https://github.com/Neo-glitch/NewGlobeAssessmentApp.git
cd NewGlobeAssessmentApp
