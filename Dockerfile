FROM eclipse-temurin:21-jdk AS builder

WORKDIR /exchange

COPY . .

RUN ./mvnw clean package

FROM eclipse-temurin:21-jre

COPY --from=builder /exchange/target/*.jar exchange-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "exchange-0.0.1-SNAPSHOT.jar" ]
