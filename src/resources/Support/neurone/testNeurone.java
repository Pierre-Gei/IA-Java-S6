package resources.Support.neurone;

import java.util.Arrays;
import java.util.Random;

public class testNeurone {
    public static void main(String[] args) {
        // Définition des entrées et des résultats attendus pour la fonction OU
        final float[][] entrees = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
        final float[] resultats = {0, 1, 1, 1};

        // Définition des entrées et des résultats attendus pour la fonction ET
        //final float[][] entrees = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
        //final float[] resultats = {0, 0, 0, 1};

        // Choix du type de neurone souhaité (à modifier par l'utilisateur)

        //final iNeurone n = new NeuroneHeaviside(entrees[0].length);
        final iNeurone n = new NeuroneSigmoid(entrees[0].length);
        //final iNeurone n = new NeuroneTanh(entrees[0].length);
        //final iNeurone n = new NeuroneReLu(entrees[0].length);

        System.out.println("Apprentissage…");
        //Apprentisage du neurone à partir des entrées et des résultats attendus
        System.out.println("Nombre de tours : " + n.apprentissage(entrees, resultats));

        // Affichage des poids d'entrée et du biais après l'apprentissage
        final Neurone vueNeurone = (Neurone) n;
        System.out.print("Synapses : ");
        for (final float f : vueNeurone.synapses())
            System.out.print(f + " ");
        System.out.print("\nBiais : ");
        System.out.println(vueNeurone.biais());

        float[] sommeSorties = new float[entrees.length];
        float[] moyenneSorties = new float[entrees.length];
        int nombreSorties = 0;

        for (int iter = 0; iter < 100; iter++) {

            // Ajout du bruit aux entrées selon une amplitude donnée (à modifier par l'utilisateur)
            float[][] entreesBruit = ajouterBruit(entrees, 0.5f);

            // Calcul de la sortie du neurone pour chaque entrée bruitée
            for (int i = 0; i < entreesBruit.length; ++i) {
                n.metAJour(entreesBruit[i]);
                float sortie = n.sortie();
                sommeSorties[i] += sortie;
                // Affichage de la sortie du neurone pour chaque entrée bruitée par itération
                System.out.println("Itération " + iter + ", Entrée " + i + Arrays.toString(entreesBruit[i]) + " : " + sortie);
            }
            System.out.println();
            nombreSorties++;
        }
        // Calcul de la moyenne des sorties pour chaque entrée sur les 100 itérations
        for(int i = 0; i < entrees.length; i++) {
            moyenneSorties[i] = sommeSorties[i] / nombreSorties;
            System.out.println("Moyenne des sorties pour l'entrée " + Arrays.toString(entrees[i]) + ": " + moyenneSorties[i]);
        }
    }

    //Fonction responsable de l'ajout de bruit aux entrées selon une amplitude donnée
    public static float[][] ajouterBruit(float[][] entrees, float amplitude) {
        Random rand = new Random();
        float[][] entreesBruit = new float[entrees.length][];
        for (int i = 0; i < entrees.length; ++i) {
            entreesBruit[i] = new float[entrees[i].length];
            for (int j = 0; j < entrees[i].length; ++j) {
                entreesBruit[i][j] = entrees[i][j] + amplitude * (rand.nextFloat() * 2 - 1);
            }
        }
        return entreesBruit;
    }
}
