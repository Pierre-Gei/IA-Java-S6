# Projet IA-JAVA-THS S6 2024
## Membres
- [GEIGUER Pierre](mailto:pierre.geiguer@isen.yncrea.fr)
- [REPPLINGER Lucas](mailto:lucas.repplinger@isen.yncrea.fr)

## Description

Le projet "IA-JAVA-THS S6 2024" implique l'application de connaissances sur la transformée de Fourier, les réseaux de neurones et la programmation orientée objet (Java) pour créer une chaîne de traitement qui change l'espace de représentation des données et les traite de manière intelligente. Le projet vise à combler l'écart entre la théorie et l'application pratique, en particulier dans le contexte du traitement du signal et des réseaux de neurones.

## Objectifs

- ### Application de la Transformée de Fourier (FFT) :
Comprendre et tester le code FFT fourni sur diverses fonctions telles que cosinus et sinus.
Analyser les effets des variations de fréquence sur la sortie de la FFT.
Étendre l'analyse aux entrées complexes en utilisant des fonctions sinus et cosinus.
 
- ### Réseaux de Neurones :
Implémenter et tester des algorithmes d'apprentissage pour les neurones, en se concentrant particulièrement sur l'apprentissage par correction d'erreur.
Utiliser différentes fonctions d'activation telles que Heaviside, Sigmoïde et ReLU.
Évaluer la robustesse des neurones avec des entrées bruitées et effectuer une analyse statistique des résultats.

- ### Intégration des Outils :
Combiner les outils FFT et réseaux de neurones pour construire un détecteur de sons qui identifie des signatures sonores spécifiques.
Optimiser les tailles de blocs pour le traitement des fichiers sonores et évaluer la précision de la détection.

- ### Approche Scientifique :
Documenter méthodiquement les expériences, les résultats et les conclusions.
Démontrer une compréhension claire de la complexité des algorithmes implémentés.
## Installation

Pour installer le projet, vous pouvez cloner le dépôt Git sur votre machine locale en utilisant la commande suivante :

```bash
git clone https://github.com/Pierre-Gei/IA-Java-S6.git
```

## Lancement et Utilisation

Vous pouvez lancer le fichier `projet.bat` (sous Windows) situé a la racine du projet pour lancer le projet.
```bash
projet.bat
```
Cela compilera tous les fichiers Java nécessaires et exécutera le programme principal `DetecteurDeSons.java`.
Vous pouvez aussi exécuter le fichier `DetecteurDeSons.java` situé a `src/DetecteurDeSons.java` dans votre IDE Java préféré après avoir compilé le projet.

Voici un exemple de ce à quoi pourrait ressembler le menu qui s'affiche une fois l'apprentissage du reseau de neurones terminé :

```
======================================================================
Bienvenue dans le détecteur de sons
Quel son voulez-vous analyser ?
1. Sinusoide
2. Sinusoide2
3. Sinusoide3Harmoniques
4. Bruit
5. Carre
6. Combinaison
7. SinusoideBruit
8. Sinusoide 100Hz
9. Carré bruité 200Hz
0. Quitter
   Choix :
```
Entrez simplement le numéro de l'option que vous souhaitez et le programme analysera le son correspondant.  
Notez que ce programme nécessite que Java soit installé sur votre machine et que le chemin d'accès à l'exécutable Java soit correctement configuré dans votre variable d'environnement PATH.

## Chronologie du Projet

- ### Phase Initiale (31/05 - 06/06) :
Découverte et compréhension des exigences du projet.
Sessions de travail en groupe pour brainstormer et démarrer le développement.

- ### Développement et Documentation (06/06 - 07/06) :
Compléter le développement des composants individuels.
Rédiger et affiner la documentation.

- ### Soumission Finale (10/06) :
Soumission du projet final, incluant le code et un rapport technique.
Test surveillé d'une heure pour évaluer la compréhension et les contributions individuelles.
Instructions on how to install the project or setup the development environment.