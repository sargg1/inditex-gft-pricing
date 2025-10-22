FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml .
COPY pricing-domain/pom.xml pricing-domain/
COPY pricing-application/pom.xml pricing-application/
COPY pricing-infra/pom.xml pricing-infra/
COPY pricing-infra/infra-rest/pom.xml pricing-infra/infra-rest/
COPY pricing-infra/infra-jpa/pom.xml pricing-infra/infra-jpa/
COPY pricing-boot/pom.xml pricing-boot/
RUN mvn -q -ntp -e -DskipTests dependency:go-offline

COPY pricing-domain/src pricing-domain/src
COPY pricing-application/src pricing-application/src
COPY pricing-infra/infra-rest/src pricing-infra/infra-rest/src
COPY pricing-infra/infra-jpa/src pricing-infra/infra-jpa/src
COPY pricing-boot/src pricing-boot/src

# Saltamos los tests acelerando la creacion de la imagen ya que suponemos que los tests se lanzaran en el pipeline
RUN mvn -pl pricing-boot -am -ntp -DskipTests package

FROM eclipse-temurin:21-jre-jammy

ENV SPRING_DATASOURCE_USERNAME=${DATASOURCE_USERNAME}
ENV SPRING_DATASOURCE_PASSWORD=${DATASOURCE_PASSWORD}

RUN groupadd -g 10001 app && useradd -M -s /usr/sbin/nologin -u 10001 -g 10001 app
WORKDIR /app

COPY --from=build --chown=10001:10001 /workspace/pricing-boot/target/pricing-boot-0.0.1-SNAPSHOT.jar /app/app.jar
RUN chmod 0440 /app/app.jar && mkdir -p /app/logs && chown 10001:10001 /app/logs

USER 10001:10001
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
