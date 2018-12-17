FROM openjdk:11-jdk-slim
MAINTAINER Jacob Harrison <jacob.harrisone@ons.gov.uk>
ARG jar
VOLUME /tmp
COPY $jar rmcensusadapter.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java -jar /rmcensusadapter.jar" ]
