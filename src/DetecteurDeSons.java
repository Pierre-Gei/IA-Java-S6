import resources.Support.FFT.ComplexeCartesien;
import resources.Support.FFT.FFTCplx;
import resources.Support.FFT.Complexe;
import resources.Support.Son.Son;
import resources.Support.neurone.*;

import java.util.Scanner;

public class DetecteurDeSons {
    public static void main(String[] args) {
        //initialisation des sons à analyser avec la FFT
        float[] sinusoide = normalize(FFT("src/resources/Sources_sonores/Sinusoide.wav"));
        float[] sinusoide2 = normalize(FFT("src/resources/Sources_sonores/Sinusoide2.wav"));
        float[] sinusoide3Harmoniques = normalize(FFT("src/resources/Sources_sonores/Sinusoide3Harmoniques.wav"));
        float[] bruit = normalize(FFT("src/resources/Sources_sonores/Bruit.wav"));
        float[] combinaison = normalize(FFT("src/resources/Sources_sonores/Combinaison.wav"));
        float[] sinBruite = normalize(FFT("src/resources/Sources_sonores/SinusoideBruit.wav"));
        float[] carre = normalize(FFT("src/resources/Sources_sonores/Carre.wav"));
        //(╯°□°)╯︵ ┻━┻

        //initialisation d'un tableau de neurones dont la moitié est spécialisée dans la détection de sinusoïdes et l'autre dans la détection de carrés
        iNeurone[] neurones = new iNeurone[10];
        for (int i = 0; i < neurones.length / 2; i++) {
            neurones[i] = new NeuroneSigmoid(sinusoide.length);
        }
        for (int i = neurones.length / 2; i < neurones.length; i++) {
            neurones[i] = new NeuroneSigmoid(carre.length);
        }

        //apprentissage général sur des sinusoïdes et des carrés generés avec et sans bruit
        System.out.println("Apprentissage général…");

        //nombre de données d'apprentissage
        int dataset_size = 20;
        float[][] entrees_sin = new float[dataset_size][];
        float[] sortie_sin = new float[dataset_size];
        //génération des données d'apprentissage pour les sinusoidales 1/4 sinusoïdes, 1/4 sinusoïdes bruitées, 1/4 carrés, 1/4 carrés bruités
        for (int i = 0; i < (dataset_size) / 4; i++) {
            entrees_sin[i] = normalize(sinusoide_generator(200 * i, 5));
            sortie_sin[i] = 1;
            entrees_sin[i + (dataset_size) / 4] = normalize(noisy_sinusoide_generator(200 * i, 5));
            sortie_sin[i + (dataset_size) / 4] = 1;
            entrees_sin[i + 2 * (dataset_size) / 4] = normalize(carre_generator(200 * i, 5));
            sortie_sin[i + 2 * (dataset_size) / 4] = 0;
            entrees_sin[i + 3 * (dataset_size) / 4] = normalize(noisy_carre_generator(200 * i, 5));
            sortie_sin[i + 3 * (dataset_size) / 4] = 0;
        }

        //apprentissage des neurones sinusoidaux avec les données générées
        for (int i = 0; i < neurones.length / 2; i++) {
            System.out.println("Nombre de tours : " + neurones[i].apprentissage(entrees_sin, sortie_sin));
        }

        float[][] entrees_carre = new float[dataset_size][];
        float[] sortie_carre = new float[dataset_size];
        //génération des données d'apprentissage pour les carrés 1/4 sinusoïdes, 1/4 sinusoïdes bruitées, 1/4 carrés, 1/4 carrés bruités
        for (int i = 0; i < (dataset_size) / 4; i++) {
            entrees_carre[i] = normalize(sinusoide_generator(200 * i, 5));
            sortie_carre[i] = 0;
            entrees_carre[i + (dataset_size) / 4] = normalize(noisy_sinusoide_generator(200 * i, 5));
            sortie_carre[i + (dataset_size) / 4] = 0;
            entrees_carre[i + 2 * (dataset_size) / 4] = normalize(carre_generator(200 * i, 5));
            sortie_carre[i + 2 * (dataset_size) / 4] = 1;
            entrees_carre[i + 3 * (dataset_size) / 4] = normalize(noisy_carre_generator(200 * i, 5));
            sortie_carre[i + 3 * (dataset_size) / 4] = 1;
        }

        //apprentissage des neurones carré avec les données générées
        for (int i = neurones.length / 2; i < neurones.length; i++) {
            System.out.println("Nombre de tours : " + neurones[i].apprentissage(entrees_carre, sortie_carre));
        }

        System.out.println("Apprentissage specialisé…");
        //apprentissage spécialisé sur les sons à analyser pour ameliorer la detection des sons donnés
        float[][] entrees = {sinusoide,sinusoide2,sinusoide3Harmoniques,combinaison,carre,bruit};
        float[] resultats = {1,1,1,1,0,0};

        //apprentissage des neurones sinusoidaux avec les données à analyser
        for(int i = 0; i < neurones.length/2; i++){
            System.out.println("Nombre de tours : " + neurones[i].apprentissage(entrees, resultats));
        }
        entrees = new float[][]{carre,sinusoide2,sinusoide3Harmoniques,combinaison,sinusoide, bruit};
        resultats = new float[]{1,0,0,1,0,0};
        //apprentissage des neurones carré avec les données à analyser
        for (int i = neurones.length / 2; i < neurones.length; i++) {
            System.out.println("Nombre de tours : " + neurones[i].apprentissage(entrees, resultats));
        }

        //menu
        //objet scanner pour lire les entrées clavier
        Scanner scanner = new Scanner(System.in);
        int choix = 1;
        do {
            System.out.println("======================================================================");
            System.out.println("Bienvenue dans le détecteur de sons");
            System.out.println("Quel son voulez-vous analyser ?");
            System.out.println("1. Sinusoide");
            System.out.println("2. Sinusoide2");
            System.out.println("3. Sinusoide3Harmoniques");
            System.out.println("4. Bruit");
            System.out.println("5. Carre");
            System.out.println("6. Combinaison");
            System.out.println("7. SinusoideBruit");
            System.out.println("8. Sinusoide 100Hz");
            System.out.println("9. Carré bruité 200Hz");
            System.out.println("0. Quitter");
            System.out.println("Choix : ");
            choix = scanner.nextInt();
            switch (choix) {
                case 1:
                    evaluate(neurones, sinusoide);
                    break;
                case 2:
                    evaluate(neurones, sinusoide2);
                    break;
                case 3:
                    evaluate(neurones, sinusoide3Harmoniques);
                    break;
                case 4:
                    evaluate(neurones, bruit);
                    break;
                case 5:
                    evaluate(neurones, carre);
                    break;
                case 6:
                    evaluate(neurones, combinaison);
                    break;
                case 7:
                    evaluate(neurones, sinBruite);
                    break;
                case 8:
                    evaluate(neurones, normalize(sinusoide_generator(100, 5)));
                    break;
                case 9:
                    evaluate(neurones, normalize(noisy_carre_generator(200, 5)));
                    break;
                case 0:
                    System.out.println("Au revoir");
                    break;
                default:
                    System.out.println("Choix invalide");
                    break;
            }
        } while (choix != 0);
    }

