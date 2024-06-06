package resources.Support.neurone;

public class NeuroneTanh extends Neurone
{
    // Fonction d'activation d'un neurone (peut facilement être modifiée par héritage)
    protected float activation(final float valeur) {return (float)Math.tanh(valeur);}

    // Constructeur
    public NeuroneTanh(final int nbEntrees) {super(nbEntrees);}
    
}