FROM openjdk:11-jre-slim
RUN mkdir -p /app
COPY ./build/install/network /app/network
WORKDIR /app/network/bin

ENTRYPOINT ["./network"]