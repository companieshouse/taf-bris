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
import uk.gov.companieshouse.taf.domain.IncomingBRISMessage;
import uk.gov.companieshouse.taf.repository.IncomingBRISMessageRepository;


@ComponentScan
@Service
public class IncomingBRISMessageService {

    final Logger logger = LoggerFactory.getLogger(IncomingBRISMessageService.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private IncomingBRISMessageRepository incomingBRISMessageRepository;

    public List<IncomingBRISMessage> findAll() {
        return incomingBRISMessageRepository.findAll();
    }

    public IncomingBRISMessage findById(String id) {
        return incomingBRISMessageRepository.findOneById(id);
    }

    public IncomingBRISMessage save(IncomingBRISMessage incomingBRISMessage) {
        return incomingBRISMessageRepository.save(incomingBRISMessage);
    }

    public void delete(IncomingBRISMessage incomingBRISMessage) {
        incomingBRISMessageRepository.delete(incomingBRISMessage);
    }

    /**
     * Removes all IncomingBRISMessage entities from database.
     */
    public void deleteAll() {
        incomingBRISMessageRepository.deleteAll();
    }

    public IncomingBRISMessage findByMessageId(String messageId) {
        return incomingBRISMessageRepository.findOneByMessageId(messageId);
    }

    public List<IncomingBRISMessage> findMultipleByMessageId(String messageId) {
        return incomingBRISMessageRepository.findMultipleByMessageId(messageId);
    }

    public IncomingBRISMessage findOneByIdAndMessageId(String id, String messageId) {
        return incomingBRISMessageRepository.findOneByIdAndMessageId(id, messageId);
    }

    /**
     * Find the message by stored message id in the incoming mongo collection.
     *
     * @param messageId message id to locate
     */
    public List<String> findDistinctByMessageId(String messageId) {
        List<String> messageIdList;
        DB db = mongoTemplate.getDb();
        DBCollection dbCollection = db.getCollection("brisIncomingMessageCollection");
        DBObject o1 = new BasicDBObject("message_id", new BasicDBObject("$eq", messageId));
        messageIdList = dbCollection.distinct("message_id", o1);

        return messageIdList;
    }

    /**
     * Find the message by stored correlation id in the incoming mongo collection.
     *
     * @param correlationId the correlation id to find
     */
    public List<String> findDistinctByCorrelationId(String correlationId) {
        List<String> correlationIdList;
        DB db = mongoTemplate.getDb();
        DBCollection dbCollection = db.getCollection("brisIncomingMessageCollection");
        DBObject o2 = new BasicDBObject("correlation_id", new BasicDBObject("$eq", correlationId));
        correlationIdList = dbCollection.distinct("correlation_id", o2);

        return correlationIdList;
    }
}
