# Tier List Maker Ultimate - RuntimeTerrors (A02-G11)

## Project Directory

Project documentation and standards:

- **[Vision Statement](docs/VISION.md)**: Detailed project vision statement.
- **[Architecture Documentation](docs/ARCHITECTURE.md)**: System architectural patterns.
- **[Coding Standards](docs/PROGRAMMING_STANDARDS.md)**: Programming guidelines.
- **[Version Control Standards](docs/VERSION_CONTROL_STANDARDS.md)**: Git workflow
  standards.

---

## SDK & Tooling Requirements

### Android SDK

- `compileSdk = 35`
- `targetSdk = 35`
- `minSdk = 34`

### Java

- **Java 17**
- JDK 17 or higher must be installed
- Kotlin is **not used** in this project

### Tools

- Android Studio (current stable)
- Android Emulator (Pixel 9 device profile recommended)
- Gradle Wrapper (included)

---

## Setup & Execution

1. **Clone the Repository**:
   ```bash
   git clone https://code.cs.umanitoba.ca/comp3350-winter2026/a02-g11-runtimeterrors
   ```
2. **Open in Android Studio**: Select the root directory of the project.
3. **Sync Gradle**: download dependencies.
4. **Run**: Use the `app` run configuration to run the app.

---

## Dependencies

The project relies on the following libraries:

* **AndroidX & Material Components**: For UI elements like ConstraintLayout.
* **JUnit 5 (Jupiter)**: For JVM unit testing.*
* **Espresso**: For UI testing on emulators.

---

## Architecture & Package Structure

This project enforces strict separation of concerns.

```
app.TierListMakerUltimate
│
├── presentation/      // Android UI (Activities, Adapters, UI logic)
│
├── business/           // Domain logic, services, validation
│
├── persistence/        // Repository interfaces + implementations
│   ├── fake/           // In-memory repositories (Hashmap)
│   └── real/           // SQLite repositories
│
├── models/             // Plain data objects (Item, Keyword)
│
└── application/        // Composition root (wiring dependencies)

```

### Dependency Rules

- `presentation → business → persistence`
- **No Android imports** (`android.*`, `androidx.*`) are allowed in the `business` or `persistence`
- Android-specific code belongs strictly in the `presentation` and `application` layers.

---

## Testing Strategy

### 1. Unit Tests (JVM)

**Location:** `app/src/test/java`

- Focuses on `business`
- Uses **JUnit 5**
- Runs against `persistence/stubs` to avoid external dependencies.
- **Run with:** `./gradlew testDebugUnitTest`

---

