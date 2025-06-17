package org.medilabo.micropatient.web.controller;

import org.medilabo.micropatient.web.service.PatientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/proxy")
public class ProxyController {
   /* @Autowired
    private PatientsService patientsService;

    @GetMapping("/patients")
    public ResponseEntity<String> proxyUsers(@RequestHeader("Authorization") String authHeader) {
        String jwt = authHeader.replace("Bearer ", "");
        String result = patientsService.getUsers(jwt);
        return ResponseEntity.ok(result);
    }*/
}
