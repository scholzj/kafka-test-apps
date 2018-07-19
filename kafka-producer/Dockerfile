FROM scholzj/centos-java-base:latest

ARG version=latest
ENV VERSION ${version}

COPY ./scripts/ /bin

ADD target/kafka-test-producer.jar /

CMD ["/bin/run.sh", "/kafka-test-producer.jar"]