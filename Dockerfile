FROM openjdk:8-jdk-slim as build
ENV APP_HOME=/app/
WORKDIR $APP_HOME
COPY src $APP_HOME/src
COPY input $APP_HOME/input
COPY gradle $APP_HOME/gradle
COPY gradlew build.gradle run.sh build.sh settings.gradle $APP_HOME
RUN ./gradlew clean build


FROM openjdk:8-jre-slim
ENV APP_HOME=/app/
WORKDIR $APP_HOME
COPY --from=build /app/build/libs/puzzle-1.0.jar $APP_HOME
ENTRYPOINT ["java", "-jar", "puzzle-1.0.jar"]