# 2025sp-420-CrowdStrike

## Running tests
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