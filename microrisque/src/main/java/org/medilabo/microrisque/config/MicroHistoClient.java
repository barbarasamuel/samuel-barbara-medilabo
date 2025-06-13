package org.medilabo.microrisque.config;

import org.medilabo.microrisque.model.HistoriqueDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microhisto", url = "${microhisto.url:http://localhost:8998}")
public interface MicroHistoClient {
    @GetMapping("/hist/risque/{patientId}")
    ResponseEntity<HistoriqueDTO> getHistorique(@PathVariable String patientId);
}
