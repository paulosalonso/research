FROM openjdk:11-jdk
VOLUME /tmp
COPY target/research.jar research.jar
ENTRYPOINT ["java", "-jar", "/research.jar"]
