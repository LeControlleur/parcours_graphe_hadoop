# Parcours en largeur de graphes avec hadoop

Ce fichier est à exécuter sur la machine virtuelle

Vous devez suivre les étapes suivantes : 

-   Mettre le fichier à traiter sur HDFS
        
    `hadoop fs -put graphe.txt /`

-   Exécuter le programme JAVA compilé  
        
    `hadoop jar target/parcours_graphe-1.0.jar com.estia.app.ParcoursGraphes /graph_input.txt /results_parcours_graphes`

-   Afficher les resultats de l'exécution du programme
        
    `hadoop fs -cat graphe.txt /results_parcours_graphes/*`