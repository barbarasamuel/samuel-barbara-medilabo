package org.medilabo.micropatient.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 *
 * DTO design pattern to collect the information about PatientsDTO
 *
 */
@Builder
@Data
public class PatientsDTO {
    private Long id;
    private Date dateNaissance;
    private String genre;
}
