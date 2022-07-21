import java.util.ArrayList;
import java.util.HashMap;


public class Node {

    //A faire le alpha et le beta.
    private HashMap <String, Integer> etatCadre;
    ArrayList <Node> children = new ArrayList<Node>();
    int symboleDuJoueurActuel; // 4= X ou 2 = O.
    //The players current decision where to place his symbole "Key".
    String decision;
    //The risk factor of the potential decision. 
    int pointage;
    //Le meilleur pointage du succeseur du noeud MAX (le noed pour lequel le tour a jouer et notre joueur) 
    int alpha;
    //Inverse pour le noed MIN.
    int beta;
    //Identify if it is a Max or Min node
    String typeNode;
    //Pour determiner quand arreter la creation de l'arbre.
    int heuristiqueCounter;

    public Node(){
        etatCadre = new HashMap<>();
    }

    public Node(HashMap <String, Integer> etatCadre){
        this.etatCadre = etatCadre;
        pointage = (int)(Math.random() * 100);
    }
    public Node(HashMap <String, Integer> etatCadre, int symboleDuJoueurActuel){
        this.etatCadre = etatCadre;
        pointage = (int)(Math.random() * 100);
        this.symboleDuJoueurActuel = symboleDuJoueurActuel;
    }

    //Recopier le etat du cadre mais faire une seul changement dans d'une position pour dessiner un scenario potentiel. 
    public Node creatAChild(String key, Integer symboleDuJoueurActuel, int heuristiqueCounter){
        Node n = new Node(etatCadre,symboleDuJoueurActuel);
        n.replace(key, symboleDuJoueurActuel);
        n.decision = key;
        children.add(n);
        
        this.heuristiqueCounter = heuristiqueCounter;
        if(this.typeNode.equals("Max")){
            n.typeNode = "Min";
        }else if(this.typeNode.equals("Min")){
            n.typeNode = "Max";
        }
        return n;
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
    public void setSymbole(int symboleDuJoueurActuel){
        this.symboleDuJoueurActuel = symboleDuJoueurActuel;
    }
}
