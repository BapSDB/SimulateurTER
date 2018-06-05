# SimulateurTER
#### Simule la journalisation des données d'un objet domotique d'une maison intelligente à partir d'un fichier de trace après l'avoir formatté en .oebl et .csv
### Formats reconnus : .mqtt, .sw2, .oebl, .csv
- [X] V0 : Lecture d’un fichier de configuration et production de la ligne 1 du fichier résultat tabulaire format csv
- [X] V1 : Simulateur élémentaire prenant en entréee 2 fichiers (configuration et one-event-by-line), et production du fichier résultat tabulaire avec la stratégie « afficher une ligne par instant présent dans le fichier original »
- [X] V2 : HM simplifiée, bouton « step » et mise à jour des valeurs quand on appuie sur « step »
- [X] V3 : Implémentation de la stratégie avec un « step » d’une durée définie
- [X] V4 : Modification de l’IHM pour permettre le paramétrage du « step »
- [X] V5 : Traduction d’un type de fichier
- [X] V6 : Modification de l’IHM pour permettre de choisir le fichier d’entrée
- [X] V7 : Pour le configurateur, construction du fichier de configuration à partir d’un fichier one-event-by-line
