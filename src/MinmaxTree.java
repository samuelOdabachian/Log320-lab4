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
        System.out.println("Positions possibles: " + Arrays.toString(positions));
        rootNode = new Node();
        rootNode.setSymbole(symboleDuJoeur);
        rootNode.typeNode = "Max";
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
    public int minimax(Node n, String typeJoueur){
        /* TO DO: For each children of parent node, recursive call until you find a leaf, give the leaf a score and return the score,
         * For each child leaf a score is returned, depending if the parent is a Min or Max player keep the appropriate score fromm all their children...
         */
        Node nChild;
        int score = 0;
        int maxScore =-100;
        int minScore = 100;
        
    

        if(n.isLeaf == true){
            //check the node`s board and wright in it a score and return the score
                
            score = scoreCalculator(n.array2DBoard(),n.symboleDuJoueurActuel, 1);
            n.pointage = score;
            JeuUtils.detailsNode(n);
            if(n.typeNode.equals("Min")){
                score = -1* score; 
            }
            return score;
        }

        if(typeJoueur.equals("Max")){
            
            
            for(int i = 0; i < n.children.size(); i ++){
                nChild = n.children.get(i);
                score = minimax(nChild,"Min");
                maxScore = Math.max(maxScore,score);
            }
            n.pointage = maxScore;
          
            return maxScore;
        }else if(typeJoueur.equals("Min")){
            

            for(int i = 0; i < n.children.size(); i ++){
                nChild = n.children.get(i);
              
                score = minimax(nChild,"Max");
                minScore = Math.min(minScore,score);
            }
            n.pointage = minScore;
            
            return minScore;
        } 

        
        
        return score;
        
      }

    /*
     * Big function to detrmine what score to give the leaf cadre scenario
     */
    public int scoreCalculator(int[][] board, int joueurActuel, int limiter){
        int joueurAdverse = JeuUtils.obtenirIdSymboleAdverse(joueurActuel);
        int score =0;
        if(board[0][0] != joueurAdverse){
            //if isTripplet or twin(twin has to check if third case is empty), for all directions from starting point then score is added
            if(board[0][0] == joueurActuel){
                if(board[0][1] == joueurActuel && board[0][2] == joueurActuel){//TictacToe first ligne. 5 points
                    System.out.println(board[0][1]+ " " + board[0][2] + " " +  joueurActuel);
                    return score = 5;
                }else if(board[0][1] == joueurActuel && board[0][2] == 0){ //first ligne XX_
                    //give a score for a twin
                    score = 2;
                    if(board[0][2] == joueurAdverse){score = -1;}
                }else if(board[0][1] == 0 && board[0][2] == joueurActuel){ //First ligne variant X_X
                    score = 2;
                    if(board[0][1] == joueurAdverse){score = -1;}
                }else if(board[1][1] == joueurActuel && board[2][2] == joueurActuel){//Tictactoe Diagonal from top left corner
                    return score = 5;
                }else if(board[1][1] == 0 && board[2][2] == joueurActuel){//Diagonal twin from top left corner. 
                    score = 2;
                    if(board[1][1] == joueurAdverse){score = -1;}
                }else if(board[1][1] == joueurActuel && board[2][2] == 0){//Diagonal twin from top left corner variant. 
                    score = 2;
                }else if(board[1][0] == joueurActuel && board[2][0] == joueurActuel){//Tictacttoe first colomn
                    return score = 5;
                }else if(board[1][0] == 0 && board[2][0] == joueurActuel){//Twin first colomn
                    score = 2;
                }else if(board[1][0] == joueurActuel && board[2][0] == 0){//Twin first colomn variant
                    score = 2;
                }
            }else if(board[0][0] == 0){
                if(board[0][1] == joueurActuel && board[0][2] == joueurActuel){ //First ligne  _XX
                    score = 2;
                }else if(board[1][1] == joueurActuel && board[2][2] == joueurActuel){//Diagonal from top left corner . 
                    score = 2;
                }else if(board[1][0] == joueurActuel && board[2][0] == joueurActuel){//First colomn
                    score = 2;
                }
         
            }
        }if(board[1][0] != joueurAdverse){
            if(board[1][0] == joueurActuel){
                
                if(board[1][1] == joueurActuel && board[1][2] == joueurActuel){// tictactoe seond ligne
                    return score = 5;
                }else if(board[1][1] == joueurActuel && board[1][2] == 0){//Second ligne XX_
                    score = 2;
                }else if(board[1][1] == 0 && board[1][2] == joueurActuel){//Second ligne variant X_X
                    score = 2;
                }
            }else if(board[1][0] == 0 && board[1][1] == joueurActuel && board[1][2] == joueurActuel){//Second ligne _XX
                score = 2;
            }

        }if(board[2][0] != joueurAdverse){
            if(board[2][0] == joueurActuel){
                if(board[2][1] == joueurActuel && board[2][2] == joueurActuel){//Tictacttoe third ligne
                    return score = 5  ;
                }else if(board[2][1] == joueurActuel && board[2][2] == 0){ //Third ligne XX_
                    score = 2;
                }else if(board[2][1] == 0 && board[2][2] == joueurActuel){//Third ligne X_X
                    score = 2;
                }
            }else if(board[2][0] == 0){
                
                 if(board[2][1] == joueurActuel && board[2][2] == joueurActuel){//Third ligne _XX
                    score = 2;
                }
            }
        }if(board[0][1] != joueurAdverse){
            if(board[0][1] == joueurActuel){
                if(board[1][1] == joueurActuel && board[2][1] == joueurActuel){//Tictacttoe midlle colomn
                    return score = 5;
                }else if(board[1][1] == joueurActuel && board[2][1] == 0){//Second Colomn XX_
                    score = 2;
                }else if(board[1][1] == 0 && board[2][1] == joueurActuel){//Second colomn variant X_X
                    score = 2;
                }
            }else if(board[0][1] == 0){
                if(board[1][1] == joueurActuel && board[2][1] == joueurActuel){//Second colomn variant _XX
                    score = 2;
                }
            }
        }if(board[0][2] != joueurAdverse){
            if(board[0][2] == joueurActuel){
                if(board[1][2] == joueurActuel && board[2][2] == joueurActuel){//Tictactoe third colomn
                    return score = 5;
                }else if(board[1][1] == joueurActuel && board[2][0] == joueurActuel){//Tictactoe Diagonal top right corner
                    return score = 5;
                }else if(board[1][2] == joueurActuel && board[2][2] == 0){//Third colomn XX_
                    score = 2;
                }else if(board[1][2] == 0 && board[2][2] == joueurActuel){//Third Colomn variant X_X
                    score = 2;
                }
            }else if(board[0][2] == 0){
                if(board[1][2] == joueurActuel && board[2][2] == joueurActuel){//third colomn variant XX_
                    score = 2;
                }
            }//Cette methode regard le board selon le point de vue du joueur actuel, il y a donc des angles morts.
            

        }
        if(limiter <2){
            int scoreAdversaire = scoreCalculator(board, joueurAdverse, 2);
            if(scoreAdversaire > score){
                score = -1* scoreAdversaire;
            }
        }
       

        
        
    return score;
    }

    //scoreCalculator version 2!
    public int scoreCalculatorRemake(int[][] board, int joueurActuel, int limiter){
        
        int joueurAdverse = JeuUtils.obtenirIdSymboleAdverse(joueurActuel);

        int score = lineairCalculator(board,joueurActuel,joueurAdverse);

        return score;

    }
    public int lineairCalculator(int[][] board, int joueurActuel, int joueurAdverse){
        int counter;
        int adversityCounter;
        //horizontal
        for(int i = 0; i < 3; i++){
            counter =0;
            adversityCounter = 0;
            for(int j = 0; j < 3; j++){
                if(board[i][j] == joueurActuel ){
                    counter++;
                }else if(board[i][j] == joueurAdverse){
                    adversityCounter++;
                }
                if(counter == 2 && adversityCounter == 0){
                    return 2;
                }else if(counter == 3){
                    return 5;
                }else if(counter == 2 && adversityCounter == 1){
                    //On ne veut pas ajouter a une ligne qui contient déjà un adversaire et au moin un de notre. (gaspillage de tour)
                    return -1;
                }
            }
        }
        //Verticale
        for(int i = 0; i < 3; i++){
            counter =0;
            adversityCounter = 0;
            for(int j = 0; j < 3; j++){
                if(board[j][i] == joueurActuel ){
                    counter++;
                }else if(board[j][i] == joueurAdverse){
                    adversityCounter++;
                }
                if(counter == 2 && adversityCounter == 0){
                    return 2;
                }else if(counter == 3){
                    return 5;
                }else if(counter == 2 && adversityCounter == 1){
                    return -1;
                }
            }
        }

        //To DO Diagonal........









        
        return 0;
        
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
        //int rand = (int)(Math.random() * 10);
         //int index = rand % rootNode.children.size(); // rand % 9
         return "no value";
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


