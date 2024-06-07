package resources.Support.neurone;

import java.util.Arrays;
import java.util.Random;

public class testNeurone {
    public static void main(String[] args) {
        final float[][] entrees = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
        final float[] resultats = {0, 1, 1, 1};

        for (int iter = 0; iter < 10; iter++) {
            System.out.println("\nItération n°" + iter);
            //final iNeurone n = new NeuroneSigmoid(entrees[0].length);
            final iNeurone n = new NeuroneHeaviside(entrees[0].length);
            // final iNeurone n = new Neurone(entrees[0].length);
            // final iNeurone n = new NeuroneReLu(entrees[0].length);
            //final iNeurone n = new NeuroneTanh(entrees[0].length);

            System.out.println("Apprentissage…");
            System.out.println("Nombre de tours : " + n.apprentissage(entrees, resultats));

            final Neurone vueNeurone = (Neurone) n;
            System.out.print("Synapses : ");
            for (final float f : vueNeurone.synapses())
                System.out.print(f + " ");
            System.out.print("\nBiais : ");
            System.out.println(vueNeurone.biais());

            for (int i = 0; i < entrees.length; ++i) {
                final float[] entree = entrees[i];
                n.metAJour(entree);
                System.out.println("Entree " + i + " : " + n.sortie());
            }

            statistiquesPoids(vueNeurone);


            float[][] entreesBruit = ajouterBruit(entrees, 0.4f);
            for (int i = 0; i < entreesBruit.length; ++i) {
                n.metAJour(entreesBruit[i]);
                System.out.println("Entree " + i + Arrays.deepToString(entreesBruit) + " : " + n.sortie());
            }
            statistiquesSortiesGlobales(vueNeurone, entreesBruit, 100);
        }
    }

    public static void statistiquesPoids(Neurone vueNeurone) {
        float sum = 0;
        float min = vueNeurone.synapses()[0];
        float max = vueNeurone.synapses()[0];
        for (float poids : vueNeurone.synapses()) {
            sum += poids;
            min = Math.min(min, poids);
            max = Math.max(max, poids);
        }
        float mean = sum / vueNeurone.synapses().length;
        float variance = 0;
        for (float poids : vueNeurone.synapses()) {
            variance += Math.pow(poids - mean, 2);
        }
        variance /= vueNeurone.synapses().length;
        float stdDev = (float) Math.sqrt(variance);

        System.out.println("Moyenne des poids: " + mean);
        System.out.println("Poids minimum: " + min);
        System.out.println("Poids maximum: " + max);
        System.out.println("Écart type des poids: " + stdDev);
    }

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

    public static void statistiquesSortiesGlobales(iNeurone n, float[][] entrees, int iterations) {
        float[][] sorties = new float[iterations][entrees.length];
        for (int iter = 0; iter < iterations; ++iter) {
            for (int i = 0; i < entrees.length; ++i) {
                n.metAJour(entrees[i]);
                sorties[iter][i] = n.sortie();
            }
        }
        float sum_ent1 = 0;
        float sum_ent2 = 0;
        float sum_ent3 = 0;
        float sum_ent4 = 0;
        for (int i = 0; i < iterations; ++i) {
            sum_ent1 += sorties[i][0];
            sum_ent2 += sorties[i][1];
            sum_ent3 += sorties[i][2];
            sum_ent4 += sorties[i][3];
        }

        float mean_ent1 = sum_ent1 / iterations;
        float mean_ent2 = sum_ent2 / iterations;
        float mean_ent3 = sum_ent3 / iterations;
        float mean_ent4 = sum_ent4 / iterations;

        System.out.println("Moyenne des sorties pour l'entrée 1: " + mean_ent1);
        System.out.println("Moyenne des sorties pour l'entrée 2: " + mean_ent2);
        System.out.println("Moyenne des sorties pour l'entrée 3: " + mean_ent3);
        System.out.println("Moyenne des sorties pour l'entrée 4: " + mean_ent4);
    }
}
