FROM eclipse-temurin:11

RUN mkdir /test-project/app
WORKDIR /test-project/app

COPY target/test-project.jar /test-project/app/test-project.jar

CMD ["java", "-jar", "/test-project/app/test-project.jar"]
