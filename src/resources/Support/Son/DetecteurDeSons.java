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

        iNeurone[] neurones = new iNeurone[50];
        for(int i = 0; i < neurones.length/2; i++){
            neurones[i] = new NeuroneSigmoid(sinusoide.length);
        }
        for(int i = neurones.length/2; i < neurones.length; i++){
            neurones[i] = new NeuroneSigmoid(carre.length);
        }

        //apprentisage
        System.out.println("Apprentissage…");
        float[][] entrees = {sinusoide,sinusoide2,sinusoide3Harmoniques,combinaison,carre};
        float[] resultats = {1,1,1,1,0};

        for(int i = 0; i < neurones.length/2; i++){
            System.out.println("Nombre de tours : " + neurones[i].apprentissage(entrees, resultats));
        }

        entrees = new float[][]{carre,sinusoide2,sinusoide3Harmoniques,combinaison,sinusoide};
        resultats = new float[]{1,0,0,1,0};

        for(int i = neurones.length/2; i < neurones.length; i++){
            System.out.println("Nombre de tours : " + neurones[i].apprentissage(entrees, resultats));
        }

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
                case 0:
                    System.out.println("Au revoir");
                    break;
                default:
                    System.out.println("Choix invalide");
                    break;
            }
        }while(choix != 0);
    }

    public static void evaluate (iNeurone[] neurones, float[] entrees) {
        float nombreNeurones = neurones.length;
        float moyenneSortiesSin = 0;
        float moyenneSortiesCarre = 0;
        for (int i = 0; i < neurones.length / 2; i++) {
            neurones[i].metAJour(entrees);
            moyenneSortiesSin += neurones[i].sortie();
        }
        for (int i = neurones.length / 2; i < neurones.length; i++) {
            neurones[i].metAJour(entrees);
            moyenneSortiesCarre += neurones[i].sortie();
        }
        moyenneSortiesSin /= nombreNeurones / 2;
        moyenneSortiesCarre /= nombreNeurones / 2;
        System.out.println("Moyenne des sorties " + moyenneSortiesSin + " " + moyenneSortiesCarre);
        if (moyenneSortiesSin > 0.5 && moyenneSortiesCarre > 0.5){
            System.out.println("C'est une combinaison");
        }else if(moyenneSortiesSin > 0.5){
            System.out.println("C'est une sinusoide");
        }else if (moyenneSortiesCarre > 0.5){
            System.out.println("C'est un carre");
        }else {
            if (moyenneSortiesSin < 0.05 && moyenneSortiesCarre < 0.05){
                System.out.println("C'est du bruit");
            } else if (moyenneSortiesSin > moyenneSortiesCarre ) {
                System.out.println("Il est plus probable que ce soit une sinusoide");
            } else {
                System.out.println("Il est plus probable que ce soit un carre");
            }
        }
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
