## Tf-Idf

### Build the Project
```
./gradlew clean build
```

or with Docker

```
./build.sh
```

#### Run the tool

```
java -jar build/libs/puzzle-1.0.jar -d "input" -n 3 -t "gato calidad"
```

or with Docker. Caveat: only works with relative paths

```
./run.sh  -d "input"  -n 3 -t "gato calidad"
```

#### Help

You can get the help screen with:
```
 java -jar  ./build/libs/puzzle-1.0.jar --help
```
or with Docker. 

```
./run.sh --help
```