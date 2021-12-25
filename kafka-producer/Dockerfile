FROM ghcr.io/scholzj/java-base-image:jdk11-latest

LABEL org.opencontainers.image.source="https://github.com/scholzj/kafka-test-apps/kafka-producer/" \
      summary="Sample Kafka producer written in Vert.x" \
      description="Sample Kafka producer written in Vert.x"

ARG version=1.0-SNAPSHOT
ENV VERSION ${version}

ARG project=none
ENV PROJECT ${project}

COPY ./scripts/ /bin

ADD target/kafka-test-producer.jar /

USER 1001

CMD ["/bin/run.sh", "/kafka-test-producer.jar"]