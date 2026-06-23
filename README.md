<div align="center">
  <img src="docs/icon.svg" width="80" alt="LocalChat Logo">
  <h1>LocalChat</h1>
  <p><strong>On-device LLM Chatbot · Powered by Gemini Nano via Android AICore</strong></p>
  <p>
    <a href="#features"><img src="https://img.shields.io/badge/🤖%20AI-Gemini%20Nano-4285F4?style=flat-square" alt="AI"></a>
    <a href="#tech-stack"><img src="https://img.shields.io/badge/📱%20Platform-Android-34A853?style=flat-square" alt="Platform"></a>
    <a href="https://kotlinlang.org"><img src="https://img.shields.io/badge/Kotlin-2.3.21-7F52FF?style=flat-square&logo=kotlin" alt="Kotlin"></a>
    <a href="https://developer.android.com/jetpack/compose"><img src="https://img.shields.io/badge/Compose-Material%203-4285F4?style=flat-square" alt="Compose"></a>
    <a href="LICENSE"><img src="https://img.shields.io/badge/License-MIT-yellow?style=flat-square" alt="License"></a>
    <a href="https://github.com/features/actions"><img src="https://img.shields.io/badge/CI/CD-GitHub%20Actions-2088FF?style=flat-square&logo=githubactions" alt="CI/CD"></a>
  </p>
</div>

---

**LocalChat** is a fully offline, on-device AI chatbot for Android that runs **Gemini Nano** directly on your phone's hardware via **Android AICore** — no cloud calls, no API keys, no internet required. Every message you send stays on your device, processed locally by the NPU or CPU.

---

## ✨ Features

- **🔒 100% Private** — All inference runs locally. Zero data leaves your device.
- **⚡ No Internet Required** — Works completely offline once the model is downloaded.
- **🧠 Gemini Nano via AICore** — Leverages Google's on-device foundation model.
- **🎨 Material Design 3** — Modern UI with dynamic color (Material You) support.
- **📱 Jetpack Compose** — Declarative, reactive UI built entirely with Compose.
- **⬇️ Auto-Download** — Checks model availability and downloads Gemini Nano if needed.
- **📋 Status Feedback** — Real-time model status (checking, downloading, available, unavailable).
- **🌙 Dark Mode** — Automatic theme switching based on system settings.
- **🆓 Free & Open Source** — No subscriptions, no API costs, forever.

## 🖼️ Screenshots

| Chat Screen | Model Downloading | Dark Mode |
|:---:|:---:|:---:|
| *screenshot_1.png* | *screenshot_2.png* | *screenshot_3.png* |

