package uk.gov.companieshouse.taf.service;

import uk.gov.companieshouse.kafka.message.Message;

public interface KafkaService {

    void send(Message kafkaMessage);
}
