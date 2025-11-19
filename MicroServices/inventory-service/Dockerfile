# ---------- build stage ----------
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /workspace

# copy pom first to leverage Maven cache for dependencies
COPY pom.xml ./
RUN mvn -B -f pom.xml -q dependency:go-offline

# copy sources and build
COPY src ./src
RUN mvn -B -f pom.xml clean package -DskipTests

# ---------- runtime stage ----------
FROM eclipse-temurin:21-jre-jammy
ARG JAR_FILE=target/*.jar
WORKDIR /app

# install curl (helpful for healthchecks; optional)
RUN apt-get update \
 && apt-get install -y --no-install-recommends curl \
 && rm -rf /var/lib/apt/lists/*

# create non-root user (recommended)
RUN addgroup --system app && adduser --system --ingroup app app

# copy jar from build stage
COPY --from=build /workspace/target/*.jar app.jar

# expose port — the app uses server.port from properties; EXPOSE just documents it
EXPOSE 8080

# run as non-root
USER app

# recommended JVM options (adjust as needed)
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app/app.jar" ]
