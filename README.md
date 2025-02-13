# 2025sp-420-CrowdStrike

## Running tests
First you must download [junit](https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar) and place it in the lib directory.
[junit download](https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar)
### Linux/Macos
```sh
javac -d out/ src/*.java
javac -d out/ -cp lib/junit-platform-console-standalone-1.11.4.jar:out test/*.java
java -jar lib/junit-platform-console-standalone-1.11.4.jar execute -cp out --scan-class-path
```

### Windows
```sh
javac -d out/ src/*.java
javac -d out/ -cp lib/junit-platform-console-standalone-1.11.4.jar;out test/*.java
java -jar lib/junit-platform-console-standalone-1.11.4.jar execute -cp out --scan-class-path
```