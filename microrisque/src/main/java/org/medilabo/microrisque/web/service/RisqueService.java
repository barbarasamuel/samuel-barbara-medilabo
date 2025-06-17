package org.medilabo.microrisque.web.service;

import org.medilabo.microrisque.config.MicroHistoClient;
import org.medilabo.microrisque.config.MicroPatientClient;
import org.medilabo.microrisque.model.HistoriqueDTO;
import org.medilabo.microrisque.model.PatientsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

@Service
public class RisqueService {
    @Autowired
    private MicroPatientClient microPatientClient;

    @Autowired
    private MicroHistoClient microHistoClient;

    private static final List<String> TERMES_DECLENCHEURS = Arrays.asList(
            "Hémoglobine A1C", "Microalbumine", "Taille", "Poids", "Fumeur",
            "Fumeuse", "Anormal", "Cholestérol", "Vertiges",
            "Rechute", "Réaction", "Anticorps"
    );

    /**
     *
     * To estimate the risk of diabetes
     *
     */
    public String evaluerRisque(String patientId) {
        try {
            // Récupérer les données patient
            ResponseEntity<PatientsDTO> patientResponse = microPatientClient.getPatient(Long.valueOf(patientId));
            if (!patientResponse.getStatusCode().is2xxSuccessful() || patientResponse.getBody() == null) {
                throw new RuntimeException("Patient non trouvé");
            }
            PatientsDTO patient = patientResponse.getBody();/**/

            // Récupérer l'historique médical
            /**/ResponseEntity<HistoriqueDTO> histoResponse = microHistoClient.getHistorique(patientId);
            if (!histoResponse.getStatusCode().is2xxSuccessful() || histoResponse.getBody() == null) {
                throw new RuntimeException("Historique non trouvé");
            }
            HistoriqueDTO historique = histoResponse.getBody();

            // Calculer l'âge
            LocalDate date = patient.getDateNaissance().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            int age = calculerAge(date);

            // Compter les termes déclencheurs
            int nombreTermes = compterTermesDeclencheurs(historique.getNote());
            //int nombreTermes = 5;

            // Appliquer la logique d'évaluation
            return evaluerRisqueSelonCriteres(patient.getGenre(), age, nombreTermes);

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'évaluation du risque: " + e.getMessage());
        }
    }

    /**
     *
     * To calculate the age
     *
     */
    private int calculerAge(LocalDate dateNaissance) {
        return Period.between(dateNaissance, LocalDate.now()).getYears();
    }

    /**
     *
     * To calculate the number of trigger words
     *
     */
    private int compterTermesDeclencheurs(List<String> termes) {
        if (termes == null) return 0;

        return (int) termes.stream()
                .filter(terme -> TERMES_DECLENCHEURS.stream()
                        .anyMatch(declencheur -> terme.toLowerCase().contains(declencheur.toLowerCase())))
                .count();
    }

    /**
     *
     * To evaluate the risk according to criteria
     *
     */
    private String evaluerRisqueSelonCriteres(String genre, int age, int nombreTermes) {
        // Cas "Early onset"
        if ((genre.equalsIgnoreCase("homme") && age < 30 && nombreTermes == 5) ||
                (genre.equalsIgnoreCase("femme") && age < 30 && nombreTermes == 7) ||
                (age >= 30 && nombreTermes >= 8)) {
            return "Risque Early onset";
        }

        // Cas "In Danger"
        if ((genre.equalsIgnoreCase("homme") && age < 30 && nombreTermes == 3) ||
                (genre.equalsIgnoreCase("femme") && age < 30 && nombreTermes == 4) ||
                (age >= 30 && nombreTermes >= 6 && nombreTermes <= 7)) {
            return "Risque In Danger";
        }

        // Cas "None" - entre 2 et 5 termes et plus de 30 ans
        if (age >= 30 && nombreTermes >= 2 && nombreTermes <= 5) {
            return "Risque Borderline";
        }

        // Cas par défaut
        return "Risque None";
    }
    /*@Autowired
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
    }*/
}
