package ru.iot;



import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.eclipse.jetty.http.HttpStatus;
import ru.iot.dto.ControllerResult;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static spark.Spark.*;

@Slf4j
public class Launcher {
	public static void main(String[] args) {

		ProducerCreator producerCreator = new ProducerCreator();
		ConsumerCreator consumerCreator = new ConsumerCreator();

		KafkaProducer<String, String> producer = producerCreator.createProducer();
		KafkaConsumer<String, String> kafkaConsumer = consumerCreator.createConsumer();

		ObjectMapper objectMapper = new ObjectMapper();
		consumer(kafkaConsumer);

		post("/event/produce", (request, response) -> {
			String message = request.body();
			log.info("request body: {}", message);
			try {
				ProducerRecord<String, String> producerRecord = new ProducerRecord<>(
						IKafkaConstants.TOPIC_NAME, message
				);
				Future<RecordMetadata> send = producer.send(producerRecord);
				RecordMetadata recordMetadata = send.get();
				log.info("sent with partition: {}, offset: {}, message: {} ", recordMetadata.partition(), recordMetadata.offset(), message);

				response.type("application/json");
				response.status(HttpStatus.OK_200);
				return objectMapper.writeValueAsString(ControllerResult.successResult("event successfully sent"));
			}catch (ExecutionException | InterruptedException | IllegalStateException e) {
				response.status(HttpStatus.BAD_REQUEST_400);
				response.type("application/json");
				return objectMapper.writeValueAsString(ControllerResult.failResult("event could not sent"));
			}
		});

		get("/shutdown", (req, res) -> {
			kafkaConsumer.close(Duration.ofMillis(10));
			producer.flush();
			producer.close(Duration.ofMillis(10));
			stop();
			return objectMapper.writeValueAsString(ControllerResult.successResult("successfully shutdown server"));
		});



	}

	public static void consumer(KafkaConsumer<String, String> kafkaConsumer) {

		while (true) {
			// 1 is the time in seconds consumer will wait if no record is found at broker.
			ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));

			//print each record.
			consumerRecords.forEach(record -> {
				log.info( "Record:  key: {}, partition: {}, offset: {}, value: {} ", record.key(), record.partition(), record.offset(), record.value());
			});
			// commits the offset of record to broker.
			kafkaConsumer.commitAsync();
		}
	}

}
