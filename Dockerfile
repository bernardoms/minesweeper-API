# Install maven and copy project for compilation
FROM maven:latest as builder
WORKDIR app
ADD . /app

RUN mvn clean install -DskipTests

FROM openjdk:11
WORKDIR app

COPY --from=builder /app/target/*.jar /app/java-challenge.jar
ADD src/main/resources /app/resources
CMD java $JAVA_OPTS -jar /app/java-challenge.jar
