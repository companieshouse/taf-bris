package uk.gov.companieshouse.taf.producer;

public interface Sender {

    boolean sendMessage(String topic, String messageId);
        
}
