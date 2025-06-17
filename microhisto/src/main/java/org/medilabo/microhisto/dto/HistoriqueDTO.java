package org.medilabo.microhisto.dto;

import java.util.List;

/**
 *
 * DTO design pattern to collect the information about HistoriqueDTO
 *
 */
public class HistoriqueDTO {
    private Long patId;
    private List<String> note;

    public Long getPatId() {
        return patId;
    }

    public void setPatId(Long patId) {
        this.patId = patId;
    }

    public List<String> getNote() {
        return note;
    }

    public void setNote(List<String> note) {
        this.note = note;
    }

}
