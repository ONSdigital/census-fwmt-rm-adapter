FROM openjdk:11-jdk-slim
ARG jar
COPY $jar /opt/rmcensusadapter.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java -jar /opt/rmcensusadapter.jar" ]
