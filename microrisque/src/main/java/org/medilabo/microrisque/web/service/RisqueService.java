package org.medilabo.microrisque.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RisqueService {
    @Autowired
    private RestTemplate restTemplate;

    public Map<String, Integer> countTerms(List<String> terms) {
        ResponseEntity<Document[]> response = restTemplate.getForEntity(
                "http://service-b:8081/documents", Document[].class);
        Document[] documents = response.getBody();

        Map<String, Integer> termCounts = new HashMap<>();
        for (String term : terms) {
            termCounts.put(term.toLowerCase(), 0);
        }

        for (Document doc : documents) {
            String content = doc.getContent().toLowerCase();
            for (String term : terms) {
                int count = termCounts.get(term.toLowerCase());
                int occurrences = countOccurrences(content, term.toLowerCase());
                termCounts.put(term.toLowerCase(), count + occurrences);
            }
        }

        return termCounts;
    }

    private int countOccurrences(String text, String term) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(term, index)) != -1) {
            count++;
            index += term.length();
        }
        return count;
    }
}
