# FROM eclipse-temurin:17-jre
# ENV PORT 8080
# ENV CLASSPATH /opt/lib
# EXPOSE 8080

# # copy pom.xml and wildcards to avoid this command failing if there's no target/lib directory
# COPY pom.xml target/lib* /opt/lib/

# # NOTE we assume there's only 1 jar in the target dir
# # but at least this means we don't have to guess the name
# # we could do with a better way to know the name - or to always create an app.jar or something
# COPY target/*.jar /opt/app.jar
# WORKDIR /opt
# ENTRYPOINT exec java $JAVA_OPTS -jar app.jar


FROM eclipse-temurin:17 AS builder
WORKDIR workspace
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} service.jar
RUN java -Djarmode=layertools -jar service.jar extract

FROM eclipse-temurin:17
RUN useradd spring
USER spring
WORKDIR workspace
COPY --from=builder workspace/dependencies/ ./
COPY --from=builder workspace/spring-boot-loader/ ./
COPY --from=builder workspace/snapshot-dependencies/ ./
COPY --from=builder workspace/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]