package org.medilabo.microrisque.config;

import org.medilabo.microrisque.model.HistoriqueDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * Annotated interface definition to generate via Feign HTTP clients HTTP
 * to communicate with the microhisto remote microservice
 *
*/
@FeignClient(name = "microhisto", configuration = FeignConfig.class, url = "${microhisto.url:http://localhost:8998}")
//@FeignClient(name = "microhisto", configuration = FeignConfig.class, url = "${microhisto.url:http://microhisto:8998}")
public interface MicroHistoClient {
    @GetMapping("/hist/risque/{patientId}")
    ResponseEntity<HistoriqueDTO> getHistorique(@PathVariable String patientId);
}
