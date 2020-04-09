# GameOn
TODO

# Development
The project is using Gradle, which can be invoked on the command-line or
with _Android Studio_ or _IntelliJ_. You should use [Jetbrains Toolbox][1]
to install and keep them updated.

## Dependencies
This project depends on Java on the host machine at build-time. Thus, install
OpenJDK 9 or 11 on Linux or [Oracle JDK 11][2] on macOS or Windows. In case you
already have a later version installed on macOS you can set the default version
explicitly by setting the __JAVA_HOME__ environment variable:

```bash
export JAVA_HOME=`/usr/libexec/java_home -v 11`
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
