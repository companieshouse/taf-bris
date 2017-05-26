package uk.gov.companieshouse.taf.config;

/**
 * Created by jblandford on 15/05/2017.
 */

import uk.gov.companieshouse.kafka.exceptions.ProducerConfigException;
import uk.gov.companieshouse.kafka.producer.ProducerConfig;

public class ProducerConfigHelper {

    public ProducerConfigHelper() {
    }

    /**
     * Assign the broker address for kafka.
     */
    public static void assignBrokerAddresses(ProducerConfig producerConfig) {
        String brokerAddresses = System.getenv("KAFKA_BROKER_ADDR");
        brokerAddresses = "chs-kafka:9092";
        if (brokerAddresses != null && !brokerAddresses.isEmpty()) {
            producerConfig.setBrokerAddresses(brokerAddresses.split(","));
        } else {
            throw new ProducerConfigException("Broker addresses for kafka broker not supplied,"
                    + " use the environment variable KAFKA_BROKER_ADDR");
        }
    }
}