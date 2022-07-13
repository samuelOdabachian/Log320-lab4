import java.util.Arrays;
import java.util.HashMap;

//Tree a interoger pour savoir les pourcentages de ganger pour chaque coups possible. Il faut recréer le tree
//après chaque coup. Donc le tree n'est pas statique.
//Il faut reécrire dans le tree les positions et options possible dans le case 3x3 envisager. 
//Le tree envoie le meilleur choix possible après des calcules internes.
public class MinmaxTree {

    private HashMap <String, Integer> _etatCases = new HashMap<String, Integer>();
    int[] cadre;
    int _idSymboleJoueur;
    Node rootNode;

    // public MinmaxTree(String symboleDuJoueur, HashMap<String, Integer> etatGrilleJeu) {
    //     this._symboleDuJoeur = symboleDuJoueur;
    //     this._etatCases.putAll(etatGrilleJeu);
    // }

    public MinmaxTree(int idSymboleDuJoueur, HashMap<String, Integer> etatGrilleJeu) {
        this._idSymboleJoueur = idSymboleDuJoueur;
        this._etatCases = etatGrilleJeu;
    }

    //Créer le tree à partire de l'état du cadre courante. 
    //Dépendant de la liste de position à checker donné, la methode va interoger le hashMap pour
    //savoir l'état et construire l'arbre.
    //Il va construir l'état actuel avec des enfant designants les états future dependant des choix fait.
    //Il doit être capable de générer les hashmaps à partie des hashmaps. 
    public void creatTree(int[] interval){
        String positions[] = obtenirListeCases(interval);
        System.out.println("Positions/Cases possibles: "+Arrays.toString(positions));
        rootNode = new Node();
        //for loop pour interoger le hashmap pour avoir info sur le cadre en question.
        for(int i = 0; i < positions.length; i++ ){
            int etatCase = this._etatCases.get(positions[i]); // etatCase = 2, 4 ou 0
            rootNode.put(positions[i], etatCase);
        }
        creatChildren(rootNode);
        System.out.println(rootNode.getMap().toString());
    }

    //Pour chaque case vide, créer un scenario possible avec le choix fait.
    private void creatChildren(Node node) {
        //Le cadre actuel dans le noed est un Hashmap
        for (HashMap.Entry<String, Integer> entry : node.getMap().entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            if(value == 0){
                // value =  Integer.valueOf(_symboleDuJoeur); 
                // value = Utils.obtenirIdSymboleJoueur(this._symboleDuJoeur);
                value = this._idSymboleJoueur;
                node.creatAChild(key, value);
            }
        }
        //Pour chaque prediction de tour du joueur, changer le symmbole pour essayer de prédire l'action de l'adversaire.
        this._idSymboleJoueur = Utils.obtenirIdAdversaire(this._idSymboleJoueur);
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
                return Utils.cadreBasGauche;
            }else if(cadre[0] == 2){
                return Utils.cadreBasMilieu;
            }else if(cadre[0] == 3){
                return Utils.cadreBasDroite;
            }
            
        }else if(cadre[1] == 2){
            if(cadre[0] == 1){//Si la colone est la deuxième (celui du plus bas)
                return Utils.cadreGauche;
            }else if(cadre[0] == 2){
                return Utils.cadreMilieu;
            }else if(cadre[0] == 3){
                return Utils.cadreDroite;
            }
        }else if(cadre[1] == 3){
            if(cadre[0] == 1){//Si la colone est la deuxième (celui du plus bas)
                return Utils.cadreHautGauche;
            }else if(cadre[0] == 2){
                return Utils.cadreHautMilieu;
            }else if(cadre[0] == 3){
                return Utils.cadreHautDroite;
            }
        }

        
        
        return null;
    }

    public void retirerCadre(int[] cadre) {
        String[] casesCadre = Utils.CASES_GRILLE_JEU[cadre[0]-1][cadre[1]-1];
        for (String uneCase : casesCadre) {
            this._etatCases.remove(uneCase);
        }
    }

    public String[] obtenirListeCases(int[] cadre) {

        if (cadre[0] == -1 && cadre[1] == -1) {
           return this._etatCases.keySet().toArray( new String[this._etatCases.size()] );
        }
        return Utils.CASES_GRILLE_JEU[cadre[0]-1][cadre[1]-1];
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