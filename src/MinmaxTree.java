import java.util.Arrays;
import java.util.HashMap;

//Tree a interoger pour savoir les pourcentages de ganger pour chaque coups possible. Il faut recréer le tree
//après chaque coup. Donc le tree n'est pas statique.
//Il faut reécrire dans le tree les positions et options possible dans le case 3x3 envisager. 
//Le tree envoie le meilleur choix possible après des calcules internes.
public class MinmaxTree {

    private HashMap <String, Integer> _etatCases = new HashMap<String, Integer>();
    int[] cadre;
    int symboleDuJoeur;//X ou O.
    Node rootNode;

    public MinmaxTree(int symboleDuJoueur, HashMap<String, Integer> etatGrilleJeu) {
        this.symboleDuJoeur = symboleDuJoueur;
        this._etatCases.putAll(etatGrilleJeu);
        // this._etatCases = etatGrilleJeu;
    }

    //Créer le tree à partire de l'état du cadre courante. 
    //Dépendant de la liste de position à checker donné, la methode va interoger le hashMap pour
    //savoir l'état et construire l'arbre.
    //Il va construir l'état actuel avec des enfant designants les états future dependant des choix fait.
    //Il doit être capable de générer les hashmaps à partie des hashmaps. 
    public void creatTree(int[] interval, int symboleAdversaire){
        String[] positions = obtenirListeCases(interval);
        System.out.println("Positions possibles: " + Arrays.toString(positions));
        rootNode = new Node();
        rootNode.setSymbole(symboleAdversaire);
        rootNode.typeNode = "max";
        rootNode.heuristiqueCounter = 0;
       
        Integer value;
        //for loop pour interoger le hashmap pour avoir info sur le cadre en question.
        for(int i = 0; i < positions.length; i++ ){
            value = this._etatCases.get(positions[i]);
            rootNode.put(positions[i],value);
        }
        creatChildren(rootNode);
    }

    //Pour chaque case vide, créer un scenario possible avec le choix fait. Configurer le alpha et beta aussi
    private void creatChildren(Node node) {
        //Le cadre actuel dans le noed est un Hashmap
        for (HashMap.Entry<String, Integer> entry : node.getMap().entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            
            //Si heuristique est atteind, donc areter la creation de l'arbre.
            if(value == 0 && node.heuristiqueCounter != 2){
                value = JeuUtils.obtenirIdSymboleAdverse(node.symboleDuJoueurActuel);
                Node n = node.creatAChild(key, value, node.heuristiqueCounter + 1);
                //Recursively creat children as long as the heuristic permits.
                creatChildren(n);
            }
            //Set le alpha et le beta en identifiant si c'est un noed max ou min.
            
            if (node.typeNode != null) {
                if(node.typeNode.equals("max")){
                    node.alpha = trouverPointageMax(node);
                    // System.out.println("alpha: "+node.alpha);
                }else if(node.typeNode.equals("min")){
                    node.beta = trouverPointageMin(node);
                    // System.out.println("beta: "+node.beta);
                }
            }
        }
    }
    //Determine le score de chaque noed. 100, -100 ou 0;
    //100 = Max a ganger,    -100 = Min a gagner    0 = egalité.
    private void minimax(Node n){
        if(n.isLeaf == true){
            //return something.
        }
        //call a score creator function that will examin the cadre and give it a score according to Max players symbole.
    }

    //Trouve le pointage min des enfant d'un noed MIN  
    public int trouverPointageMin(Node n){
        int beta = Integer.MAX_VALUE;
        for(int i = 0; i < n.children.size(); i++){
            if(n.children.get(i).pointage <= beta){
                beta = n.children.get(i).pointage;
            }
        }
        return beta;
    }

    //Trouve le pointage max des enfants d'un noed MAX 
    public int trouverPointageMax(Node n){
        int alpha = Integer.MIN_VALUE;
        for(int i = 0; i < n.children.size(); i++){
            if(n.children.get(i).pointage >= alpha){
                alpha = n.children.get(i).pointage;
            }
        }
        return alpha;
    }

    //recois la case et retourn l'identification de la position corespondant à la case.
    //Generer la meilleur decision grace à l'alpha beta
    public String genererMeilleurDecision(int[] cadre){
        //Random number in the interval of the root node children arraylist.
        int rand = (int)(Math.random() * 10);
         int index = rand % rootNode.children.size(); // rand % 9
         return rootNode.children.get(index).decision;
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
    /*private String[] processInterval(int[] cadre){
        
        if(cadre[1] == 1 ){//Si la ligne est la première
            if(cadre[0] == 1){//Si la colone est la première (celui du plus bas)
                return JeuUtils.cadreBasGauche;
            }else if(cadre[0] == 2){
                return JeuUtils.cadreBasMilieu;
            }else if(cadre[0] == 3){
                return JeuUtils.cadreBasDroite;
            }
            
        }else if(cadre[1] == 2){
            if(cadre[0] == 1){//Si la colone est la deuxième (celui du plus bas)
                return JeuUtils.cadreGauche;
            }else if(cadre[0] == 2){
                return JeuUtils.cadreMilieu;
            }else if(cadre[0] == 3){
                return JeuUtils.cadreDroite;
            }
        }else if(cadre[1] == 3){
            if(cadre[0] == 1){//Si la colone est la deuxième (celui du plus bas)
                return JeuUtils.cadreHautGauche;
            }else if(cadre[0] == 2){
                return JeuUtils.cadreHautMilieu;
            }else if(cadre[0] == 3){
                return JeuUtils.cadreHautDroite;
            }
        }
        return null;
    }*/

    public void retirerCadre(int[] cadre) {
        String[] casesCadre = JeuUtils.CASES_GRILLE_JEU[cadre[0]-1][cadre[1]-1];
        for (String uneCase : casesCadre) {
            this._etatCases.remove(uneCase);
        }
    }

    public String[] obtenirListeCases(int[] cadre) {

        if (cadre[0] == -1 && cadre[1] == -1) {
           return this._etatCases.keySet().toArray( new String[this._etatCases.size()] );
        }
        return JeuUtils.CASES_GRILLE_JEU[cadre[0]-1][cadre[1]-1];
    }
}


/*
 * Memo:
 * 
 * Pour chaque enfant du Node, setter le nouveau symbole et creer des enfants pour chaque enfant des autre decision possible.
 * - Creat the new Node
 * - Set the symbole
 * - add node to the children list of parent
 * - 
 * - Create the scenario
 *  - Recursive call until no more "0" in the cadre.
 */