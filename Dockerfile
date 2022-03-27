FROM openjdk:11-jre-slim
RUN mkdir -p /app
COPY ./build/install/network/ /app/
WORKDIR /app

ENTRYPOINT ["./bin/network"]