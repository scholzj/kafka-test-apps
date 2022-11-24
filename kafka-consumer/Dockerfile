FROM registry.access.redhat.com/ubi8/ubi-minimal:latest

LABEL org.opencontainers.image.source="https://github.com/scholzj/kafka-test-apps/kafka-consumer/" \
      summary="Sample Kafka consumer written in Vert.x" \
      description="Sample Kafka consumer written in Vert.x"

ARG JAVA_VERSION=17

RUN microdnf update \
    && microdnf --setopt=install_weak_deps=0 --setopt=tsflags=nodocs install java-${JAVA_VERSION}-openjdk-headless \
    && microdnf clean all

ENV JAVA_HOME /usr/lib/jvm/jre-17

COPY ./scripts/ /bin

ADD target/kafka-test-consumer.jar /

USER 1001

CMD ["/bin/run.sh", "/kafka-test-consumer.jar"]