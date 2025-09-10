# Projet Medilabo

##A propos
Medilabo est un projet permettant à tout médecin d'enregistrer ses patients et leur historique pour ensuite consulter, modificer leur fiche ou consulter et évaluer le niveau de risque de diabète type 2 pour le patient en cours.

##Les microservices
Microeureka pour enregistrer tous les microservices à l'exception de lui-même.
Microapigateway pour gérer une API Gateway et JWT.
Micropatient pour gérer les patients et leur fiche. Utilise Spring security et MySQL
Microhisto pour gérer l'historique des patients. Utilise Spring security et MongoDB
Microrisque pour évaluer le risque de diabète d'un patient. Utilise Spring security et la bibliothèque Spring Cloud OpenFeign pour créer des appels HTTP vers Micropatient et Microhisto. 

##Le frontend
Le frontend a été développé en Angular, les sources sont dans le dossier angularmedilabo.

##Les scripts
Le fichier contenant les scripts SQL de la base de données medilabo avec la table patients est fourni dans le dossier src/main/resources du microservice Micropatient.
Le fichier contenant la liste des historiques au format JSON est fourni dans le dossier src/main/resources du microservice Microhisto.

##Conseils pour le Green Code
Compresser les images sur les sites web.
Optimiser l’utilisation des ressources matérielles et logicielles.
