package ru.iot;

public interface IKafkaConstants {
	String KAFKA_BROKERS = "localhost:9092";
	String GROUP_ID="iot-group";
	String TOPIC_NAME="event-pro";
	String OFFSET_RESET_LATEST="latest";
	String OFFSET_RESET_EARLIER="earliest";
	Integer MAX_POLL_RECORDS=1;
}
