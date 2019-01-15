FROM openjdk:11-jdk-slim
ARG jar
COPY $jar rmcensusadapter.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java -jar /rmcensusadapter.jar" ]
