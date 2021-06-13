FROM openjdk:11-jdk-slim

RUN mkdir -p /home/app

COPY ./target/*.jar /home/app/logisticsService.jar

WORKDIR /home/app

CMD ["java", "-jar", "logisticsService.jar"]