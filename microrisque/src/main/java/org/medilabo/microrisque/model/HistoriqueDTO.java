package org.medilabo.microrisque.model;

import java.util.List;

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
