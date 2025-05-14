package org.medilabo.microhisto.web.dao;

import org.medilabo.microhisto.model.Histo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HistoRepository extends MongoRepository<Histo, String> {
    //List<Histo> findByPatient(Long patId);
    List<Histo> findAllById(Long id);
}