    //fonction pour évaluer les sorties des neurones et donner une prédiction sur le type de son
    public static void evaluate(iNeurone[] neurones, float[] entrees) {
        //calcul de la moyenne des sorties des neurones spécialisés en sinusoïdes et en carrés
        float nombreNeurones = neurones.length;
        float moyenneSortiesSin = 0;
        float moyenneSortiesCarre = 0;
        //metAJour les neurones spécialisés en sinusoïdes et calcule la moyenne des sorties
        for (int i = 0; i < neurones.length / 2; i++) {
            neurones[i].metAJour(entrees);
            moyenneSortiesSin += neurones[i].sortie();
        }
        //metAJour les neurones spécialisés en carrés et calcule la moyenne des sorties
        for (int i = neurones.length / 2; i < neurones.length; i++) {
            neurones[i].metAJour(entrees);
            moyenneSortiesCarre += neurones[i].sortie();
        }
        moyenneSortiesSin /= nombreNeurones / 2;
        moyenneSortiesCarre /= nombreNeurones / 2;
        //affichage de la moyenne des sorties et prédiction
        System.out.println("Moyenne des sorties : \nNeurones specialisés en Sinusoides : " + moyenneSortiesSin + "\nNeurones specialisés en Carrés : " + moyenneSortiesCarre);
        //passage en rouge pour les prédictions
        System.out.print("\033[31m");
        if (moyenneSortiesSin > 0.5 && moyenneSortiesCarre > 0.5) {
            System.out.println("C'est une combinaison");
        } else if (moyenneSortiesSin > 0.5) {
            System.out.println("C'est une sinusoide");
        } else if (moyenneSortiesCarre > 0.5) {
            System.out.println("C'est un carre");
        } else {
            if (moyenneSortiesSin < 0.05 && moyenneSortiesCarre < 0.05) {
                System.out.println("C'est du bruit");
            } else if (moyenneSortiesSin > moyenneSortiesCarre) {
                System.out.println("Il est plus probable que ce soit une sinusoide");
            } else {
                System.out.println("Il est plus probable que ce soit un carre");
            }
        }
        //retour à la couleur normale
        System.out.print("\033[0m");
    }

