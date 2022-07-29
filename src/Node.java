import java.util.ArrayList;
import java.util.HashMap;


public class Node {

    //A faire le alpha et le beta.
    HashMap <String, Integer> etatCadre = new HashMap<>();;
    int[][] board = null;
    ArrayList <Node> position;
    ArrayList <Node> children = new ArrayList<Node>();
    int symboleDuJoueurActuel; // 4= X ou 2 = O.
    //The players current decision where to place his symbole "Key".
    String decision;
    //The risk factor of the potential decision. 
    int pointage ;
    //Le meilleur pointage du succeseur du noeud MAX (le noed pour lequel le tour a jouer et notre joueur) 
    int alpha;
    //Inverse pour le noed MIN.
    int beta;
    //Identify if it is a Max or Min node
    String typeNode;
    //Pour determiner quand arreter la creation de l'arbre.
    int heuristiqueCounter;

    boolean isLeaf = false;
    
    public Node(){}

    public Node(HashMap <String, Integer> etatCadre){
        this.etatCadre.putAll(etatCadre);
        
    }
    public Node(HashMap <String, Integer> etatCadre, int symboleDuDernierJoeur){
        this.etatCadre.putAll(etatCadre);
       
        this.symboleDuJoueurActuel = JeuUtils.obtenirIdSymboleAdverse(symboleDuDernierJoeur);
    }

    //Recopier le etat du cadre mais faire une seul changement dans d'une position pour dessiner un scenario potentiel. 
    public Node creatAChild(String key){
       
        Node n = new Node(etatCadre,JeuUtils.obtenirIdSymboleAdverse(this.symboleDuJoueurActuel));
        n.replace(key, n.symboleDuJoueurActuel);
        n.decision = key;
        children.add(n);
        n.heuristiqueCounter = this.heuristiqueCounter + 1;

        
        this.isLeaf = false;
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

    public int[][] array2DBoard(){
        return this.board = JeuUtils.mapTo2DArray(this.etatCadre);
    }


    //A être setter a chaque fois qu'un nouveau niveau s'ajoute dans l'arbre.
    public void setSymbole(int symboleDuJoueurActuel){
        this.symboleDuJoueurActuel = symboleDuJoueurActuel;
    }
}