> **Note:** Add screenshots to the `docs/` folder and update the table above.

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| **Language** | [Kotlin](https://kotlinlang.org) 2.3.21 |
| **UI Framework** | [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material Design 3 |
| **Theming** | Dynamic Color (Material You) via `MaterialTheme.colorScheme` |
| **Architecture** | Single Activity + `ViewModel` + `StateFlow` |
| **AI Inference** | [ML Kit GenAI Prompt API](https://developers.google.com/ml-kit/genai/prompt/android) `1.0.0-beta2` |
| **On-Device Model** | Gemini Nano via `com.google.android.aicore` |
| **Min SDK** | API 26 (Android 8.0) |
| **Target SDK** | API 36 (Android 15) |
| **Build System** | Gradle 8.12 + Android Gradle Plugin 8.7.3 |
| **CI/CD** | GitHub Actions — automated APK build & release |

## 📦 Installation

### Prerequisites

- Android device with **AICore support** (see [device compatibility](#-device-compatibility))
- Android 14+ recommended

### Download APK

1. Go to the [Releases](https://github.com/yourusername/localchat/releases) page.
2. Download the latest `app-debug.apk`.
3. Open it on your device to install.

### Build from Source

```bash
git clone https://github.com/yourusername/localchat.git
cd localchat
./gradlew assembleDebug
```

The APK will be at `app/build/outputs/apk/debug/app-debug.apk`.

## 🚀 Usage

1. Open **LocalChat** on your device.
2. The app checks for Gemini Nano availability automatically.
   - **Available:** Start chatting immediately.
   - **Downloadable:** Tap send to download the model (one-time).
   - **Unavailable:** Your device doesn't support AICore yet.
3. Type your message and tap the send button.
4. Gemini Nano processes your request entirely on-device.

## 🤖 How It Works

```
┌─────────────────────────────────────────────────────┐
│                  Your Android Device                  │
│                                                       │
│  ┌──────────────┐     ┌───────────────────────────┐  │
│  │  LocalChat    │────▶│  ML Kit GenAI Prompt API  │  │
│  │  (Compose UI) │◀────│  (com.google.mlkit)       │  │
│  └──────────────┘     └───────────┬───────────────┘  │
│                                   │                   │
│                          ┌────────▼───────────────┐  │
│                          │  Android AICore         │  │
│                          │  (com.google.aicore)    │  │
│                          └────────┬───────────────┘  │
│                                   │                   │
│                          ┌────────▼───────────────┐  │
│                          │  Gemini Nano            │  │
│                          │  (On-Device NPU/CPU)    │  │
│                          └────────────────────────┘  │
│                                                       │
│  ───── No Internet · No API Calls · No Cloud ─────   │
└─────────────────────────────────────────────────────┘
```

## 📱 Device Compatibility

Gemini Nano via AICore requires specific hardware.

### Confirmed Supported Devices

| Brand | Models |
|-------|--------|
| **Google** | Pixel 8, Pixel 8 Pro, Pixel 9, Pixel 9 Pro, Pixel 9 Pro XL, Pixel 9 Pro Fold |
| **Samsung** | Galaxy S24, S24+, S24 Ultra, S25, S25+, S25 Ultra, Z Fold6, Z Flip6 |
| **Xiaomi** | Xiaomi 14T Pro, Xiaomi 15, Xiaomi 15 Ultra |
| **OnePlus** | OnePlus 13 |
| **Others** | Honor, OPPO, vivo, Motorola (select models with AICore) |

> Check [Google's official compatibility list](https://developers.google.com/ml-kit/genai) for the latest updates.

## 🏗️ Project Structure

```
localchat/
├── app/
│   ├── src/main/
│   │   ├── java/com/localchat/app/
│   │   │   ├── MainActivity.kt      # Compose UI — Scaffold, ChatBubble, MessageInput
│   │   │   ├── ChatViewModel.kt     # AICore integration, model state, message logic
│   │   │   └── ui/theme/
│   │   │       ├── Theme.kt         # Material 3 theme (light/dark/dynamic)
│   │   │       ├── Color.kt         # Custom color definitions
│   │   │       └── Type.kt          # Typography scale
│   │   ├── AndroidManifest.xml
│   │   └── res/
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── docs/
│   └── index.html                   # Project landing page
├── .github/workflows/
│   └── release.yml                  # CI/CD pipeline for APK
├── build.gradle.kts                 # Root build config
├── settings.gradle.kts
├── gradle.properties
└── README.md
```

## 🤝 Contributing

Contributions are welcome! Here's how you can help:

1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/amazing-feature`).
3. Commit your changes (`git commit -m 'Add amazing feature'`).
4. Push to the branch (`git push origin feature/amazing-feature`).
5. Open a Pull Request.

Please ensure your code follows the existing style and passes the build.

## 🚢 How to Release

Creating a new release is a **two-step** process:

### Step 1 — Bump the version

Edit `app/build.gradle.kts` and update the `versionName`:

```kotlin
defaultConfig {
    versionCode = 2          // increment for each release
    versionName = "1.1.0"    // update to the new version
}
```

### Step 2 — Commit with `#go`

Commit your changes with a message that **must contain `#go`** anywhere in the commit text:

```bash
git add .
git commit -m "fix: resolve input lag #go"
git push origin main
```

The CI/CD pipeline will automatically:
1. Detect `#go` in the commit message.
2. Read the version from `app/build.gradle.kts`.
3. Build the debug APK.
4. Create a Git tag (`v1.1.0`).
5. Publish a GitHub Release with the APK attached.

> **Note:** No keystore signing is required — the debug APK is released as-is. This is fine for open-source projects. Users install it as a debug build.

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2025 LocalChat Contributors

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
