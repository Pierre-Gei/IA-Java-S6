package resources.Support.neurone;

public class NeuroneReLu extends Neurone
{
    // Fonction d'activation d'un neurone (peut facilement être modifiée par héritage)
    protected float activation(final float valeur) {return Math.max(0.f, valeur);}

    // Constructeur
    public NeuroneReLu(final int nbEntrees) {super(nbEntrees);}
}
