import java.util.ArrayList;
import java.util.HashMap;


public class Node {

    //A faire le alpha et le beta.
    private HashMap <String, Integer> _etatCadre = new HashMap<String, Integer>();
    ArrayList <Node> children = new ArrayList<Node>();
    private String symboleDuJoueurActuel; // X ou Y. //index of the player of interest
    //The decision of the current potential scenario;
    String desision; //action taken by player
    int value = 0;
    //index of enemy
    //action taken by other enemy


    public Node(){ }

    public Node(HashMap <String, Integer> etatCadre){
        this._etatCadre = etatCadre;
        System.out.println(this._etatCadre.toString());
        Utils.dessiner_grille_jeu(etatCadre);
    }

    //Recopier le etat du cadre mais faire une seul changement dans d'une position pour dessiner un scenario potentiel. 
    public void creatAChild(String key, Integer value){
        Node n = new Node(_etatCadre);
        n.replace(key, value);
        n.desision = key;
        children.add(n);

        
    }

    /**
     * Ajouter une case a la liste de possibilites de ce noeud
     * @param key position de la case
     * @param value etat de la case
     */
    public void put(String key, Integer value){
        _etatCadre.put(key, value);
    }

    /**
     * Modifier l'etat d'une case
     * @param key position de la case
     * @param value etat de la case
     */
    public void replace(String key, Integer value){
        _etatCadre.replace(key, value);
    }

    public HashMap <String, Integer> getMap(){
        return _etatCadre;
    }

    //A être setter a chaque fois qu'un nouveau niveau s'ajoute dans l'arbre.
    // public void setSymbole(String symboleDuJoueurActuel){
    //     this.symboleDuJoueurActuel = symboleDuJoueurActuel;
    // }

}
