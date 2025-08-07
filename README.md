<a id="readme-top"></a>

[![Contributors][contributors-shield]][contributors-url]
[![Unlicense License][license-shield]][license-url]
[![Last Commit][last-commit-shield]][last-commit-url]


<div align="center">
  <h1 align="center">EVENTORIUM</h1>
  <p align="center">
    <br />
    <a href="https://github.com/natasakasikovic/eventorium-mobile/issues/new?labels=bug">Report Bug</a>
    <a href="https://www.youtube.com/watch?v=QiGeygigk-w">Demo</a>
  </p>
</div>


<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#-about-the-project">About The Project</a>
      <ul>
        <li><a href="#-built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#-getting-started">Getting Started</a>
      <ul>
        <li><a href="#-prerequisites">Prerequisites</a></li>
        <li><a href="#-installation-steps">Installation Steps</a></li>
      </ul>
    </li>
    <li><a href="#-available-roles-and-credentials">Available Roles and Credentials</a></li>
  </ol>
</details>




## 📋 About The Project

Welcome to the Eventorium project! This is the mobile application for Eventorium, an event planning platform designed to streamline the organization of various events such as weddings, corporate gatherings, and birthday parties. Our mobile app, built for Android, offers a seamless and user-friendly experience to help you plan your events with ease.

Happy planning with Eventorium! 🎉

</br>

### 🔧 Built With

This project is built using the following core technologies:

 [![Android][android-shield]][android-url]

 [![Java][java-shield]][java-url]

 [![Android Studio][studio-shield]][studio-url]

 [![Gradle][gradle-shield]][gradle-url]

 [![XML][xml-shield]][xml-url]

## 🚀 Getting Started

Follow the steps below to set up and run the project locally.

### 📦 Prerequisites

Before you begin, ensure you have the following installed:

- 🔧 Java Development Kit (JDK) – Version 17 or later
- 📱 Android Studio – Latest stable version recommended
- 📦 Gradle – Installed via Android Studio or separately (usually handled automatically)
- 📲 Android device or emulator for testing

Optional:

- 🧪 USB Debugging enabled on your Android device (for real device deployment)


### 🛠️ Installation Steps

1. 🔁 Clone the repository

```sh
git clone https://github.com/natasakasikovic/eventorium-mobile.git
```

2. 🧰 Open the project in Android Studio

- Launch Android Studio
- Click "Open"
- Select the `eventorium-mobile` directory

3. ⏳ Wait for Gradle to sync and build
4. 🔐 Update `local_properties` with the (backend) `ip_addr` and `secret`.
- Make sure to use the same `secret` value that's defined in the backend configuration. 
5. 📱 Run the application

- Connect a physical Android device (with USB Debugging enabled) or start an emulator.
- Click "Run" (green play button) or use:

```sh
./gradlew installDebug
```

## 👥 Available Roles and Credentials

The system supports the following roles with corresponding credentials:

- **Organizer**
  - Email: `organizer@gmail.com`
  - Password: `pera`

- **Service and product provider**
  - Email: `provider@gmail.com`
  - Password: `pera`

- **Administrator**
  - Email: `admin@gmail.com`
  - Password: `pera`

- **User (registered via quick registration)**
  - Email: `user@gmail.com`
  - Password: `pera`


<p align="right">(<a href="#readme-top">back to top</a>)</p>

## 📸 Screenshots

#### 🏠 Home Page and Login page

<div align="center" style="overflow: hidden;">
  <img src="./screenshots/0. login.jpg" alt="Login" width="400" height="auto"/>
  <img src="./screenshots/9. homepage.jpg" alt="Homepage" width="400" height="auto"/>
</div>

#### 📆 Event Details View

<div align="center" style="overflow: hidden;">
  <img src="./screenshots/8. event_details.jpg" alt="Event details" width="400" height="auto"/>
  <img src="./screenshots/8. agenda_map.jpg" alt="Agenda and map" width="400" height="auto"/>
</div>

#### 👤 User profile view

<div align="center" style="overflow: hidden;">
  <img src="./screenshots/15. profile.jpg" alt="Event details" width="400" height="auto"/>
  <img src="./screenshots/16. edit_profile.jpg" alt="Agenda and map" width="400" height="auto"/>
</div>

#### 📁 More Screenshots

You can view additional screenshots in the [screenshots](./screenshots) folder.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

[contributors-shield]: https://img.shields.io/github/contributors/natasakasikovic/eventorium-mobile.svg?style=for-the-badge
[contributors-url]: https://github.com/natasakasikovic/eventorium-mobile/graphs/contributors
[license-shield]: https://img.shields.io/github/license/natasakasikovic/eventorium-mobile.svg?style=for-the-badge
[license-url]: https://github.com/natasakasikovic/eventorium-mobile/blob/master/LICENSE.txt
[android-shield]: https://img.shields.io/badge/Android-API%2034-3DDC84?logo=android&logoColor=white
[java-shield]: https://img.shields.io/badge/Java-18-red?logo=java&logoColor=white
[studio-shield]: https://img.shields.io/badge/Android%20Studio-Hedgehog-FD6F00?logo=ladybug&logoColor=white  
[gradle-shield]: https://img.shields.io/badge/Gradle-8.9-02303A?logo=gradle&logoColor=white
[xml-shield]: https://img.shields.io/badge/XML-Layouts-FF6600?logo=xml&logoColor=white
[android-url]: https://developer.android.com/about/versions/14  
[java-url]: https://www.oracle.com/java/technologies/javase/jdk18-archive-downloads.html  
[studio-url]: https://developer.android.com/studio/archive#hedgehog  
[gradle-url]: https://docs.gradle.org/8.9/release-notes.html  
[xml-url]: https://www.w3.org/XML/
[last-commit-shield]: https://img.shields.io/github/last-commit/kzi-nastava/ma-project-event-planner-siit-2024-team-13?branch=main&style=for-the-badge
[last-commit-url]: https://github.com/kzi-nastava/ma-project-event-planner-siit-2024-team-13/commits/main
