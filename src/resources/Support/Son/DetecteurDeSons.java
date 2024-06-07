package resources.Support.Son;

import resources.Support.FFT.ComplexeCartesien;
import resources.Support.FFT.FFTCplx;
import resources.Support.FFT.Complexe;
import resources.Support.neurone.NeuroneHeaviside;
import resources.Support.neurone.iNeurone;

public class DetecteurDeSons {
    public static void main(String[] args) {
        int sampleRate = 44100;
        Complexe[] sinusoide = FFT("src/resources/Sources_sonores/Sinusoide.wav");
        Complexe[] sinusoide2 = FFT("src/resources/Sources_sonores/Sinusoide2.wav");
        Complexe[] sinusoide3Harmoniques = FFT("src/resources/Sources_sonores/Sinusoide3Harmoniques.wav");
        Complexe[] bruit = FFT("src/resources/Sources_sonores/Bruit.wav");
        Complexe[] carre = FFT("src/resources/Sources_sonores/Carre.wav");
        Complexe[] combinaison = FFT("src/resources/Sources_sonores/Combinaison.wav");

        //train 1 neurone to trigger on sinusoide and not on Bruit or Carre
        iNeurone neurone = new NeuroneHeaviside(sinusoide.length);
        float[][] entrees = new float[][]{toMagnitudeArray(sinusoide), toMagnitudeArray(bruit), toMagnitudeArray(carre)};
        float[] resultats = new float[]{1, 0, 0};
        neurone.apprentissage(entrees, resultats);

        //train the neurone to also trigger on sinusoide2
        float[][] entrees2 = new float[][]{toMagnitudeArray(sinusoide2), toMagnitudeArray(bruit), toMagnitudeArray(carre)};
        float[] resultats2 = new float[]{1, 0, 0};
        neurone.apprentissage(entrees2, resultats2);

        //test if the neurone triggers on sinusoide and not on Bruit or Carre
        float[] sinusoideMagnitude = toMagnitudeArray(sinusoide);
        float[] sinusoide2Magnitude = toMagnitudeArray(sinusoide2);
        float[] sinusoide3HarmoniquesMagnitude = toMagnitudeArray(sinusoide3Harmoniques);
        float[] bruitMagnitude = toMagnitudeArray(bruit);
        float[] carreMagnitude = toMagnitudeArray(carre);
        int echecs = 0;
        for (int i = 0 ; i < 10; i++ ){
            neurone.metAJour(sinusoideMagnitude);
            System.out.println("Sinusoide: " + neurone.sortie());
            if (neurone.sortie() != 1) {
                echecs++;
            }
            neurone.metAJour(bruitMagnitude);
            System.out.println("Bruit: " + neurone.sortie());
            if (neurone.sortie() != 0) {
                echecs++;
            }
            neurone.metAJour(carreMagnitude);
            System.out.println("Carre: " + neurone.sortie());
            if (neurone.sortie() != 0) {
                echecs++;
            }
        }
        System.out.println("Nombre d'échecs: " + echecs);
        System.out.println("Nombre de réussites: " + (30 - echecs));
        System.out.println("Taux de réussite: " + (30 - echecs) / 30.0);
        System.out.println("================================");
        System.out.println("Test avec Sin2");
        echecs = 0;
        for (int i = 0 ; i < 10; i++ ){
            neurone.metAJour(sinusoide2Magnitude);
            System.out.println("Sinusoide2: " + neurone.sortie());
            if (neurone.sortie() != 1) {
                echecs++;
            }
            neurone.metAJour(bruitMagnitude);
            System.out.println("Bruit: " + neurone.sortie());
            if (neurone.sortie() != 0) {
                echecs++;
            }
            neurone.metAJour(carreMagnitude);
            System.out.println("Carre: " + neurone.sortie());
            if (neurone.sortie() != 0) {
                echecs++;
            }
        }
        System.out.println("Nombre d'échecs: " + echecs);
        System.out.println("Nombre de réussites: " + (30 - echecs));
        System.out.println("Taux de réussite: " + (30 - echecs) / 30.0);
        System.out.println("================================");
        System.out.println("Test avec Sin3Harmoniques");
        echecs = 0;
        for (int i = 0 ; i < 10; i++ ){
            neurone.metAJour(sinusoide3HarmoniquesMagnitude);
            System.out.println("Sinusoide3Harmoniques: " + neurone.sortie());
            if (neurone.sortie() != 1) {
                echecs++;
            }
            neurone.metAJour(bruitMagnitude);
            System.out.println("Bruit: " + neurone.sortie());
            if (neurone.sortie() != 0) {
                echecs++;
            }
            neurone.metAJour(carreMagnitude);
            System.out.println("Carre: " + neurone.sortie());
            if (neurone.sortie() != 0) {
                echecs++;
            }
        }
        System.out.println("Nombre d'échecs: " + echecs);
        System.out.println("Nombre de réussites: " + (30 - echecs));
        System.out.println("Taux de réussite: " + (30 - echecs) / 30.0);
    }

    public static Complexe[] FFT(String path) {
        Son son = new Son(path);
        int blockSize = 256; // Adjust this value as needed
        for (int i = 0; i < son.donnees().length / blockSize; i++) {
            float[] audioBlock = son.bloc_deTaille(i, blockSize);
            Complexe[] signal = new Complexe[audioBlock.length];
            for (int j = 0; j < audioBlock.length; j++) {
                signal[j] = new ComplexeCartesien(audioBlock[j], 0);
            }
            return FFTCplx.appliqueSur(signal);
        }
        return null;
    }

    public static float[] toMagnitudeArray(Complexe[] complexArray) {
        float[] magnitudeArray = new float[complexArray.length];
        for (int i = 0; i < complexArray.length; i++) {
            magnitudeArray[i] = (float) complexArray[i].mod();
        }
        return magnitudeArray;
    }
}