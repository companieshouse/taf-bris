package uk.gov.companieshouse.taf.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.taf.domain.IncomingBRISMessage;
import uk.gov.companieshouse.taf.repository.IncomingBRISMessageRepository;
import java.util.ArrayList;
import java.util.List;

@ComponentScan
@Service
public class IncomingBRISMessageService {

    final Logger logger = LoggerFactory.getLogger(IncomingBRISMessageService.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private IncomingBRISMessageRepository incomingBRISMessageRepository;

    public List<IncomingBRISMessage> findAll() {
        List<IncomingBRISMessage> incomingBRISMessageEntries = incomingBRISMessageRepository.findAll();
        return incomingBRISMessageEntries;
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
        List<IncomingBRISMessage> incomingBRISMessageEntries = incomingBRISMessageRepository.findMultipleByMessageId(messageId);
        return incomingBRISMessageEntries;
    }

    public IncomingBRISMessage findOneByIdAndMessageId(String id, String messageId) {
        return incomingBRISMessageRepository.findOneByIdAndMessageId(id, messageId);
    }

    public List<String> findDistinctByMessageId(String messageId) {
        List<String> messageIdList = new ArrayList<String>();
        DB db = mongoTemplate.getDb();
        DBCollection dbCollection = db.getCollection("brisIncomingMessageCollection");
        DBObject o1 = new BasicDBObject("message_id", new BasicDBObject("$eq", messageId));
        messageIdList = dbCollection.distinct("message_id", o1);

        return messageIdList;
    }

    public List<String> findDistinctByCorrelationId(String messageId) {
        List<String> correlationIdList = new ArrayList<String>();
        DB db = mongoTemplate.getDb();
        DBCollection dbCollection = db.getCollection("brisIncomingMessageCollection");
        DBObject o2 = new BasicDBObject("correlation_id", new BasicDBObject("$eq", messageId));
        correlationIdList = dbCollection.distinct("correlation_id", o2);

        return correlationIdList;
    }
}
