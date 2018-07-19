FROM scholzj/centos-java-base:latest

ARG version=latest
ENV VERSION ${version}

COPY ./scripts/ /bin

ADD target/kafka-test-consumer.jar /

CMD ["/bin/run.sh", "/kafka-test-consumer.jar"]