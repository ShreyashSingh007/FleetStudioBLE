# FleetStudioBLE
assigment submission
## Overview

In general, the steps necessary to build the APK file:

1. [Install git]
2. [Install Android Studio]
3. [Set git path in Android Studio preferences]
4. [Download FleetStudioBLE code]
5. [Download Android Studio and Android SDK]
6. [Build the app]
7. [Transfer APK to mobile for testing]

## Step by step walkthrough

Detailed description of the steps necessary to build the APK file.

## Install git (if you don't have it)

Follow the manual on the [git installation page]

## Install Android Studio

The following screenshots have been taken from Android Studio Version Chipmunk | 2021.2.1. Screens can change in future versions of Android Studio. But you should be able to find your way through. [Help from the community](../Where-To-Go-For-Help/Connect-with-other-users.md) is provided.

One of the most important things when installing Android Studio: **Be patient!** During installation and setup Android Studio is downloading a lot of stuff which will take its time.

Download [Android Studio from here](https://developer.android.com/studio/install.html) and install it on your computer.

On first start you will find the setup wizard:

Select "Do not import settings" as you have not used it before.

   ![Do not import settings]

   ![Standard installation]

Select the theme for the user interface you like. (In this manual we used "Light".) Then click "Next".

> **_Note:_**  This is just the color scheme. You can select whatever you like (i.e. "Darcula" for dark mode).
This selection has no influence on building the APK but the following screenshots might look different.


Click "Finish" on the "Verify Settings" dialog.



Wait while Android Studio downloads additional components and be patient. Once everything is downloaded button "Finish" turns blue. Click the button now.

   ![Downloading components]


## Set git path in preferences

Make sure [git is installed]

On the Android Studio welcome screen click "Customize" (1) on the left and then select the link  "All settings..." (2):



## Download Android SDK

* In the menu, go to  File (1) > Settings (2).

* Double-click on Appearance & Behaviour to open its submenu (1).
* Double-click on System Settings (2) and select Android SDK (3).
* Tick the box left of "Android" (Latest Version)
* Confirm changes by clicking OK.
* Accept licence agreement (1) and click "Next" (2).
* Wait until the SDK download and installation is finished.
* When SDK installation is completed the "Finish" button will turn blue. Click this button.
* Android Studio might recommend to update the gradle system. **Never update gradle!** This will lead to difficulties!
* Restart Android Studio before you continue.


## Transfer APK to smartphone

Easiest way to transfer app-full-release.apk to your phone is via [USB cable or Google Drive](https://support.google.com/android/answer/9064445?hl=en). Please note that transfer by mail might cause difficulties and is not the preferred way.

On your phone you have to allow installation from unknown sources. Manuals how to do this can be found on the internet (i.e. [here](https://www.expressvpn.com/de/support/vpn-setup/enable-apk-installs-android/) or [here](https://www.androidcentral.com/unknown-sources)).
