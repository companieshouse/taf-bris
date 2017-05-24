package uk.gov.companieshouse.taf.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.taf.domain.IncomingBrisMessage;
import uk.gov.companieshouse.taf.repository.IncomingBrisMessageRepository;

@ComponentScan
@Service
public class IncomingBrisMessageService {

    @Autowired
    private IncomingBrisMessageRepository incomingBrisMessageRepository;

    /**
     * Finds an Incoming BRIS message by correlation ID.
     */
    public IncomingBrisMessage findOneByCorrelationId(String correlationId) {
        return incomingBrisMessageRepository.findOneByCorrelationId(correlationId);
    }
}
