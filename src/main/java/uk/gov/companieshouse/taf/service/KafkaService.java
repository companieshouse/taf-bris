package uk.gov.companieshouse.taf.service;

import uk.gov.companieshouse.kafka.message.Message;

public interface KafkaService {

    public void send(Message kafkaMessage);
}
