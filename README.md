# 🌌 ZeroG — Anti-Gravity Weight Simulator

> **Course:** Mobile Applications Development 2025 | **Student ID:** 2301681045
> **Platform:** Android (min SDK 24 / Android 7.0) | **Language:** Kotlin

---

## 💡 Idea

ZeroG is an educational Android calculator that answers one question:
**"How much would this object weigh if gravity were partially or completely removed?"**

The app lets you enter any object's Earth weight and dial in an anti-gravity percentage using a slider. It instantly shows the reduced weight, lets you save calculations to a personal log, and share results with others. The concept is grounded in real physics used by NASA engineers and science communicators to explain reduced-gravity environments (the Moon = ~17% gravity, Mars = ~38% gravity).

Despite being a simple numbers app, ZeroG demonstrates a complete Android production architecture — Room database, MVVM + Repository pattern, Jetpack Navigation Component, share intents, and Material 3 theming — making it an ideal coursework project.

---

## ⚙️ How It Works

### The Formula

$$\text{Weight}_{\text{Zero-G}} = \text{Weight}_{\text{Earth}} \times \left(1 - \frac{\text{Reduction \%}}{100}\right)$$

| Example | Earth Weight | Reduction | Zero-G Weight |
|---------|-------------|-----------|---------------|
| Car | 1 500 kg | 80 % | **300 kg** |
| Person | 80 kg | 83 % (Moon) | **13.6 kg** |
| Feather | 0.005 kg | 100 % | **0 kg** |

### Feature Overview

| Feature | Implementation |
|---------|---------------|
| Weight calculation | Formula applied in `CalculatorFragment` on button click |
| Gravity slider | Material 3 `Slider` (0 – 100 %) |
| Save to log | Room `INSERT` via `GravityViewModel` |
| Edit log entry | `AlertDialog` + Room `UPDATE` |
| Delete log entry | Trash icon / long-press → Room `DELETE` |
| Share result | `Intent.ACTION_SEND` (plain text) |
| Persistent storage | SQLite via Room ORM |
| Reactive UI | `StateFlow` → `repeatOnLifecycle` |

---

## 🏛️ Architecture

The app follows **Clean MVVM** with a Repository layer:

```
UI Layer
├── MainActivity.kt              — Single Activity host
├── CalculatorFragment.kt        — Screen 1: simulator + share intent
├── HistoryFragment.kt           — Screen 2: CRUD list
└── GravityLogAdapter.kt         — RecyclerView ListAdapter

ViewModel Layer
└── GravityViewModel.kt          — AndroidViewModel, exposes StateFlow<List<GravityLog>>

Repository Layer
└── GravityRepository.kt         — Abstracts DAO from ViewModel

Data Layer (Room)
├── GravityLog.kt                — @Entity
├── GravityLogDao.kt             — @Dao (Insert / getAll Flow / Update / Delete)
└── GravityDatabase.kt           — @Database singleton

Navigation
└── nav_graph.xml                — Jetpack Navigation Component
└── bottom_nav_menu.xml          — BottomNavigationView (2 tabs)
```

### Tech Stack

| Component | Library / Version |
|-----------|------------------|
| Language | Kotlin |
| UI | Android Views + Material 3 |
| Navigation | Jetpack Navigation Component 2.9.0 |
| Database | Room 2.7.1 |
| Annotation Processing | KSP 2.3.8 |
| Async | Kotlin Coroutines + Flow |
| Code Style | ktlint 12.1.2 |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 36 |

---

## 🚀 User Flow

