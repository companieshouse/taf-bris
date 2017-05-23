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

    final Logger logger = LoggerFactory.getLogger(IncomingBrisMessageService.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private IncomingBrisMessageRepository incomingBrisMessageRepository;

    public List<IncomingBrisMessage> findAll() {
        return incomingBrisMessageRepository.findAll();
    }

    public IncomingBrisMessage findById(String id) {
        return incomingBrisMessageRepository.findOneById(id);
    }

    public IncomingBrisMessage save(IncomingBrisMessage incomingBrisMessage) {
        return incomingBrisMessageRepository.save(incomingBrisMessage);
    }

    public void delete(IncomingBrisMessage incomingBrisMessage) {
        incomingBrisMessageRepository.delete(incomingBrisMessage);
    }

    /**
     * Removes all IncomingBrisMessage entities from database.
     */
    public void deleteAll() {
        incomingBrisMessageRepository.deleteAll();
    }

    public IncomingBrisMessage findByMessageId(String messageId) {
        return incomingBrisMessageRepository.findOneByMessageId(messageId);
    }

    public List<IncomingBrisMessage> findMultipleByMessageId(String messageId) {
        return incomingBrisMessageRepository.findMultipleByMessageId(messageId);
    }

    public IncomingBrisMessage findOneByIdAndMessageId(String id, String messageId) {
        return incomingBrisMessageRepository.findOneByIdAndMessageId(id, messageId);
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
