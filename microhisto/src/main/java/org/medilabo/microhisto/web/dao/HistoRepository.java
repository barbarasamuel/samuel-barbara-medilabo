package org.medilabo.microhisto.web.dao;

import org.medilabo.microhisto.model.Histo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 *
 * To get the information from mongodb linked to the Histo document
 *
 */
public interface HistoRepository extends MongoRepository<Histo, Long> {
    List<Histo> findByPatId(Long id);
    Optional<Histo> findById(String id);
}