```
App Launch
    │
    ▼
┌─────────────────────────────┐
│     SIMULATOR SCREEN        │  ← Default tab
│                             │
│  [Item Name Input]          │
│  [Earth Weight (kg) Input]  │
│  [Anti-Gravity Slider 0-100%│
│                             │
│  ┌─────────────────────┐    │
│  │   [ CALCULATE ]     │    │  ← Validates inputs, applies formula
│  └─────────────────────┘    │
│                             │
│  ╔═════════════════════╗    │
│  ║  Zero-G Weight:     ║    │  ← Result card appears
│  ║  300.0000 kg        ║    │
│  ╚═════════════════════╝    │
│                             │
│  [ Save to Log ]            │  ← INSERT into Room DB
│  [ Share with Crew ]        │  ← Share Intent (plain text)
└─────────────────────────────┘
    │
    │  Tap "Log" tab
    ▼
┌─────────────────────────────┐
│      LOG HISTORY SCREEN     │
│                             │
│  ┌─────────────────────┐    │
│  │ 🚗 My Car           │    │
│  │ 1500 kg → 300 kg    │ 🗑 │  ← Tap card → Edit name (UPDATE)
│  │ Reduction: 80%      │    │  ← Long-press / 🗑 → Delete (DELETE)
│  └─────────────────────┘    │
│  ┌─────────────────────┐    │
│  │ 🧑 Person           │    │
│  │ 80 kg → 13.6 kg     │ 🗑 │
│  │ Reduction: 83%      │    │
│  └─────────────────────┘    │
└─────────────────────────────┘
```

---

## 📲 Steps to Launch

### Prerequisites
- Android Studio Meerkat (2024.3) or later
- Android SDK 36 installed
- A physical device (Android 7.0+) or AVD emulator

### Option A — Run from Android Studio (Recommended)
1. Clone or download the repository
2. Open **Android Studio → Open → select the `ZeroG/` folder**
3. Wait for Gradle sync to complete (~1–2 min on first run)
4. Select your device/emulator in the toolbar
5. Press **▶ Run** (`Shift+F10`)

### Option B — Install the Release APK directly
1. Copy `apk/app-release.apk` to your Android device
2. On the device: **Settings → Security → Install Unknown Apps → Allow**
3. Open a file manager, navigate to the APK, tap to install
4. Launch **ZeroG** from the app drawer

### Option C — Build from Terminal
```bash
# Windows (PowerShell)
.\gradlew.bat assembleRelease

# macOS / Linux
./gradlew assembleRelease
```
Output APK: `app/build/outputs/apk/release/app-release.apk`

---

## 🧪 Testing

### Unit Tests (JVM — no device needed)
- **File:** `app/src/test/java/com/example/zerog/GravityCalculationTest.kt`
- **16 tests** covering the Zero-G formula and `GravityLog` entity
- Run via Android Studio: right-click file → **Run 'GravityCalculationTest'**

### UI / Instrumented Test (Espresso)
- **File:** `app/src/androidTest/java/com/example/zerog/MainActivityTest.kt`
- **1 test** covering the full calculate → save → navigate → assert flow
- Run via Android Studio with a connected device/emulator

### Code Style
```bash
.\gradlew.bat ktlintCheck   # Verify style
.\gradlew.bat ktlintFormat  # Auto-fix style issues
```

---

## 📸 Screenshots

> _Replace the placeholders below with actual screenshots after running the app._

| Simulator Screen | Result Card | Log History | Edit Dialog |
|-----------------|-------------|-------------|-------------|
| ![Simulator](screenshots/screen_simulator.png) | ![Result](screenshots/screen_result.png) | ![History](screenshots/screen_history.png) | ![Edit](screenshots/screen_edit.png) |

---

## 📁 Project Structure

```
ZeroG/
├── apk/
│   └── app-release.apk              ← Compiled release APK
├── app/
│   └── src/
│       ├── main/
│       │   ├── java/com/example/zerog/
│       │   │   ├── data/local/      ← Room Entity, DAO, Database
│       │   │   ├── data/repository/ ← Repository
│       │   │   ├── ui/viewmodel/    ← ViewModel
│       │   │   ├── MainActivity.kt
│       │   │   ├── CalculatorFragment.kt
│       │   │   ├── HistoryFragment.kt
│       │   │   └── GravityLogAdapter.kt
│       │   └── res/
│       │       ├── layout/          ← XML layouts
│       │       ├── navigation/      ← Nav graph
│       │       ├── menu/            ← Bottom nav menu
│       │       └── values/          ← Themes, strings, colors
│       ├── test/                    ← JUnit unit tests
│       └── androidTest/             ← Espresso UI tests
├── gradle/libs.versions.toml        ← Version catalog
└── README.md
```

---

## 👤 Author

| Field | Value |
|-------|-------|
| Name | _(your name)_ |
| Student ID | 2301681045 |
| Course | Mobile Applications Development 2025 |
| University | _(your university name)_ |

---

*Generated with Android Studio · Kotlin · Room · Jetpack Navigation · Material 3*
