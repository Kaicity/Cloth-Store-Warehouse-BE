# Stage: PACKAGE
FROM maven:3.8.4-openjdk-17 AS MAVEN_BUILD

WORKDIR /usr/src/app

# copy the pom and src code to the container
COPY . .

# package our application code
RUN mvn clean package

# Stage: FINAL
FROM openjdk:17-alpine AS FINAL

ARG PROFILE
ENV APP_PORT=5556
ENV APP_VERSION=0.0.1-SNAPSHOT
ENV APP_PROFILE=$PROFILE

WORKDIR /usr/src/app

# copy only the artifacts we need from the first stage and discard the rest
COPY --from=MAVEN_BUILD /usr/src/app/ct-start/target/*.jar ./

EXPOSE ${APP_PORT}

CMD java -Dserver.port=${APP_PORT} -jar ./ct-start-${APP_VERSION}.jar --spring.profiles.active=${APP_PROFILE}
