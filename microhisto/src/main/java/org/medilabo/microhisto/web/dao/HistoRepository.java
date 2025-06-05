package org.medilabo.microhisto.web.dao;

import org.medilabo.microhisto.model.Histo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface HistoRepository extends MongoRepository<Histo, Long> {
    Optional<List<Histo>> findByPatId(Long id);
    Optional<Histo> findById(String id);
}
