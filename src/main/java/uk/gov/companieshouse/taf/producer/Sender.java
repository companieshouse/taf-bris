package uk.gov.companieshouse.taf.producer;

public interface Sender {

    public boolean sendMessage(String topic, String messageId);
        
}
