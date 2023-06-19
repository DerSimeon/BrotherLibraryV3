<h1 align="center">BrotherLib</h1>

# Prerequisites

* Java (19+)
* Libusb (see #libusb)

# Libusb

## Windows

* Since the LibUSB Drivers can be pretty tricky to install, I recommend using Zadig to replace the default Brother Driver.
  * Zadig can be found [here](https://zadig.akeo.ie) and is pretty easy to use.
  * Just press "Options" and select "List All Devices" and then select your printer.
  * on the right side, select "libusb-win32 (v1.2.6.0)" and press "Replace Driver".
  * After that, you should be able to use the printer with this library.

## Linux

* most distributions should have libusb preinstalled, if not, visit [libusb.info](https://libusb.info) for more information.

## MacOS

* Use homebrew to install libusb:
  * `brew install libusb`
* NOTE: If you encounter an error saying you are missing a native "darwin-aarch64" Library, your Laptop is incompatible.

# Development

* In order to work with this Library, you need to add the following information to your build.gradle.kts:
  * `repositories { maven("https://repo.simeon.lol/snapshots") }`
  * `dependencies { implementation("lol.simeon:BrotherLibV3:1.0-SNAPSHOT") }`