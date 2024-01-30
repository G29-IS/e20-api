FROM gradle:8.5.0-jdk21 as builder
WORKDIR /etc/e20-api
COPY . .
USER root
# Create the shadowjar (chmod +x makes the gradlew script executable)
RUN chmod +x ./gradlew
RUN ./gradlew shadowJar --no-daemon

FROM gradle:8.5.0-jdk21
WORKDIR /opt/e20-api
# Copy the shadowjar in the current workdir
COPY --from=builder ./etc/e20-api/build/libs/ .
# Entrypoint is used instead of CMD because the image is not intended to run another executable instead of the jar
ENTRYPOINT java \
    # java -D tag --> set a system property
    -Dkotlin.script.classpath="/opt/e20-api/e20-api.jar" \
    -jar \
    ./e20-api.jar