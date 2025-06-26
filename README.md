# Projet Medilabo

##A propos
Medilabo est un projet permettant à tout médecin d'enregistrer ses patients et leur historique pour ensuite consulter, modificer leur fiche ou consulter, compléter leur historque et enfin pour savoir, dans la fiche du patient, le niveau de rique de diabète leurs étant évalué.

##Les microservices
Micropatient pour gérer les patients et leur fiche. Utilise Spring security et MySQL
Microhisto pour gérer l'historique des patients. Utilise Spring security et l'API Gateway pour permettre les échanges entre microservices et MongoDB
Microrisque pour évaluer le risque de diabète d'un patient. Utilise la bibliothèque Spring Cloud OpenFeign pour créer des appels HTTP. 

##Le frontend
Le frontend a été développé en Angular, les sources sont dans le dossier angularmedilabo.

##Les scripts
Le fichier contenant les scripts SQL de la base de données medilabo avec la table patients est fourni dans le dossier src/main/resources
Le fichier contenant la liste des historiques au format JSON est fourni dans le dossier src/main/resources

##Conseils pour le Green Code
Compresser les images sur les sites web.
Optimiser l’utilisation des ressources matérielles et logicielles.
