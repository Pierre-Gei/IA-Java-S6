import resources.Support.neurone.NeuroneHeaviside;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // Création d'une instance de NeuroneHeaviside
        NeuroneHeaviside neurone = new NeuroneHeaviside(2);

        // Définition des entrées et des résultats attendus pour la fonction ET
        float[][] entreesEt = ajouterBruit(new float[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}});
        float[] resultatsEt = new float[]{0, 0, 0, 1};

        // Apprentissage de la fonction ET et affichage des résultats
        apprendreEtAfficher(neurone, entreesEt, resultatsEt, "ET");

        // Réinitialisation du neurone
        neurone = new NeuroneHeaviside(2);

        // Définition des entrées et des résultats attendus pour la fonction OU
        float[][] entreesOu = ajouterBruit(new float[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}});
        float[] resultatsOu = new float[]{0, 1, 1, 1};

        // Apprentissage de la fonction OU et affichage des résultats
        apprendreEtAfficher(neurone, entreesOu, resultatsOu, "OU");
    }

    private static float[][] ajouterBruit(float[][] entrees) {
        for (float[] entree : entrees) {
            for (int i = 0; i < entree.length; i++) {
                entree[i] += (Math.random() - 0.5) * 0.1; // Ajoute un bruit entre -0.05 et 0.05
            }
        }
        return entrees;
    }

    private static void apprendreEtAfficher(NeuroneHeaviside neurone, float[][] entrees, float[] resultats, String nomFonction) {
        // Affichage des poids d'entrée et du biais avant l'apprentissage
        System.out.println("Poids d'entrée avant apprentissage pour " + nomFonction + ": " + Arrays.toString(neurone.synapses()));
        System.out.println("Biais avant apprentissage pour " + nomFonction + ": " + neurone.biais());

        // Apprentissage de la fonction
        int echecs = neurone.apprentissage(entrees, resultats);

        // Affichage des poids d'entrée et du biais après l'apprentissage
        System.out.println("Poids d'entrée après apprentissage pour " + nomFonction + ": " + Arrays.toString(neurone.synapses()));
        System.out.println("Biais après apprentissage pour " + nomFonction + ": " + neurone.biais());
        System.out.println("Nombre d'échecs pour la fonction " + nomFonction + ": " + echecs);
    }
}