    //fonction qui prend un tableau de float en entrée et renvoie un tableau de float normalisé après FFT
    public static float[] FFT(float[] input) {
        //Taille des blocs pour la FFT
        int blockSize = 512;
        int numBlocks = input.length / blockSize;
        //Tableau de float pour stocker les valeurs de la FFT
        float[] signal_complet_fft = new float[input.length * 2];
        //Valeur maximale pour la normalisation
        float maxVal = 0;

        // Premier passage pour trouver la valeur maximale pour la normalisation avant la FFT
        for (int i = 0; i < numBlocks; i++) {
            float[] audioBlock = new float[blockSize];
            for (int j = 0; j < blockSize; j++) {
                audioBlock[j] = input[i * blockSize + j];
            }
            for (float val : audioBlock) {
                maxVal = Math.max(maxVal, Math.abs(val));
            }
        }

        // Deuxième passage pour appliquer la normalisation et la FFT
        for (int i = 0; i < numBlocks; i++) {
            // Création d'un bloc de float pour la FFT
            float[] audioBlock = new float[blockSize];
            for (int j = 0; j < blockSize; j++) {
                audioBlock[j] = input[i * blockSize + j];
            }
            // Normalisation
            for (int j = 0; j < audioBlock.length; j++) {
                audioBlock[j] /= maxVal;
            }
            // Création d'un tableau de Complexe pour la FFT
            Complexe[] signal = new Complexe[audioBlock.length];
            for (int j = 0; j < audioBlock.length; j++) {
                signal[j] = new ComplexeCartesien(audioBlock[j], 0);
            }
            // Application de la FFT
            Complexe[] signal_fft = FFTCplx.appliqueSur(signal);
            // Stockage des valeurs de la FFT dans le tableau de float sous forme de module et d'argument
            for (int j = 0; j < blockSize; j++) {
                signal_complet_fft[i * blockSize + j] = (float) signal_fft[j].mod();
                signal_complet_fft[i * blockSize + j + blockSize] = (float) signal_fft[j].arg();
            }
        }
        // Renvoie du tableau de float normalisé après FFT
        return signal_complet_fft;
    }

    //fonction qui prend un path en entrée et renvoie un tableau de float normalisé après FFT
    public static float[] FFT(String path) {
        //Création d'un objet Son à partir du path
        Son son = new Son(path);
        //Taille des blocs pour la FFT
        int blockSize = 512;
        int numBlocks = son.donnees().length / blockSize;
        float[] signal_complet_fft = new float[son.donnees().length * 2];
        //Valeur maximale pour la normalisation
        float maxVal = 0;

        // Premier passage pour trouver la valeur maximale pour la normalisation avant la FFT
        for (int i = 0; i < numBlocks; i++) {
            float[] audioBlock = son.bloc_deTaille(i, blockSize);
            for (float val : audioBlock) {
                maxVal = Math.max(maxVal, Math.abs(val));
            }
        }
        // Deuxième passage pour appliquer la normalisation et la FFT
        for (int i = 0; i < numBlocks; i++) {
            float[] audioBlock = son.bloc_deTaille(i, blockSize);
            // Normalisation
            for (int j = 0; j < audioBlock.length; j++) {
                audioBlock[j] /= maxVal;
            }
            // Création d'un tableau de Complexe pour la FFT
            Complexe[] signal = new Complexe[audioBlock.length];
            for (int j = 0; j < audioBlock.length; j++) {
                signal[j] = new ComplexeCartesien(audioBlock[j], 0);
            }
            // Application de la FFT
            Complexe[] signal_fft = FFTCplx.appliqueSur(signal);
            // Stockage des valeurs de la FFT dans le tableau de float sous forme de module et d'argument
            for (int j = 0; j < blockSize; j++) {
                signal_complet_fft[i * blockSize + j] = (float) signal_fft[j].mod();
                signal_complet_fft[i * blockSize + j + blockSize] = (float) signal_fft[j].arg();
            }
        }
        // Renvoie du tableau de float normalisé après FFT
        return signal_complet_fft;
    }

