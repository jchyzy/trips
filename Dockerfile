FROM openjdk:8
ADD target/Trip-manager.jar Trip-manager.jar
EXPOSE 8080
EXPOSE 5000
EXPOSE 61613
ENTRYPOINT ["java", "-jar", "Trip-manager.jar"]