package resources.Support.Son;

import resources.Support.FFT.ComplexeCartesien;
import resources.Support.FFT.FFTCplx;
import resources.Support.FFT.Complexe;
import resources.Support.neurone.*;

public class DetecteurDeSons {
    public static void main(String[] args) {
        float[][] FFTSinusoide = FFT("src/resources/Sources_sonores/Sinusoide.wav");
        float[][] sinusoide = new float[2][FFTSinusoide[0].length];
        for (int i = 0; i < FFTSinusoide[0].length; i++) {
            sinusoide[0][i] = FFTSinusoide[0][i];
            sinusoide[1][i] = FFTSinusoide[1][i];
        }
        float[][] FFTSinusoide2 = FFT("src/resources/Sources_sonores/Sinusoide2.wav");
        float[][] sinusoide2 = new float[2][FFTSinusoide2[0].length];
        for (int i = 0; i < FFTSinusoide2[0].length; i++) {
            sinusoide2[0][i] = FFTSinusoide2[0][i];
            sinusoide2[1][i] = FFTSinusoide2[1][i];
        }

        float[][] FFTSinusoide3Harmoniques = FFT("src/resources/Sources_sonores/Sinusoide3Harmoniques.wav");
        float[][] sinusoide3Harmoniques = new float[2][FFTSinusoide3Harmoniques[0].length];
        for (int i = 0; i < FFTSinusoide3Harmoniques[0].length; i++) {
            sinusoide3Harmoniques[0][i] = FFTSinusoide3Harmoniques[0][i];
            sinusoide3Harmoniques[1][i] = FFTSinusoide3Harmoniques[1][i];
        }

        float[][] FFTBruit = FFT("src/resources/Sources_sonores/Bruit.wav");
        float[][] bruit = new float[2][FFTBruit[0].length];
        for (int i = 0; i < FFTBruit[0].length; i++) {
            bruit[0][i] = FFTBruit[0][i];
            bruit[1][i] = FFTBruit[1][i];
        }

        float[][] FFTCarre = FFT("src/resources/Sources_sonores/Carre.wav");
        float[][] carre = new float[2][FFTCarre[0].length];
        for (int i = 0; i < FFTCarre[0].length; i++) {
            carre[0][i] = FFTCarre[0][i];
            carre[1][i] = FFTCarre[1][i];
        }

        float[][] FFTCombinaison = FFT("src/resources/Sources_sonores/Combinaison.wav");
        float[][] combinaison = new float[2][FFTCombinaison[0].length];
        for (int i = 0; i < FFTCombinaison[0].length; i++) {
            combinaison[0][i] = FFTCombinaison[0][i];
            combinaison[1][i] = FFTCombinaison[1][i];
        }

//        System.out.println("Sinusoide : ");
//        for (int i = 0; i < sinusoide.length; i++) {
//            System.out.print(sinusoide[i] + " ");
//        }

        iNeurone neuroneSinuoide = new NeuroneSigmoid(sinusoide[0].length);
        iNeurone neuroneCarre = new NeuroneSigmoid(carre[0].length);

        float[][] entrees = new float[][]{sinusoide[0], sinusoide[1], sinusoide2[0], sinusoide2[1], sinusoide3Harmoniques[0], sinusoide3Harmoniques[1], carre[0], carre[1]};
        float[] resultats = new float[]{1, 1, 1, 1, 0, 0, 0, 0};


        System.out.println("Apprentissage…");
        System.out.println("Nombre de tours : " + neuroneSinuoide.apprentissage(entrees, resultats));

        resultats = new float[]{0,0,0,0,0,0,1,1};
        System.out.println("Nombre de tours : " + neuroneCarre.apprentissage(entrees, resultats));

        //display number of synapses and bias for each neuron
        Neurone vueNeuroneSinusoide = (Neurone) neuroneSinuoide;
        System.out.println("Synapses Sinusoide : " + vueNeuroneSinusoide.synapses().length);
        System.out.println("Biais Sinusoide : " + vueNeuroneSinusoide.biais());

        //test the output of sinusoide neuron
        int echecs = 0;
        for (int i = 0; i < 1000; i++) {
            neuroneSinuoide.metAJour(sinusoide[0]);
            if (neuroneSinuoide.sortie() < 0.5) {
                echecs++;
            }
        }
        System.out.println("Nombre d'échecs pour la fonction Sinusoide : " + echecs);
        float [] bruite = FFT("src/resources/Sources_sonores/SinusoideBruit.wav")[0];
        echecs = 0;
        for (int i = 0; i < 1000; i++) {
            neuroneSinuoide.metAJour(bruite);
            if (neuroneSinuoide.sortie() < 0.5) {
                echecs++;
            }
        }
        System.out.println("Nombre d'échecs pour la fonction Sinusoide bruité : " + echecs);

        Neurone vueNeuroneCarre = (Neurone) neuroneCarre;
        System.out.println("Synapses Carre : " + vueNeuroneCarre.synapses().length);
        System.out.println("Biais Carre : " + vueNeuroneCarre.biais());
        echecs = 0;
        for (int i = 0; i < 1000; i++) {
            neuroneCarre.metAJour(carre[0]);
            if (neuroneCarre.sortie() < 0.5) {
                echecs++;
            }
        }
        System.out.println("Nombre d'échecs pour la fonction Carre : " + echecs);
        echecs = 0;
        for (int i = 0; i < 1000; i++) {
            neuroneCarre.metAJour(sinusoide2[0]);
            if (neuroneCarre.sortie() < 0.5) {
                echecs++;
            }
        }
        System.out.println("Nombre d'échecs pour la fonction Sin2 : " + echecs);
        echecs = 0;
        for (int i = 0; i < 1000; i++) {
            neuroneSinuoide.metAJour(combinaison[0]);
            if (neuroneSinuoide.sortie() < 0.5) {
                echecs++;
            }
        }
        System.out.println("Nombre d'échecs pour la fonction Combinaison : " + echecs);
        echecs = 0;
        for (int i = 0; i < 1000; i++) {
            neuroneCarre.metAJour(combinaison[0]);
            if (neuroneCarre.sortie() < 0.5) {
                echecs++;
            }
        }
        System.out.println("Nombre d'échecs pour la fonction Combinaison : " + echecs);
    }

