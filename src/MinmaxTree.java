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
    public void creatTree(int[] interval ){
        String[] positions = obtenirListeCases(interval);
        int symboleAdversaire = JeuUtils.obtenirIdSymboleAdverse(symboleDuJoeur);
        System.out.println("Positions possibles: " + Arrays.toString(positions));
        rootNode = new Node();
        rootNode.setSymbole(symboleAdversaire);
        rootNode.typeNode = "Max";
        rootNode.heuristiqueCounter = 0;
        

        Integer value;
        //for loop pour interoger le hashmap pour avoir info sur le cadre en question.
        for(int i = 0; i < positions.length; i++ ){
            value = this._etatCases.get(positions[i]);
            rootNode.put(positions[i],value);
            // working System.out.println("Key:" + positions[i]);
        }
        for (HashMap.Entry<String, Integer> entry : rootNode.getMap().entrySet()) {
            String key = entry.getKey();
            Integer value2 = entry.getValue();
          
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
                //System.out.println("Root Node cadre--- :" + rootNode.getMap().toString());
                //value = JeuUtils.obtenirIdSymboleAdverse(node.symboleDuJoueurActuel);
                Node n = node.creatAChild(key);
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
    public void minimax(Node n, int joueur){
        /* TO DO: For each children of parent node, recursive call until you find a leaf, give the leaf a score and return the score,
         * For each child leaf a score is returned, depending if the parent is a Min or Max player keep the appropriate score fromm all their children...
         */
        n.array2DBoard();
        for(int i = 0; i < n.children.size(); i++){

        }
        
        if(n.isLeaf == true){
            //check the node`s board and wright in it a score and return the score
            //return score;
        }
        //call a score creator function that will examin the cadre and give it a score according to Max players symbole. Make the score follow up the tree
    }

    /*
     * Big function to detrmine what score to give the leaf cadre scenario
     */
    private int scoreCalculator(int[][] board, int joueurActuel){
        int joueurAdverse = JeuUtils.obtenirIdSymboleAdverse(joueurActuel);
        int score =0;
        if(board[0][0] != joueurAdverse){
            //if isTripplet or twin(twin has to check if third case is empty), for all directions from starting point then score is added
            if(board[0][0] == joueurActuel){
                if(board[0][1] == joueurActuel && board[0][2] == joueurActuel){//TictacToe first ligne.

                }else if(board[0][1] == joueurActuel && board[0][2] == 0 ){ //first ligne XX_
                //give a score for a twin
                }else if(board[0][1] == 0 && board[0][2] == joueurActuel){ //First ligne variant X_X

                }else if(board[1][1] == joueurActuel && board[2][2] == joueurActuel){//Tictactoe Diagonal from top left corner

                }else if(board[1][1] == 0 && board[2][2] == joueurActuel){//Diagonal from top left corner. 

                }else if(board[1][1] == joueurActuel && board[2][2] == 0){//Diagonal from top left corner variant. 

                }else if(board[1][0] == joueurActuel && board[2][0] == joueurActuel){//Tictacttoe first colomn

                }else if(board[1][0] == 0 && board[2][0] == joueurActuel){//First colomn
                    
                }else if(board[1][0] == joueurActuel && board[2][0] == 0){//First colomn variant

                }
            }else if(board[0][0] == 0){
                if(board[0][1] == joueurActuel && board[0][2] == 0 ){ //first ligne XX_
                    //give a score for a twin
                }else if(board[0][1] == joueurActuel && board[0][2] == joueurActuel){ //First ligne  _XX
    
                }else if(board[1][1] == joueurActuel && board[2][2] == joueurActuel){//Diagonal from top left corner . 
    
                }else if(board[1][0] == joueurActuel && board[2][0] == joueurActuel){//First colomn
                        
                }
         
            }
        }else if(board[1][0] != joueurAdverse){
            if(board[1][0] == joueurActuel){
                
                if(board[1][1] == joueurActuel && board[1][2] == joueurActuel){// tictactoe seond ligne

                }else if(board[1][1] == joueurActuel && board[1][2] == 0){//Second ligne XX_

                }else if(board[1][1] == 0 && board[1][2] == joueurActuel){//Second ligne variant X_X

                }
            }else if(board[1][0] == 0 && board[1][1] == joueurActuel && board[1][2] == 0){//Second ligne _XX

            }

        }else if(board[2][0] != joueurAdverse){
            if(board[2][0] == joueurActuel){
                if(board[2][1] == joueurActuel && board[2][2] == joueurActuel){//Tictacttoe third ligne

                }else if(board[2][1] == joueurActuel && board[2][2] == 0){ //Third ligne XX_

                }else if(board[2][1] == 0 && board[2][2] == joueurActuel){//Third ligne X_X

                }
            }else if(board[2][0] == 0){
                
                 if(board[2][1] == joueurActuel && board[2][2] == joueurActuel){//Third ligne _XX

                }
            }
        }else if(board[0][1] != joueurAdverse){
            if(board[0][1] == joueurActuel){
                if(board[1][1] == joueurActuel && board[2][1] == joueurActuel){//Tictacttoe midlle colomn
            
                }else if(board[1][1] == joueurActuel && board[2][1] == 0){//Second Colomn XX_

                }else if(board[1][1] == 0 && board[2][1] == joueurActuel){//Second colomn variant X_X

                }
            }else if(board[0][1] == 0){
                if(board[1][1] == joueurActuel && board[2][1] == joueurActuel){//Second colomn variant _XX

                }
            }
        }else if(board[0][2] != joueurAdverse){
            if(board[0][2] == joueurActuel){
                if(board[1][2] == joueurActuel && board[2][2] == joueurActuel){//Tictactoe third colomn

                }else if(board[1][1] == joueurActuel && board[2][0] == joueurActuel){//Tictactoe Diagonal top right corner

                }else if(board[1][2] == joueurActuel && board[2][2] == 0){//Third colomn XX_

                }else if(board[1][2] == 0 && board[2][2] == joueurActuel){//Third Colomn variant X_X

                }
            }else if(board[0][2] == 0){
                if(board[1][2] == joueurActuel && board[2][2] == joueurActuel){//third colomn variant XX_
                    
                }
            }
        }else if (board[0][0] == joueurAdverse || board[1][0] == joueurAdverse || board[2][0] == joueurAdverse || board[0][1] == joueurAdverse || board[0][2] == joueurAdverse){
            score = -1 * scoreCalculator(board,joueurAdverse);
        }else{
            score = 1;
        }

    
    return score;
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


