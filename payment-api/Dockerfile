FROM openjdk:17

WORKDIR /app

COPY ./build/libs/payment-api-0.0.1-SNAPSHOT.jar ./app/payment-api.jar

CMD ["java", "-jar", "./app/payment-api.jar"]