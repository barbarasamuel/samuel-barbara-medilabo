package org.medilabo.microhisto.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Getter
@Setter
@Document(collection = "histo")
public class Histo {
    @Id
    private Long patId;
    private String patient;
    private String note;
}
