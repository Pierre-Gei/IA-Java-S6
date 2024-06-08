package resources.Support.Son;

import resources.Support.FFT.ComplexeCartesien;
import resources.Support.FFT.FFTCplx;
import resources.Support.FFT.Complexe;
import resources.Support.neurone.*;

import java.util.Scanner;

public class DetecteurDeSons {
    public static void main(String[] args) {
        float[] sinusoide = normalize(FFT("src/resources/Sources_sonores/Sinusoide.wav"));
        float[] sinusoide2 = normalize(FFT("src/resources/Sources_sonores/Sinusoide2.wav"));
        float[] sinusoide3Harmoniques = normalize(FFT("src/resources/Sources_sonores/Sinusoide3Harmoniques.wav"));
        float[] bruit = normalize(FFT("src/resources/Sources_sonores/Bruit.wav"));
        float[] carre = normalize(FFT("src/resources/Sources_sonores/Carre.wav"));
        float[] combinaison = normalize(FFT("src/resources/Sources_sonores/Combinaison.wav"));
        //(╯°□°)╯︵ ┻━┻
        float [] sinBruite = normalize(FFT("src/resources/Sources_sonores/SinusoideBruit.wav"));

        iNeurone neuroneSinusoide = new NeuroneSigmoid(sinusoide.length);
        iNeurone neuroneCarre = new NeuroneSigmoid(carre.length);

        //apprentisage
        System.out.println("Apprentissage…");
        float[][] entrees = {sinusoide,sinusoide2,sinusoide3Harmoniques,combinaison,carre};
        float[] resultats = {1,1,1,1,0};

        System.out.println("Nombre de tours : " + neuroneSinusoide.apprentissage(entrees, resultats));

        entrees = new float[][]{carre,sinusoide2,sinusoide3Harmoniques,bruit,combinaison,sinusoide};
        resultats = new float[]{1,0,0,0,1,0};

        System.out.println("Nombre de tours : " + neuroneCarre.apprentissage(entrees, resultats));


        //menu
        Scanner scanner = new Scanner(System.in);
        int choix = 1;
        do{
            System.out.println("1. Sinusoide");
            System.out.println("2. Sinusoide2");
            System.out.println("3. Sinusoide3Harmoniques");
            System.out.println("4. Bruit");
            System.out.println("5. Carre");
            System.out.println("6. Combinaison");
            System.out.println("7. SinusoideBruit");
            System.out.println("0. Quitter");
            System.out.println("Choix : ");
            choix = scanner.nextInt();
            switch(choix){
                case 1:
                    neuroneSinusoide.metAJour(sinusoide);
                    System.out.println("Sortie neurone sin: " + neuroneSinusoide.sortie());
                    neuroneCarre.metAJour(sinusoide);
                    System.out.println("Sortie neurone carré: " + neuroneCarre.sortie());
                    break;
                case 2:
                    neuroneSinusoide.metAJour(sinusoide2);
                    System.out.println("Sortie neurone sin: " + neuroneSinusoide.sortie());
                    neuroneCarre.metAJour(sinusoide2);
                    System.out.println("Sortie neurone carré: " + neuroneCarre.sortie());
                    break;
                case 3:
                    neuroneSinusoide.metAJour(sinusoide3Harmoniques);
                    System.out.println("Sortie neurone sin: " + neuroneSinusoide.sortie());
                    neuroneCarre.metAJour(sinusoide3Harmoniques);
                    System.out.println("Sortie neurone carré: " + neuroneCarre.sortie());
                    break;
                case 4:
                    neuroneSinusoide.metAJour(bruit);
                    System.out.println("Sortie neurone sin: " + neuroneSinusoide.sortie());
                    neuroneCarre.metAJour(bruit);
                    System.out.println("Sortie neurone carré: " + neuroneCarre.sortie());
                    break;
                case 5:
                    neuroneSinusoide.metAJour(carre);
                    System.out.println("Sortie neurone sin: " + neuroneSinusoide.sortie());
                    neuroneCarre.metAJour(carre);
                    System.out.println("Sortie neurone carré: " + neuroneCarre.sortie());
                    break;
                case 6:
                    neuroneSinusoide.metAJour(combinaison);
                    System.out.println("Sortie neurone sin: " + neuroneSinusoide.sortie());
                    neuroneCarre.metAJour(combinaison);
                    System.out.println("Sortie neurone carré: " + neuroneCarre.sortie());
                    break;
                case 7:
                    neuroneSinusoide.metAJour(sinBruite);
                    System.out.println("Sortie neurone sin: " + neuroneSinusoide.sortie());
                    neuroneCarre.metAJour(sinBruite);
                    System.out.println("Sortie neurone carré: " + neuroneCarre.sortie());
                    break;
                case 0:
                    System.out.println("Au revoir");
                    break;
                default:
                    System.out.println("Choix invalide");
                    break;
            }
        }while(choix != 0);
    }

    public static float[] FFT(String path) {
        Son son = new Son(path);
        int blockSize = 512;
        int numBlocks = son.donnees().length / blockSize;
        float[] signal_complet_fft = new float[son.donnees().length * 2];
        float maxVal = 0;

        // First pass to find max value for normalization
        for (int i = 0; i < numBlocks; i++) {
            float[] audioBlock = son.bloc_deTaille(i, blockSize);
            for (float val : audioBlock) {
                maxVal = Math.max(maxVal, Math.abs(val));
            }
        }

        // Second pass to perform normalization and FFT
        for (int i = 0; i < numBlocks; i++) {
            float[] audioBlock = son.bloc_deTaille(i, blockSize);
            for (int j = 0; j < audioBlock.length; j++) {
                audioBlock[j] /= maxVal;
            }

            Complexe[] signal = new Complexe[audioBlock.length];
            for (int j = 0; j < audioBlock.length; j++) {
                signal[j] = new ComplexeCartesien(audioBlock[j], 0);
            }
            Complexe[] signal_fft = FFTCplx.appliqueSur(signal);
            for (int j = 0; j < blockSize; j++) {
                signal_complet_fft[i * blockSize + j] = (float) signal_fft[j].mod();
                signal_complet_fft[i * blockSize + j + blockSize] = (float) signal_fft[j].arg();
            }
        }
        return signal_complet_fft;
    }

    public static float[] normalize(float[] input) {
        float maxVal = 0;
        for (float val : input) {
            maxVal = Math.max(maxVal, Math.abs(val));
        }
        float[] normalizedInput = new float[input.length];
        for (int i = 0; i < input.length; i++) {
            normalizedInput[i] = input[i] / maxVal;
        }
        return normalizedInput;
    }
}
