FROM bellsoft/liberica-openjdk-alpine:17 as build
WORKDIR /workspace/app

# Copy Gradle wrapper and other necessary files
COPY build/libs/ .

# Unpack the built application
RUN mkdir -p target/extracted
RUN java -Djarmode=layertools -jar *.jar extract --destination target/extracted

FROM bellsoft/liberica-openjdk-alpine:17
VOLUME /tmp
ARG EXTRACTED=/workspace/app/target/extracted

# Copy over the unpacked application
COPY --from=build ${EXTRACTED}/dependencies/ ./
COPY --from=build ${EXTRACTED}/spring-boot-loader/ ./
COPY --from=build ${EXTRACTED}/snapshot-dependencies/ ./
COPY --from=build ${EXTRACTED}/application/ ./

ENTRYPOINT ["java","-Dspring.profiles.active=dev","org.springframework.boot.loader.JarLauncher"]