    //fonction qui prend un tableau de float en entrée et renvoie un tableau de float normalisé entre -1 et 1
    public static float[] normalize(float[] input) {
        // Recherche de la valeur maximale pour la normalisation
        float maxVal = 0;
        for (float val : input) {
            maxVal = Math.max(maxVal, Math.abs(val));
        }
        // Normalisation
        float[] normalizedInput = new float[input.length];
        for (int i = 0; i < input.length; i++) {
            normalizedInput[i] = input[i] / maxVal;
        }
        return normalizedInput;
    }

    //fonction qui génère un tableau de float correspondant à une sinusoïde de fréquence freq et de durée duration
    public static float[] sinusoide_generator(int freq, int duration) {
        // Création d'un tableau de float pour stocker la sinusoïde
        float[] sinusoide = new float[44100 * duration];
        // Remplissage du tableau avec les valeurs de la sinusoïde
        for (int i = 0; i < sinusoide.length; i++) {
            sinusoide[i] = (float) Math.sin(2 * Math.PI * freq * i / 44100);
        }
        // Application de la FFT
        return FFT(sinusoide);
    }

    //fonction qui génère un tableau de float correspondant à une sinusoïde de fréquence freq et de durée duration avec du bruit
    public static float[] noisy_sinusoide_generator(int freq, int duration) {
        // Création d'un tableau de float pour stocker la sinusoïde
        float[] sinusoide = new float[44100 * duration];
        for (int i = 0; i < sinusoide.length; i++) {
            sinusoide[i] = (float) Math.sin(2 * Math.PI * freq * i / 44100);
        }
        // Ajout de bruit
        for (int i = 0; i < sinusoide.length; i++) {
            sinusoide[i] += Math.random() * 0.5 - 0.25;
        }
        return FFT(sinusoide);
    }

    //fonction qui génère un tableau de float correspondant à un carré de fréquence freq et de durée duration
    public static float[] carre_generator(int freq, int duration) {
        // Création d'un tableau de float pour stocker le carré
        float[] carre = new float[44100 * duration];
        for (int i = 0; i < carre.length; i++) {
            carre[i] = (float) (((Math.signum(Math.sin(2 * Math.PI * freq * i / 44100))) + 1) / 2);
            // Remplacement des valeurs de 0.5 causées par le signum a 0 pour éviter des problèmes de normalisation
            if (carre[i] == 0.5) {
                carre[i] = 0;
            }
        }
        return FFT(carre);
    }

    //fonction qui génère un tableau de float correspondant à un carré de fréquence freq et de durée duration avec du bruit
    public static float[] noisy_carre_generator(int freq, int duration) {
        // Création d'un tableau de float pour stocker le carré
        float[] carre = new float[44100 * duration];
        for (int i = 0; i < carre.length; i++) {
            carre[i] = (float) (((Math.signum(Math.sin(2 * Math.PI * freq * i / 44100))) + 1) / 2);
            // Remplacement des valeurs de 0.5 causées par le signum a 0 pour éviter des problèmes de normalisation
            if (carre[i] == 0.5) {
                carre[i] = 0;
            }
        }
        // Ajout de bruit
        for (int i = 0; i < carre.length; i++) {
            carre[i] += Math.random() * 0.5 - 0.25;
            if (carre[i] > 1) {
                carre[i] = 1;
            } else if (carre[i] < 0) {
                carre[i] = 0;
            }
        }
        return FFT(carre);
    }
}
