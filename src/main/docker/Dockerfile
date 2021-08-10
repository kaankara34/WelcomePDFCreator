FROM openjdk:8-jdk-alpine
RUN apk add tzdata && cp /usr/share/zoneinfo/Europe/Istanbul /etc/localtime
VOLUME /tmp
ADD aegon-welcome-script-pdf-service*.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
