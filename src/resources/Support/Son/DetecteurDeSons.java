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
        float[] combinaison = normalize(FFT("src/resources/Sources_sonores/Combinaison.wav"));
        float[] sinBruite = normalize(FFT("src/resources/Sources_sonores/SinusoideBruit.wav"));

        float[] carre = normalize(FFT("src/resources/Sources_sonores/Carre.wav"));
        //(╯°□°)╯︵ ┻━┻
        iNeurone[] neurones = new iNeurone[10];
        for (int i = 0; i < neurones.length / 2; i++) {
            neurones[i] = new NeuroneSigmoid(sinusoide.length);
        }
        for (int i = neurones.length / 2; i < neurones.length; i++) {
            neurones[i] = new NeuroneSigmoid(carre.length);
        }

        Neurone.fixeCoefApprentissage(0.1f);

        System.out.println("Apprentissage général…");

        int dataset_size = 40;
        float[][] entrees_sin = new float[dataset_size][];
        float[] sortie_sin = new float[dataset_size];
        for (int i = 0; i < (dataset_size) / 4; i++) {
            entrees_sin[i] = normalize(sinusoide_generator(10 * i, 5));
            sortie_sin[i] = 1;
            entrees_sin[i + (dataset_size) / 4] = normalize(noisy_sinusoide_generator(10 * i, 5));
            sortie_sin[i + (dataset_size) / 4] = 1;
            entrees_sin[i + 2 * (dataset_size) / 4] = normalize(carre_generator(10 * i, 5));
            sortie_sin[i + 2 * (dataset_size) / 4] = 0;
            entrees_sin[i + 3 * (dataset_size) / 4] = normalize(noisy_carre_generator(10 * i, 5));
            sortie_sin[i + 3 * (dataset_size) / 4] = 0;
        }

        for (int i = 0; i < neurones.length / 2; i++) {
            System.out.println("Nombre de tours : " + neurones[i].apprentissage(entrees_sin, sortie_sin));
        }

        float[][] entrees_carre = new float[dataset_size][];
        float[] sortie_carre = new float[dataset_size];
        for (int i = 0; i < (dataset_size) / 4; i++) {
            entrees_carre[i] = normalize(sinusoide_generator(10 * i, 5));
            sortie_carre[i] = 0;
            entrees_carre[i + (dataset_size) / 4] = normalize(noisy_sinusoide_generator(10 * i, 5));
            sortie_carre[i + (dataset_size) / 4] = 0;
            entrees_carre[i + 2 * (dataset_size) / 4] = normalize(carre_generator(10 * i, 5));
            sortie_carre[i + 2 * (dataset_size) / 4] = 1;
            entrees_carre[i + 3 * (dataset_size) / 4] = normalize(noisy_carre_generator(10 * i, 5));
            sortie_carre[i + 3 * (dataset_size) / 4] = 1;
        }

        for (int i = neurones.length / 2; i < neurones.length; i++) {
            System.out.println("Nombre de tours : " + neurones[i].apprentissage(entrees_carre, sortie_carre));
        }

        System.out.println("Apprentissage specialisé…");
        float[][] entrees = {sinusoide,sinusoide2,sinusoide3Harmoniques,combinaison,carre,bruit};
        float[] resultats = {1,1,1,1,0,0};

        for(int i = 0; i < neurones.length/2; i++){
            System.out.println("Nombre de tours : " + neurones[i].apprentissage(entrees, resultats));
        }
        entrees = new float[][]{carre,sinusoide2,sinusoide3Harmoniques,combinaison,sinusoide, bruit};
        resultats = new float[]{1,0,0,1,0,0};
        for (int i = neurones.length / 2; i < neurones.length; i++) {
            System.out.println("Nombre de tours : " + neurones[i].apprentissage(entrees, resultats));
        }

        //menu
        Scanner scanner = new Scanner(System.in);
        int choix = 1;
        do {
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

    public static void evaluate(iNeurone[] neurones, float[] entrees) {
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
        System.out.println("Moyenne des sorties : \nSinusoide : " + moyenneSortiesSin + "\nCarré : " + moyenneSortiesCarre);
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
    }

    public static float[] FFT(float[] input) {
        int blockSize = 512;
        int numBlocks = input.length / blockSize;
        float[] signal_complet_fft = new float[input.length * 2];
        float maxVal = 0;

        // First pass to find max value for normalization
        for (int i = 0; i < numBlocks; i++) {
            float[] audioBlock = new float[blockSize];
            for (int j = 0; j < blockSize; j++) {
                audioBlock[j] = input[i * blockSize + j];
            }
            for (float val : audioBlock) {
                maxVal = Math.max(maxVal, Math.abs(val));
            }
        }

        // Second pass to perform normalization and FFT
        for (int i = 0; i < numBlocks; i++) {
            float[] audioBlock = new float[blockSize];
            for (int j = 0; j < blockSize; j++) {
                audioBlock[j] = input[i * blockSize + j];
            }
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

    public static float[] sinusoide_generator(int freq, int duration) {
        float[] sinusoide = new float[44100 * duration];
        for (int i = 0; i < sinusoide.length; i++) {
            sinusoide[i] = (float) Math.sin(2 * Math.PI * freq * i / 44100);
        }
        return FFT(sinusoide);
    }

    public static float[] noisy_sinusoide_generator(int freq, int duration) {
        float[] sinusoide = new float[44100 * duration];
        for (int i = 0; i < sinusoide.length; i++) {
            sinusoide[i] = (float) Math.sin(2 * Math.PI * freq * i / 44100);
        }
        for (int i = 0; i < sinusoide.length; i++) {
            sinusoide[i] += Math.random() * 0.5 - 0.25;
        }
        return FFT(sinusoide);
    }

    public static float[] carre_generator(int freq, int duration) {
        float[] carre = new float[44100 * duration];
        for (int i = 0; i < carre.length; i++) {
            carre[i] = (float) (((Math.signum(Math.sin(2 * Math.PI * freq * i / 44100))) + 1) / 2);
            if (carre[i] == 0.5) {
                carre[i] = 0;
            }
        }
        return FFT(carre);
    }

    public static float[] noisy_carre_generator(int freq, int duration) {
        float[] carre = new float[44100 * duration];
        for (int i = 0; i < carre.length; i++) {
            carre[i] = (float) (((Math.signum(Math.sin(2 * Math.PI * freq * i / 44100))) + 1) / 2);
            if (carre[i] == 0.5) {
                carre[i] = 0;
            }
        }
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
