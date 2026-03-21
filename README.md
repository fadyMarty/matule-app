<div align="center">

# Matule

**A modern Android application built with Kotlin**

![Kotlin](https://img.shields.io/badge/Kotlin-2.x-7F52FF?style=flat-square&logo=kotlin)
![Android](https://img.shields.io/badge/Android-API%2024+-3DDC84?style=flat-square&logo=android)

</div>

---

## About

**Matule** is an Android application structured around a clean modular architecture. The UI layer and networking layer are maintained as independent libraries and connected via git submodules — making each component independently versioned, testable, and reusable.

## Repository Structure

```
matule-app/
├── app/                  # Main application module
├── matule_ui_kit/        # ← git submodule (UI components)
└── matule_network/       # ← git submodule (networking layer)
```

| Module | Repository | Role |
|---|---|---|
| `matule_ui_kit` | [matule-ui-kit](https://github.com/fadyMarty/matule-ui-kit) | Shared UI components & theme |
| `matule_network` | [matule-network](https://github.com/fadyMarty/matule-network) | HTTP client & API layer |

## Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- JDK 17+
- Android SDK (API 24+)

### Clone with submodules

```bash
git clone --recurse-submodules https://github.com/fadyMarty/matule-app.git
```

> If you already cloned without submodules:
> ```bash
> git submodule update --init --recursive
> ```

### Build & Run

Open the project in Android Studio and run the `app` configuration, or use Gradle:

```bash
./gradlew assembleDebug
```

## ✦ Related Repositories

- 🎨 [matule-ui-kit](https://github.com/fadyMarty/matule-ui-kit) — UI component library
- 🌐 [matule-network](https://github.com/fadyMarty/matule-network) — Networking library
