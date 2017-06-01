package uk.gov.companieshouse.taf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.kafka.exceptions.ProducerConfigException;
import uk.gov.companieshouse.kafka.message.Message;
import uk.gov.companieshouse.kafka.producer.Acks;
import uk.gov.companieshouse.kafka.producer.CHKafkaProducer;
import uk.gov.companieshouse.kafka.producer.ProducerConfig;
import uk.gov.companieshouse.taf.config.Env;

@Component
public class KafkaServiceImpl implements KafkaService {

    private static String KAFKA_BROKER_ADDR = "kafka.broker.address";

    @Autowired
    private Env env;

    @Override
    public void send(Message kafkaMessage) {
        ProducerConfig config = new ProducerConfig();
        assignBrokerAddresses(config);
        config.setAcks(Acks.WAIT_FOR_LOCAL);
        config.setRetries(2);

        CHKafkaProducer producer = new CHKafkaProducer(config);
        producer.send(kafkaMessage);
        producer.close();
    }

    /**
     * Assign the broker address for kafka.
     */
    private void assignBrokerAddresses(ProducerConfig producerConfig) {
        String brokerAddresses = env.config.getString(KAFKA_BROKER_ADDR);

        if (brokerAddresses != null && !brokerAddresses.isEmpty()) {
            producerConfig.setBrokerAddresses(brokerAddresses.split(","));
        } else {
            throw new ProducerConfigException("Broker addresses for kafka brokers "
                    + "have not been supplied. Please make sure that this is configured"
                    + " in the kafka.broker.address entry  for the relevant environment "
                    + "profile in the env.conf file.");
        }
    }
}
