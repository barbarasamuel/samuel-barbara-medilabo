package org.medilabo.micropatient.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

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
    @NotBlank(message = "Le prénom est obligatoire")
    @Pattern(regexp = "^[A-ZÀ-ÖØ-Ý][a-zA-ZÀ-ÖØ-öø-ÿÇç-]*$",
    message = "Seules les lettres (avec ou sans accent) et les tirets sont autorisés. Le prénom doit commencer par une majuscule.")
    private String prenom;
    @Column(nullable=false)
    @NotBlank(message = "Le nom est obligatoire")
    @Pattern(regexp = "^[A-ZÀ-ÖØ-Ý][a-zA-ZÀ-ÖØ-öø-ÿÇç-]*$",
            message = "Seules les lettres (avec ou sans accent) et les tirets sont autorisés. Le nom doit commencer par une majuscule.")
    private String nom;
    @Column(nullable=false)
    private Date dateNaissance;
    @Column(nullable=false)
    @Pattern(regexp = "^(F|M)$",
            message = "Le genre doit être F (Féminin) ou M (Masculin).")
    private String genre;
    @Pattern(regexp = "^$|^[A-Za-z0-9 ,'-]+$", message = "L'adresse ne peut contenir que des lettres et des chiffres.")
    private String adresse;
    @Pattern(regexp = "^$|^\\d{3}-\\d{3}-\\d{4}$", message = "Le numéro de téléphone doit être au format 123-123-1234.")
    private String telephone;

}
