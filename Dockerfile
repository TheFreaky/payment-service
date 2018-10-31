FROM openjdk:8-jdk-alpine

EXPOSE 8080

ARG JAR_FILE=target/payment-service-0.1.jar

ADD ${JAR_FILE} payment-service-0.1.jar

CMD ["java","-jar","/payment-service-0.1.jar"]