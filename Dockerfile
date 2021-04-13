FROM openjdk:11-jdk
RUN addgroup --system spring && adduser --system spring && adduser spring spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]