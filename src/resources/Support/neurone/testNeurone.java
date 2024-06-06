package resources.Support.neurone;

import java.util.Random;

public class testNeurone
{
 public static void main(String[] args)
 {
  final float[][] entrees = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
  final float[] resultats = {0, 0, 0, 1};

  // final iNeurone n = new NeuroneSigmoid(entrees[0].length);
   final iNeurone n = new NeuroneHeaviside(entrees[0].length);
  // final iNeurone n = new Neurone(entrees[0].length);
  // final iNeurone n = new NeuroneReLu(entrees[0].length);
   //final iNeurone n = new NeuroneTanh(entrees[0].length);

  System.out.println("Apprentissage…");
  System.out.println("Nombre de tours : "+n.apprentissage(entrees, resultats));

  final Neurone vueNeurone = (Neurone)n;
  System.out.print("Synapses : ");
  for (final float f : vueNeurone.synapses())
   System.out.print(f+" ");
  System.out.print("\nBiais : ");
  System.out.println(vueNeurone.biais());

  for (int i = 0; i < entrees.length; ++i)
  {
   final float[] entree = entrees[i];
   n.metAJour(entree);
   System.out.println("Entree "+i+" : "+n.sortie());
  }

  statistiquesPoids(vueNeurone);
  ajouterBruit(vueNeurone, 0.1f);
  System.out.println("Apprentissage…");
  System.out.println("Nombre de tours : "+n.apprentissage(entrees, resultats));
  System.out.print("Synapses : ");
  for (final float f : vueNeurone.synapses())
   System.out.print(f+" ");
  System.out.print("\nBiais : ");
  System.out.println(vueNeurone.biais());
  for (int i = 0; i < entrees.length; ++i)
  {
   final float[] entree = entrees[i];
   n.metAJour(entree);
   System.out.println("Entree "+i+" : "+n.sortie());
  }
  statistiquesPoids(vueNeurone);
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

 public static void ajouterBruit(Neurone vueNeurone, float amplitude) {
  Random rand = new Random();
  for (int i = 0; i < vueNeurone.synapses().length; ++i) {
   vueNeurone.synapses()[i] += amplitude * (rand.nextFloat() * 2 - 1);
  }
 }
}