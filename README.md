<div align="center">

# Matule

**A modern Android application built with Kotlin & Jetpack Compose**

![Kotlin](https://img.shields.io/badge/Kotlin-2.3-7F52FF?style=flat-square&logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2026.01-4285F4?style=flat-square&logo=jetpackcompose)
![Android](https://img.shields.io/badge/Android-API%2024+-3DDC84?style=flat-square&logo=android)

</div>

---

## About

**Matule** is an Android application built on a clean modular architecture. The UI layer and networking layer are maintained as independent libraries connected via git submodules — each independently versioned, testable, and reusable. Analytics are powered by **AppMetrica**.

## Repository Structure

```
matule-app/
├── app/                  # Main application module
├── matule_ui_kit/        # ← git submodule (UI components & assets)
└── matule_network/       # ← git submodule (networking & API layer)
```

| Module | Repository | Role |
|---|---|---|
| `matule_ui_kit` | [matule-ui-kit](https://github.com/fadyMarty/matule-ui-kit) | Shared UI components, image loading, PDF viewer |
| `matule_network` | [matule-network](https://github.com/fadyMarty/matule-network) | HTTP client, Retrofit API, serialization |

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin 2.3 |
| UI | Jetpack Compose + Material 3 |
| Navigation | Navigation Compose |
| DI | Koin (compose-viewmodel-navigation) |
| Storage | DataStore Preferences |
| Analytics | AppMetrica |
| Networking | → matule_network submodule |
| UI Components | → matule_ui_kit submodule |
| Build | Gradle Kotlin DSL + Version Catalogs |

## Getting Started

### Prerequisites

- Android Studio Meerkat or later
- JDK 17+
- Android SDK (API 24+)

### Clone with submodules

```bash
git clone --recurse-submodules https://github.com/fadyMarty/matule-app.git
```

> Already cloned without submodules?
> ```bash
> git submodule update --init --recursive
> ```

### Build & Run

```bash
./gradlew assembleDebug
```

## Related Repositories

- 🎨 [matule-ui-kit](https://github.com/fadyMarty/matule-ui-kit) — UI component library
- 🌐 [matule-network](https://github.com/fadyMarty/matule-network) — Networking library
