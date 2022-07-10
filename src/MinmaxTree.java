import java.util.Arrays;
import java.util.HashMap;

//Tree a interoger pour savoir les pourcentages de ganger pour chaque coups possible. Il faut recréer le tree
//après chaque coup. Donc le tree n'est pas statique.
//Il faut reécrire dans le tree les positions et options possible dans le case 3x3 envisager. 
//Le tree envoie le meilleur choix possible après des calcules internes.
public class MinmaxTree {

    private HashMap <String, Integer> _etatCases = new HashMap<String, Integer>();
    int[] cadre;
    String _symboleDuJoeur;//X ou O.
    Node rootNode;

    //Les 3 cadres de bas sur le plateau de jeu
    private static final String[] cadreBasGauche = {"A1","A2","A3","B1","B2","B3","C1","C2","C3"};
    private static final String[] cadreBasMilieu = {"D1","D2","D3","E1","E2","E3","F1","F2","F3"};
    private static final String[] cadreBasDroite = {"G1","G2","G3","H1","H2","H3","I1","I2","I3"};
        
    //Les 3 cadres de centre sur le plateau de jeu
    private static final String[] cadreGauche = {"A4","A5","A6","B4","B5","B6","C4","C5","C6"};
    private static final String[] cadreMilieu = {"D4","D5","D6","E4","E5","E6","F4","F5","F6"};
    private static final String[] cadreDroite = {"G4","G5","G6","H4","H5","H6","I4","I5","I6"};

    //Les 3 cadres de haut sur le plateau de jeu
    private static final String[] cadreHautGauche = {"A7","A8","A9","B7","B8","B9","C7","C8","C9"};
    private static final String[] cadreHautMilieu = {"D7","D8","D9","E7","E8","E9","F7","F8","F9"};
    private static final String[] cadreHautDroite = {"G7","G8","G9","H7","H8","H9","I7","I8","I9"};
    
    private static final String[][][] CASES_GRILLE_JEU = {
        {cadreBasGauche, cadreGauche, cadreHautGauche}, //colonne 1
        {cadreBasMilieu, cadreMilieu, cadreHautMilieu}, // colonne 2
        {cadreBasDroite, cadreDroite, cadreHautDroite} // colonne 3
    };

    public MinmaxTree(String symboleDuJoueur, HashMap<String, Integer> etatGrilleJeu) {
        this._symboleDuJoeur = symboleDuJoueur;
        this._etatCases.putAll(etatGrilleJeu);
    }

    //Créer le tree à partire de l'état du cadre courante. 
    //Dépendant de la liste de position à checker donné, la methode va interoger le hashMap pour
    //savoir l'état et construire l'arbre.
    //Il va construir l'état actuel avec des enfant designants les états future dependant des choix fait.
    //Il doit être capable de générer les hashmaps à partie des hashmaps. 
    public void creatTree(int[] interval){
        String positions[] = obtenirListeCases(interval);
        System.out.println("Positions possibles: "+Arrays.toString(positions));
        rootNode = new Node();
        Integer value;
        //for loop pour interoger le hashmap pour avoir info sur le cadre en question.
        for(int i = 0; i < positions.length; i++ ){
            value = this._etatCases.get(positions[i]);
            rootNode.put(positions[i],value);
        }
        creatChildren(rootNode);
    }

    //Pour chaque case vide, créer un scenario possible avec le choix fait.
    private void creatChildren(Node node) {
        //Le cadre actuel dans le noed est un Hashmap
        for (HashMap.Entry<String, Integer> entry : node.getMap().entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            if(value == 0){
                // value =  Integer.valueOf(_symboleDuJoeur); 
                value = Utils.obtenirIdSymboleJoueur(this._symboleDuJoeur);
                node.creatAChild(key, value);
            }
        }
        //Pour chaque prediction de tour du joueur, changer le symmbole pour essayer de prédire l'action de l'adversaire.
        if(_symboleDuJoeur.equals("X")){
            _symboleDuJoeur = "O";
        }else if(_symboleDuJoeur.equals("O")){
            _symboleDuJoeur = "X";
        }
        //while this node has children, call this function recursivvely.
        for(int i = 0; i < node.children.size(); i++ ){
            creatChildren(node.children.get(i));
        }
    }

    //recois la case et retourn l'identification de la position corespondant à la case.
    public String genererMeilleurDecision(int[] cadre){
        //Random number in the interval of the root node children arraylist.
        int rand = (int)(Math.random() * 10);
         int index = rand % rootNode.children.size(); // rand % 9
         return rootNode.children.get(index).desision;
    }

    // public void setEtatCases(HashMap <String, Integer> etatCases){
    //     this._etatCases = etatCases;
    // }

    //Envoie une liste de tous les combinaison du cadre approprié 
    /**
     * 
     * @param cadre index du cadre. cadre[0] = rangee, cadre[1] = colonne
     * @return
     */
    private String[] processInterval(int[] cadre){
        
        if(cadre[1] == 1 ){//Si la ligne est la première
            if(cadre[0] == 1){//Si la colone est la première (celui du plus bas)
                return cadreBasGauche;
            }else if(cadre[0] == 2){
                return cadreBasMilieu;
            }else if(cadre[0] == 3){
                return cadreBasDroite;
            }
            
        }else if(cadre[1] == 2){
            if(cadre[0] == 1){//Si la colone est la deuxième (celui du plus bas)
                return cadreGauche;
            }else if(cadre[0] == 2){
                return cadreMilieu;
            }else if(cadre[0] == 3){
                return cadreDroite;
            }
        }else if(cadre[1] == 3){
            if(cadre[0] == 1){//Si la colone est la deuxième (celui du plus bas)
                return cadreHautGauche;
            }else if(cadre[0] == 2){
                return cadreHautMilieu;
            }else if(cadre[0] == 3){
                return cadreHautDroite;
            }
        }

        
        
        return null;
    }

    public void retirerCadre(int[] cadre) {
        String[] casesCadre = CASES_GRILLE_JEU[cadre[0]-1][cadre[1]-1];
        for (String uneCase : casesCadre) {
            this._etatCases.remove(uneCase);
        }
    }

    public String[] obtenirListeCases(int[] cadre) {

        if (cadre[0] == -1 && cadre[1] == -1) {
           return this._etatCases.keySet().toArray( new String[this._etatCases.size()] );
        }
        return CASES_GRILLE_JEU[cadre[0]-1][cadre[1]-1];
    }
}




/*
 * Memo:
 * 
 * Pour chaque enfant du Node, setter le nouveau symbole et creer des enfants pour chaque enfant des autre desision possible.
 * - Creat the new Node
 * - Set the symbole
 * - add node to the children list of parent
 * - 
 * - Create the scenario
 *  - Recursive call until no more "0" in the cadre.
 */