# ---- Etapa 1: Build ----
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

RUN echo '<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" \
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" \
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd"> \
  </settings>' > /usr/share/maven/ref/settings.xml

COPY wd-lib-common ./wd-lib-common
RUN mvn -s /usr/share/maven/ref/settings.xml -f wd-lib-common/pom.xml clean install -DskipTests

COPY ms-Scoring-evaluation ./ms-Scoring-evaluation
RUN mvn -s /usr/share/maven/ref/settings.xml -f ms-Scoring-evaluation/pom.xml clean package -DskipTests

# ---- Etapa 2: Runtime ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring

COPY --from=builder /app/ms-Scoring-evaluation/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]