    public static float[][] FFT(String path) {
        Son son = new Son(path);
        int blockSize = 512;
        int numBlocks = son.donnees().length / blockSize;
        float[][] signal_complet_fft = new float[2][son.donnees().length];
        float maxVal = 0;
        for (int i = 0; i < numBlocks; i++) {
            float[] audioBlock = son.bloc_deTaille(i, blockSize);
            for (float val : audioBlock) {
                maxVal = Math.max(maxVal, Math.abs(val));
            }
        }
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
                signal_complet_fft[0][i * blockSize + j] = (float) signal_fft[j].mod();
                signal_complet_fft[1][i * blockSize + j] = (float) signal_fft[j].arg();
            }
        }
//        System.out.println("Signal complet FFT : ");
//        for (int i = 0; i < signal_complet_fft[0].length; i++) {
//            System.out.print(signal_complet_fft[0][i] + " ");
//        }
        return signal_complet_fft;
    }

//    public static float[][] FFT_Bruite(String Path, int Signal_noise_ratio){
//        Son son = new Son(Path);
//        Son noise = new Son("src/resources/Sources_sonores/Bruit.wav");
//        int blockSize = 512;
//        if (son.donnees().length != noise.donnees().length) {
//            throw new IllegalArgumentException("Audio data and noise must be the same length");
//        }
//        float[] noisyAudioData = new float[son.donnees().length];
//        for (int i = 0; i < son.donnees().length; i++) {
//            noisyAudioData[i] = son.donnees()[i] + noise.donnees()[i]*(Signal_noise_ratio/100);
//        }
//        int numBlocks = noisyAudioData.length / blockSize;
//        float[][] signal_complet_fft = new float[2][noisyAudioData.length];
//        float maxVal = 0;
//        for (int i = 0; i < numBlocks; i++) {
//            float[] audioBlock = son.bloc_deTaille(i, blockSize);
//            for (float val : audioBlock) {
//                maxVal = Math.max(maxVal, Math.abs(val));
//            }
//        }
//        for (int i = 0; i < numBlocks; i++) {
//            float[] audioBlock = son.bloc_deTaille(i, blockSize);
//            for (int j = 0; j < audioBlock.length; j++) {
//                audioBlock[j] /= maxVal;
//            }
//            Complexe[] signal = new Complexe[audioBlock.length];
//            for (int j = 0; j < audioBlock.length; j++) {
//                signal[j] = new ComplexeCartesien(audioBlock[j], 0);
//            }
//            Complexe[] signal_fft = FFTCplx.appliqueSur(signal);
//            for (int j = 0; j < blockSize; j++) {
//                signal_complet_fft[0][i * blockSize + j] = (float) signal_fft[j].mod();
//                signal_complet_fft[1][i * blockSize + j] = (float) signal_fft[j].arg();
//            }
//        }
//
//        return signal_complet_fft;
//    }
}
