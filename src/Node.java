import java.util.ArrayList;
import java.util.HashMap;


public class Node {

    //A faire le alpha et le beta.
    private HashMap <String, Integer> etatCadre;
    private ArrayList <Node> children;
    private String symboleDuJoueurActuel; // X ou Y.

    public Node(){
        etatCadre = new HashMap<>();
    }

    public Node(HashMap <String, Integer> etatCadre){
        this.etatCadre = etatCadre;
    }


    //Recopier le etat du cadre mais faire une seul changement dans d'une position pour dessiner un scenario potentiel. 
    public void creatAChild(String key, Integer value){
        Node n = new Node(etatCadre);
        n.replace(key, value);
        children.add(n);

        
    }

    public void put(String key, Integer value){
        etatCadre.put(key, value);
    }

    public void replace(String key, Integer value){
        etatCadre.replace(key, value);
    }

    public HashMap <String, Integer> getMap(){
        return etatCadre;
    }

    //A être setter a chaque fois qu'un nouveau niveau s'ajoute dans l'arbre.
    public void setSymbole(String symboleDuJoueurActuel){
        this.symboleDuJoueurActuel = symboleDuJoueurActuel;
    }

}
