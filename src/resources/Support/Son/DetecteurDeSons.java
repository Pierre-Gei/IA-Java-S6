package resources.Support.Son;

import resources.Support.FFT.ComplexeCartesien;
import resources.Support.FFT.FFTCplx;
import resources.Support.FFT.Complexe;
import resources.Support.neurone.NeuroneHeaviside;
import resources.Support.neurone.iNeurone;

public class DetecteurDeSons {
    public static void main(String[] args) {
        int sampleRate = 44100; // Adjust this value as needed
        int targetFrequencyHz = 200;
                // 1. Lecture du fichier son
                        Son son = new Son("src/resources/Sources_sonores/Sinusoide.wav");
                // 2. Apply the FFT to each block of audio data
                int blockSize = 256; // Adjust this value as needed
                for (int i = 0; i < son.donnees().length / blockSize; i++) {
                    float[] audioBlock = son.bloc_deTaille(i, blockSize);
                    Complexe[] signal = new Complexe[audioBlock.length];
                    for (int j = 0; j < audioBlock.length; j++) {
                        signal[j] = new ComplexeCartesien(audioBlock[j], 0);
                    }
                    Complexe[] frequencyComponents = FFTCplx.appliqueSur(signal);

                }
                float[] outputs = new float[son.donnees().length / blockSize];

                //get the main frequency component of the signal


        // 3. Use the frequency components to detect the presence of a specific waveform (e.g., a sine wave) at a target frequency
        int targetFrequencyIndex = targetFrequencyHz * blockSize / sampleRate;
        float threshold = 0.1f; // Adjust this value as needed
        for (int i = 0; i < son.donnees().length / blockSize; i++) {
            float[] audioBlock = son.bloc_deTaille(i, blockSize);
            Complexe[] signal = new Complexe[audioBlock.length];
            for (int j = 0; j < audioBlock.length; j++) {
                signal[j] = new ComplexeCartesien(audioBlock[j], 0);
            }
            Complexe[] frequencyComponents = FFTCplx.appliqueSur(signal);

            // Set the desired output for the neuron based on the presence of the target frequency
            outputs[i] = frequencyComponents[targetFrequencyIndex].mod() > threshold ? 1.0f : 0.0f;
        }

        // 4. Use a perceptron to classify the audio data based on the detected frequency components
        iNeurone neuron = new NeuroneHeaviside(blockSize); // Adjust this as needed
        float[][] inputs = new float[son.donnees().length / blockSize][blockSize];
        for (int i = 0; i < son.donnees().length / blockSize; i++) {
            float[] audioBlock = son.bloc_deTaille(i, blockSize);
            Complexe[] signal = new Complexe[audioBlock.length];
            for (int j = 0; j < audioBlock.length; j++) {
                signal[j] = new ComplexeCartesien(audioBlock[j], 0);
            }
            Complexe[] frequencyComponents = FFTCplx.appliqueSur(signal);

            // Convert the frequency components to inputs for the neuron
            for (int j = 0; j < blockSize; j++) {
                inputs[i][j] = (float) frequencyComponents[j].mod();
            }

            // Set the desired output for the neuron
            outputs[i] = frequencyComponents[targetFrequencyIndex].mod() > threshold ? 1.0f : 0.0f;
        }

        // Train the neuron with the inputs and outputs
        neuron.apprentissage(inputs, outputs);

        // 5. Use the neuron to classify new audio data
        int count = 0;
        Son newAudioData = new Son("src/resources/Sources_sonores/Bruit.wav");
        for (int i = 0; i < newAudioData.donnees().length / blockSize; i++) {
            float[] audioBlock = newAudioData.bloc_deTaille(i, blockSize);
            Complexe[] signal = new Complexe[audioBlock.length];
            for (int j = 0; j < audioBlock.length; j++) {
                signal[j] = new ComplexeCartesien(audioBlock[j], 0);
            }
            Complexe[] frequencyComponents = FFTCplx.appliqueSur(signal);

            // Convert the frequency components to inputs for the neuron
            float[] inputs2 = new float[blockSize];
            for (int j = 0; j < blockSize; j++) {
                inputs2[j] = (float) frequencyComponents[j].mod();
            }

            // Use the neuron to classify the inputs
            neuron.metAJour(inputs2);
            float output = neuron.sortie();

            // Output the classification result
            System.out.println("Output for block " + i + ": " + output);

            // Analyze the output to determine if the trained waveform is present in the new audio data block (by making a decision based on the output value ratio)
            if (output > 0.5) {
                count++;
            }
        }
        // Analyze the classification results
        float detectionRatio = (float) count / (newAudioData.donnees().length / blockSize);
        boolean waveformDetected = detectionRatio > 0.5; // Adjust this threshold as needed

        // Output the final decision
        System.out.println("Detection ratio: " + detectionRatio);
        System.out.println("Trained waveform detected: " + waveformDetected);

    }
}