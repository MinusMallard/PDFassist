# PDFassist 

**PDFassist** is a streamlined Windows application designed for efficient PDF reading and management. Built with a mix of **Kotlin** and **Java**, it utilizes a modern MVVM architecture to provide a smooth, tabbed browsing experience for your documents.

## Features

- **Tabbed Interface:** Manage and switch between multiple PDF documents easily using the custom TabBar.
- **Modern UI:** Built with Kotlin-based UI components for a native, responsive feel.
- **Robust File Handling:** Efficiently open, manage, and process local PDF files.
- **State Management:** Remembers your window state and settings across sessions.

## Project Structure

The project follows a modular directory layout for high maintainability:

```text
src
+---main
|   +---kotlin
|   |   |   Main.kt
|   |   |
|   |   +---components
|   |   |       FileHandler.java
|   |   |       PDF.java
|   |   |       Tab.java
|   |   |
|   |   +---ui
|   |   |       CurrentTabScreen.kt
|   |   |       SettingScreen.kt
|   |   |       TabBar.kt
|   |   |
|   |   \---viewmodels
|   |           FileHandlerViewModel.kt
|   |           TabBarViewModel.kt
|   |           ViewModel.kt
|   |           ViewModelProvider.kt
|   |           WindowStateManagement.kt
|   |
|   \---resources
```

## ⚙️ How It Works

The application follows the **MVVM (Model-View-ViewModel)** pattern to ensure a clean separation of concerns:

1.  **The Model (`components/`):** `FileHandler.java` and `PDF.java` handle the heavy lifting—interacting with the Windows file system and parsing PDF data. These are written in Java for robust performance.

2.  **The ViewModel (`viewmodels/`):** `FileHandlerViewModel.kt` acts as the brain. It calls the `FileHandler`, processes the data, and holds it in a "state" that the UI can observe. This ensures that if a file loads in the background, the UI updates automatically without freezing.

3.  **The View (`ui/`):** `CurrentTabScreen.kt` and `TabBar.kt` are purely visual. They observe the ViewModel and display the PDF content or switch tabs based on user interaction.

4.  **Window Management:** `WindowStateManagement.kt` ensures that when you resize or reopen the app, your experience remains consistent.

## 🛠 Tech Stack

- **Language:** Kotlin & Java (Hybrid)
- **Architecture:** MVVM (Model-View-ViewModel)
- **Platform:** Windows

## Key Components

| Module | Description |
| :--- | :--- |
| **View** (`ui/`) | Handles the visual layout using `CurrentTabScreen` and `TabBar`. |
| **ViewModel** (`viewmodels/`) | Manages the UI state and bridges data from the logic to the screens. |
| **Logic** (`components/`) | Contains the `FileHandler` and `PDF` classes for low-level file operations. |

## ⚙️ Getting Started

### Prerequisites
* JDK 17 or higher
* [IntelliJ IDEA](https://www.jetbrains.com/idea/) (Recommended)

### Installation
1. Clone the repository:
   ```bash
   git clone [https://github.com/MinusMallard/PDFassist.git](https://github.com/MinusMallard/PDFassist.git)
   ```
2. Open the project in IntelliJ IDEA.
3. Build the project using the built-in Gradle/Maven tool.
4. Run `Main.kt` to launch the application.

## Contributing

Contributions are welcome! If you'd like to improve the PDF rendering or add new UI features:
1. Fork the project.
2. Create your feature branch (`git checkout -b feature/NewFeature`).
3. Commit your changes (`git commit -m 'Add NewFeature'`).
4. Push to the branch (`git push origin feature/NewFeature`).
5. Open a Pull Request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

---
*Developed by [MinusMallard](https://github.com/MinusMallard)*
