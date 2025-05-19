package org.medilabo.microhisto.web.dao;

import org.medilabo.microhisto.model.Histo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface HistoRepository extends MongoRepository<Histo, Long> {
    //List<Histo> findByPatient(Long patId);
    Optional<List<Histo>> findByPatId(Long id);
}
