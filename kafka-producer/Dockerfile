FROM enmasseproject/java-base:8-3

ARG version=latest
ENV VERSION ${version}
ADD target/kafka-test-producer.jar /

CMD ["/bin/launch_java.sh", "/kafka-test-producer.jar"]