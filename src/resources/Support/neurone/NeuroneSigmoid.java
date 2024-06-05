package resources.Support.neurone;

public class NeuroneSigmoid extends Neurone
{
    // Fonction d'activation d'un neurone (peut facilement être modifiée par héritage)
    protected float activation(final float valeur) {return 1.f/(1.f+(float)Math.exp(-valeur));}

    // Constructeur
    public NeuroneSigmoid(final int nbEntrees) {super(nbEntrees);}
}
