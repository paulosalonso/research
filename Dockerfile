FROM maven:3.6.3-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml package -Pno-tests

FROM openjdk:11-jdk
VOLUME /tmp
COPY --from=build /home/app/target/research.jar research.jar
ENTRYPOINT ["java", "-jar", "/research.jar"]
