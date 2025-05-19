package org.medilabo.microhisto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MicrohistoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicrohistoApplication.class, args);
	}

}/*
@SpringBootApplication
public class MicrohistoApplication implements CommandLineRunner {

      private final Logger logger = LoggerFactory.getLogger(MicrohistoApplication.class);

      @Autowired
      private HistoRepository histoRepository;

	public static void main(String[] args) {
		SpringApplication.run(MicrohistoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
			  Histo histo1 = new Histo();
			  histo1.setPatId("1");
			  histo1.setPatient("TestNone");
			  histo1.setNote("Le patient déclare qu'il 'se sent très bien' Poids égal ou inférieur au poids recommandé");

			  Histo histo2 = new Histo();
			histo2.setPatId("2");
			histo2.setPatient("TestBorderline");
			histo2.setNote("Le patient déclare qu'il ressent beaucoup de stress au travail Il se plaint également que son audition est anormale dernièrement");

			Histo histo3 = new Histo();
			histo3.setPatId("3");
			histo3.setPatient("TestBorderline");
			histo3.setNote("Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois Il remarque également que son audition continue d'être anormale");

			Histo histo4 = new Histo();
			histo4.setPatId("4");
			histo4.setPatient("TestInDanger");
			histo4.setNote("Le patient déclare qu'il fume depuis peu");

			Histo histo5 = new Histo();
			histo5.setPatId("5");
			histo5.setPatient("TestInDanger");
			histo5.setNote(" Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière Il se plaint également de crises d’apnée respiratoire anormales Tests de laboratoire indiquant un taux de cholestérol LDL élevé");

			Histo histo6 = new Histo();
			histo6.setPatId("6");
			histo6.setPatient("TestEarlyOnset");
			histo6.setNote("Le patient déclare qu'il lui est devenu difficile de monter les escaliers Il se plaint également d’être essoufflé Tests de laboratoire indiquant que les anticorps sont élevés Réaction aux médicaments");

			Histo histo7 = new Histo();
			histo7.setPatId("7");
			histo7.setPatient("TestEarlyOnset");
			histo7.setNote("Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps");

			Histo histo8 = new Histo();
			histo8.setPatId("8");
			histo8.setPatient("TestEarlyOnset");
			histo8.setNote(" Le patient déclare avoir commencé à fumer depuis peu Hémoglobine A1C supérieure au niveau recommandé");

			Histo histo9 = new Histo();
			histo9.setPatId("9");
			histo9.setPatient("TestEarlyOnset");
			histo9.setNote("Taille, Poids, Cholestérol, Vertige et Réaction");

			histoRepository.insert(List.of(histo1,histo2,histo3,histo4,histo5,histo6,histo7,histo8,histo9));
		  }
}
*/