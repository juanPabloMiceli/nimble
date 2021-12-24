FROM openjdk

WORKDIR /

COPY ./src/main/resources/ src/main/resources/

COPY ./build/libs/nimble*.jar nimble.jar

EXPOSE 8080

CMD ["java", "-jar", "nimble.jar"]