package org.medilabo.micropatient.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 *
 * To get the data about patients
 *
 */
@Getter
@Setter
@Entity
public class Patients {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable=false)
    private Long id;
    @Column(nullable=false)
    private String prenom;
    @Column(nullable=false)
    private String nom;
    @Column(nullable=false)
    private Date dateNaissance;
    @Column(nullable=false)
    private String genre;
    private String adressePostale;
    private String numTel;

}
