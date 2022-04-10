FROM openjdk:11-jre-slim
RUN mkdir -p /app
COPY ./build/libs/network-0.0.1.jar /app/network
WORKDIR /app

ENTRYPOINT ["java", "-jar", "./network"]