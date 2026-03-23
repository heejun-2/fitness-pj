# [1] Build Stage
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle* settings.gradle* ./
RUN chmod +x ./gradlew

COPY src src

RUN ./gradlew clean build -x test
RUN ls -al /app/build/libs
RUN jar tf /app/build/libs/app.jar | grep BOOT-INF | head -n 5


# [2] Run Stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/app.jar app.jar
EXPOSE 8080
CMD ["sh","-c","java -Dserver.port=${PORT:-8080} -Dserver.address=0.0.0.0 -jar app.jar"]


