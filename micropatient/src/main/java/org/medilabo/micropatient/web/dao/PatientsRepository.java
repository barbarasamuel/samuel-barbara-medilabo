package org.medilabo.micropatient.web.dao;

import org.medilabo.micropatient.model.Patients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *
 * To get the information from the database linked to the patients table
 *
 */
@Repository
public interface PatientsRepository extends JpaRepository<Patients, Long>  {
    public Optional<Patients> findById(Long id);
}
