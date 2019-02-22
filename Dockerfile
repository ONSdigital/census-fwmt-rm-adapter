FROM openjdk:11-jdk-slim
ARG jar
RUN groupadd -g 998 rmcensusadapter && \
    useradd -r -u 998 -g rmcensusadapter rmcensusadapter
USER rmcensusadapter
COPY $jar /opt/rmcensusadapter.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "java", "-jar", "/opt/rmcensusadapter.jar" ]
