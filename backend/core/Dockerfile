FROM openjdk:17-ea-slim as build
WORKDIR /workspace/app

# Copy Gradle wrapper and other necessary files
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

RUN apt-get update && apt-get install -y findutils

# Execute the Gradle build
RUN ./gradlew build -x test

# Unpack the built application
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../../build/libs/*.jar)

FROM openjdk:17-ea-slim
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency

# Copy over the unpacked application
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# Run the application
ENTRYPOINT ["java","-cp","app:app/lib/*","site.timecapsulearchive.core.CoreApplication"]
