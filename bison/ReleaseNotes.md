
## Release Notes for Version 2.5.3.5


The goal of this version is to prevent as many of the home-made illegal accesses as possible.
As a consequence, jython seamlessly starts under JDK 17:

```
$ java -version
openjdk version "17.0.2" 2022-01-18
OpenJDK Runtime Environment Temurin-17.0.2+8 (build 17.0.2+8)
OpenJDK 64-Bit Server VM Temurin-17.0.2+8 (build 17.0.2+8, mixed mode, sharing)

$ java -Dpython.launcher.tty=true -jar jython-2.5.3.5.jar 
Jython 2.5.3.5 (, May 18 2022, 23:14:52) 
[OpenJDK 64-Bit Server VM (Eclipse Adoptium)] on java17.0.2
Type "help", "copyright", "credits" or "license" for more information.
>>> exit()
$ 
```

The main change was to add a heuristic approach (`AccessibleSupport`) to prevent illegal accesses on different levels.
Calls to methods and members of `sun.nio.*` classes are now blocked by default.

This prevents (amongst others) the following modules:
 - `select`
 - `signal`
 - `socket`
 
To workaround this, you need to add all the necessary `--add-opens` flags, plus the system property `-Dpython.allow.illegal.method.calls=true` when starting the JVM.


Some smaller changes are:
 - Use default platform encodings (`StandardCharsets.ISO_8859_1` for Windows, `StandardCharsets.UTF_8` for all other platforms)
 - Use [guava-7.0.1](https://github.com/ohumbel/guava/releases/tag/v7.0.1)
 - Detect `isatty()` solely by using the value of the system property `-Dpython.launcher.tty` which defaults to `false`.
   For an interactive console, `-Dpython.launcher.tty=true` is now mandatory
 - Implement some `posixpath` methods in pure Java, with slightly reduced functionality

And - last but not least - `jython-2.5.3.5.jar` now has a ``Main-Class: org.python.util.jython` attribute in its `MANIFEST.MF` again.