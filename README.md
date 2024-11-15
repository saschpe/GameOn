# GameOn

[![Build Status](https://github.com/saschpe/GameOn/workflows/Main%20CI/badge.svg)](https://github.com/saschpe/GameOn/actions)
[![Security](https://github.com/saschpe/GameOn/actions/workflows/security.yml/badge.svg)](https://github.com/saschpe/GameOn/actions/workflows/security.yml)
![badge-android](http://img.shields.io/badge/Platform-Android-brightgreen.svg?logo=android)

#### Get games on sale.

Search for games on sale across many online stores, find daily deals and remember favorites.
You'll get notified once your desired price is reached.

- Unlock game keys for Steam, GOG.com or DRM-free
- Search across more than thirty online game stores
- Check reviews, perks and Linux support via Proton.DB

This shopping app aggregates and displays digital game content from third-party online stores.
Purchasing any offer happens on the aforementioned third-party online stores.
All content images and descriptions are copyrighted material and belong to their respective owners.

[![Google Play](https://lh5.googleusercontent.com/ZRRDXkwmBGwDtTNbd-ueKZTRUIyOxeVFV6Fc9DfsIMpByjYEz-7E8f6EjXkaFFn9jQtEPaIFcjEt0YzOJCs7Y-enK8SgjElpLftHrZam3XGfwYKv=w1280)](https://play.google.com/store/apps/details?id=saschpe.gameon)

# Development

The project is using Gradle, which can be invoked on the command-line or with _Android Studio_ or _IntelliJ_.
You should use [Jetbrains Toolbox][1] to install and keep them updated.

## Dependencies

This project depends on Java on the host machine at build-time.
Thus, install OpenJDK 21 on Linux or [Oracle JDK 21][2] on macOS or Windows.
In case you already have the latest version installed on macOS, you can set the default version
explicitly by setting the __JAVA_HOME__ environment variable:

```bash
export JAVA_HOME=`/usr/libexec/java_home -v 21`
```

## Building

Either open the project with an IDE or build it on the command-line. The Gradle
task _build_ assembles the sources, runs all code checks (such as Android Lint)
and runs all unit tests in the project:

```bash
./gradlew build
```

Running integration tests depends on either a physical device attached, or a
running Android Emulator instance:

```bash
./gradlew connectedTest
```

# IsThereAnyDeal API

See: https://itad.docs.apiary.io/
App: https://isthereanydeal.com/dev/app/501/

# License

    Copyright (C) 2019 Sascha Peilicke. All rights reserved.

[1]: https://www.jetbrains.com/toolbox/

[2]: https://www.oracle.com/technetwork/java/javase/downloads/index.html#JDK8
