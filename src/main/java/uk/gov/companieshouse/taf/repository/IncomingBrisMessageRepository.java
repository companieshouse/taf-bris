package uk.gov.companieshouse.taf.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import uk.gov.companieshouse.taf.domain.IncomingBrisMessage;

/**
 * Spring Data MongoDB repository for the IncomingBrisMessage entity.
 */
@Repository
public interface IncomingBrisMessageRepository extends
        MongoRepository<IncomingBrisMessage, String> {

    IncomingBrisMessage findOneById(String id);

    List<IncomingBrisMessage> findAll();

    @SuppressWarnings("unchecked")
    IncomingBrisMessage save(IncomingBrisMessage saved);

    @SuppressWarnings("unchecked")
    void delete(IncomingBrisMessage incomingBrisMessage);

    @SuppressWarnings("unchecked")
    void deleteAll();

    @Query("{ 'messageId' : ?0 }")
    IncomingBrisMessage findOneByMessageId(String messageId);

    @Query("{ 'messageId' : ?0 }")
    List<IncomingBrisMessage> findMultipleByMessageId(String messageId);

    @Query("{ 'id' : ?0, 'messageId' : ?1 }")
    IncomingBrisMessage findOneByIdAndMessageId(String id, String messageId);
}
