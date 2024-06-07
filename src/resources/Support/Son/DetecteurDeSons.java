package resources.Support.Son;

import resources.Support.FFT.ComplexeCartesien;
import resources.Support.FFT.FFTCplx;
import resources.Support.FFT.Complexe;
import resources.Support.neurone.*;

import java.util.Random;

public class DetecteurDeSons {
    public static void main(String[] args) {
        int sampleRate = 44100;
        float[] sinusoide = FFT("src/resources/Sources_sonores/Sinusoide.wav");
        float[] sinusoide2 = FFT("src/resources/Sources_sonores/Sinusoide2.wav");
        float[] sinusoide3Harmoniques = FFT("src/resources/Sources_sonores/Sinusoide3Harmoniques.wav");
        float[] bruit = FFT("src/resources/Sources_sonores/Bruit.wav");
        float[] carre = FFT("src/resources/Sources_sonores/Carre.wav");
        float[] combinaison = FFT("src/resources/Sources_sonores/Combinaison.wav");

        iNeurone neuroneSinuoide = new NeuroneSigmoid(sinusoide.length);
        iNeurone neuroneSinusoide2 = new NeuroneSigmoid(sinusoide2.length);
        iNeurone neuroneSinusoide3Harmoniques = new NeuroneSigmoid(sinusoide3Harmoniques.length);
        iNeurone neuroneBruit = new NeuroneSigmoid(bruit.length);
        iNeurone neuroneCarre = new NeuroneSigmoid(carre.length);
        iNeurone neuroneCombinaison = new NeuroneSigmoid(combinaison.length);

        // create a neuron that will gather the results of the other neurons to make a decision on the type of sound
        //iNeurone neuroneDecision = new NeuroneSigmoid(6);

        System.out.println("Apprentissage…");
        neuroneSinuoide.apprentissage(new float[][]{sinusoide}, new float[]{1});
        neuroneSinusoide2.apprentissage(new float[][]{sinusoide2}, new float[]{1});
        neuroneSinusoide3Harmoniques.apprentissage(new float[][]{sinusoide3Harmoniques}, new float[]{1});
        neuroneBruit.apprentissage(new float[][]{bruit}, new float[]{0});
        neuroneCarre.apprentissage(new float[][]{carre}, new float[]{0});
        neuroneCombinaison.apprentissage(new float[][]{combinaison}, new float[]{0});

        //display training length
        System.out.println("Nombre de tours Sinusoide : " + neuroneSinuoide.apprentissage(new float[][]{sinusoide}, new float[]{1}));
        System.out.println("Nombre de tours Sinusoide2 : " + neuroneSinusoide2.apprentissage(new float[][]{sinusoide2}, new float[]{1}));
        System.out.println("Nombre de tours Sinusoide3Harmoniques : " + neuroneSinusoide3Harmoniques.apprentissage(new float[][]{sinusoide3Harmoniques}, new float[]{1}));
        System.out.println("Nombre de tours Bruit : " + neuroneBruit.apprentissage(new float[][]{bruit}, new float[]{0}));
        System.out.println("Nombre de tours Carre : " + neuroneCarre.apprentissage(new float[][]{carre}, new float[]{0}));
        System.out.println("Nombre de tours Combinaison : " + neuroneCombinaison.apprentissage(new float[][]{combinaison}, new float[]{0}));

        //display number of synapses and bias for each neuron
        Neurone vueNeuroneSinusoide = (Neurone)neuroneSinuoide;
        System.out.println("Synapses Sinusoide : " + vueNeuroneSinusoide.synapses().length);
        System.out.println("Biais Sinusoide : " + vueNeuroneSinusoide.biais());

        Neurone vueNeuroneSinusoide2 = (Neurone)neuroneSinusoide2;
        System.out.println("Synapses Sinusoide2 : " + vueNeuroneSinusoide2.synapses().length);
        System.out.println("Biais Sinusoide2 : " + vueNeuroneSinusoide2.biais());

        Neurone vueNeuroneSinusoide3Harmoniques = (Neurone)neuroneSinusoide3Harmoniques;
        System.out.println("Synapses Sinusoide3Harmoniques : " + vueNeuroneSinusoide3Harmoniques.synapses().length);
        System.out.println("Biais Sinusoide3Harmoniques : " + vueNeuroneSinusoide3Harmoniques.biais());

        Neurone vueNeuroneBruit = (Neurone)neuroneBruit;
        System.out.println("Synapses Bruit : " + vueNeuroneBruit.synapses().length);
        System.out.println("Biais Bruit : " + vueNeuroneBruit.biais());

        Neurone vueNeuroneCarre = (Neurone)neuroneCarre;
        System.out.println("Synapses Carre : " + vueNeuroneCarre.synapses().length);
        System.out.println("Biais Carre : " + vueNeuroneCarre.biais());

        Neurone vueNeuroneCombinaison = (Neurone)neuroneCombinaison;
        System.out.println("Synapses Combinaison : " + vueNeuroneCombinaison.synapses().length);
        System.out.println("Biais Combinaison : " + vueNeuroneCombinaison.biais());
        
    }

    public static float[] FFT(String path) {
        Son son = new Son(path);
        int blockSize = 512;
        int numBlocks = son.donnees().length / blockSize;
        float[] signal_complet_fft = new float[son.donnees().length];
        for (int i = 0; i < numBlocks; i++) {
            float[] audioBlock = son.bloc_deTaille(i, blockSize);
            Complexe[] signal = new Complexe[audioBlock.length];
            for (int j = 0; j < audioBlock.length; j++) {
                signal[j] = new ComplexeCartesien(audioBlock[j], 0);
            }
            Complexe[] signal_fft = FFTCplx.appliqueSur(signal);
            for (int j = 0; j < blockSize; j++) {
                signal_complet_fft[i * blockSize + j] = (float) signal_fft[j].mod();
            }
        }
        return signal_complet_fft;
    }

    public static float[] sin(int sampleRate, int frequency, int duration) {
        int numSamples = sampleRate * duration;
        Complexe[] sin = new Complexe[numSamples];
        for (int i = 0; i < numSamples; i++) {
            sin[i] = new ComplexeCartesien(Math.sin(2 * Math.PI * frequency * i / sampleRate), 0);
        }
        int blockSize = 512;
        int numBlocks = numSamples / blockSize;
        float[] signal_complet_fft = new float[numSamples];
        for (int i = 0; i < numBlocks; i++) {
            Complexe[] signal = new Complexe[blockSize];
            for (int j = 0; j < blockSize; j++) {
                signal[j] = sin[i * blockSize + j];
            }
            Complexe[] signal_fft = FFTCplx.appliqueSur(signal);
            for (int j = 0; j < blockSize; j++) {
                signal_complet_fft[i * blockSize + j] = (float) signal_fft[j].mod();
            }
        }
        return signal_complet_fft;
    }

    public static float[] addNoise(float[] audioData, float noiseAmplitude) {
        Random rand = new Random();
        float[] noisyAudioData = new float[audioData.length];
        for (int i = 0; i < audioData.length; i++) {
            float noise = (rand.nextFloat() * 2 - 1) * noiseAmplitude; // Generate random noise between -noiseAmplitude and +noiseAmplitude
            noisyAudioData[i] = audioData[i] + noise;
        }
        return noisyAudioData;
    }

}
