package ru.iot;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

import static java.util.Objects.isNull;

public class ProducerCreator {

	private KafkaProducer<String, String> kafkaProducer = null;

	public KafkaProducer<String, String> createProducer () {
		return isNull(kafkaProducer) ? initProducer() : kafkaProducer;
	}

	private KafkaProducer<String, String> initProducer() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ProducerConfig.RETRIES_CONFIG, 0);
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, 4094);
		props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		// set buffer size to 1 MB
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 11288576);
		props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 1000);
		// value to block, after which it will throw a TimeoutException
		props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 1000);

		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		//props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class.getName());
		return new KafkaProducer<>(props);
	}
}
