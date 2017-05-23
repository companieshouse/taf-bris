package uk.gov.companieshouse.taf.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import uk.gov.companieshouse.taf.domain.IncomingBRISMessage;

/**
 * Spring Data MongoDB repository for the IncomingBRISMessage entity.
 */
@Repository
public interface IncomingBRISMessageRepository extends
        MongoRepository<IncomingBRISMessage, String> {

    IncomingBRISMessage findOneById(String id);

    List<IncomingBRISMessage> findAll();

    @SuppressWarnings("unchecked")
    IncomingBRISMessage save(IncomingBRISMessage saved);

    @SuppressWarnings("unchecked")
    void delete(IncomingBRISMessage incomingBRISMessage);

    @SuppressWarnings("unchecked")
    void deleteAll();

    @Query("{ 'messageId' : ?0 }")
    IncomingBRISMessage findOneByMessageId(String messageId);

    @Query("{ 'messageId' : ?0 }")
    List<IncomingBRISMessage> findMultipleByMessageId(String messageId);

    @Query("{ 'id' : ?0, 'messageId' : ?1 }")
    IncomingBRISMessage findOneByIdAndMessageId(String id, String messageId);
}
