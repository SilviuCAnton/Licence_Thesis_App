FROM openjdk:11-jdk-slim

RUN mkdir -p /home/app

COPY ./target/*.jar /home/app/menuService.jar

WORKDIR /home/app

CMD ["java", "-jar", "menuService.jar"]