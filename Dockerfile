#############################################################################################
###              Stage where Docker is building spring boot app using maven               ###
#############################################################################################
FROM maven:3.8.3-openjdk-17 as build

ARG SKIP_TESTS=false
ARG MVN_PROFILE=default

WORKDIR /

COPY . .

RUN mvn -ntp -B clean install \
        -P ${MVN_PROFILE} \
        -Dmaven.test.skip=${SKIP_TESTS}

#############################################################################################

#############################################################################################
### Stage where Docker is running a java process to run a service built in previous stage ###
#############################################################################################
FROM eclipse-temurin:17-jre-alpine

RUN apk upgrade expat  # Fix for CVE-2022-43680

ARG SERVICE_NAME=pcss-criminal-application

COPY --from=build ./${SERVICE_NAME}/target/${SERVICE_NAME}.jar /app/service.jar

CMD ["java", "-Xmx1g", "-jar", "/app/service.jar"]
#############################################################################################
