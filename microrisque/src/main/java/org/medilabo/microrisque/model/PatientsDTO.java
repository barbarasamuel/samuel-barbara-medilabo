package org.medilabo.microrisque.model;

import java.util.Date;

/**
 *
 * DTO design pattern to collect the information about PatientsDTO
 *
 */
public class PatientsDTO {
    private String id;
    private String genre;
    private Date dateNaissance;

    // Constructeurs
    public PatientsDTO() {}

    public PatientsDTO(String id, String genre, Date dateNaissance) {
        this.id = id;
        this.genre = genre;
        this.dateNaissance = dateNaissance;
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public Date getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(Date dateNaissance) { this.dateNaissance = dateNaissance;}
    }
