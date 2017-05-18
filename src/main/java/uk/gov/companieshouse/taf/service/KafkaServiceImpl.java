package uk.gov.companieshouse.taf.service;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.kafka.message.Message;
import uk.gov.companieshouse.kafka.producer.Acks;
import uk.gov.companieshouse.kafka.producer.CHKafkaProducer;
import uk.gov.companieshouse.kafka.producer.ProducerConfig;
import uk.gov.companieshouse.taf.config.ProducerConfigHelper;


@Component
public class KafkaServiceImpl implements KafkaService {

    @Override
    public void send(Message kafkaMessage) {
        ProducerConfig config = new ProducerConfig();
        ProducerConfigHelper.assignBrokerAddresses(config);
        config.setAcks(Acks.WAIT_FOR_LOCAL);
        config.setRetries(10);
        
        CHKafkaProducer producer = new CHKafkaProducer(config);
        producer.send(kafkaMessage);
        producer.close();
    }

}
