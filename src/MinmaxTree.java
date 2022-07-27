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
                
            score = scoreCalculatorRemake(n.array2DBoard(),n.symboleDuJoueurActuel);
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

    //scoreCalculator version 2!
    public int scoreCalculatorRemake(int[][] board, int joueurActuel){
        
        int joueurAdverse = JeuUtils.obtenirIdSymboleAdverse(joueurActuel);

        int score = lineairCalculator(board,joueurActuel,joueurAdverse);
        int sccoreSelonAdversaire = lineairCalculator(board, joueurAdverse, joueurActuel);
        
        if(score <= sccoreSelonAdversaire){
            score = -1* sccoreSelonAdversaire;
        }
        return score;

    }

    //verifie lineairement les ligne horizontal, vertical et diagonal pour trouver un tictactoe ou un twin, ainsi que des gaspillage de tours.
    private int lineairCalculator(int[][] board, int joueurActuel, int joueurAdverse ){
        int counter;
        int adversityCounter;
        int score = 0;
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

                score = compareScore(counter, adversityCounter, score);
                if (score == 5){
                    return score;
                }
            }
        }


        //Verticale
        for(int i = 0; i < 3; i++){
            counter = 0;
            adversityCounter = 0;
            for(int j = 0; j < 3; j++){
                if(board[j][i] == joueurActuel ){
                    counter++;
                }else if(board[j][i] == joueurAdverse){
                    adversityCounter++;
                }
                score = compareScore(counter, adversityCounter,score);
                if (score == 5){
                    return score;
                }
            }
        }

        //Diagonal top right corner
        counter = 0;
        adversityCounter = 0;
        int j=0;
        for(int i = 0; i < 3; i++){
           
            if(board[i][j] == joueurActuel ){
                counter++;
            }else if(board[i][j] == joueurAdverse){
                adversityCounter++;
            }
            j++;
        }
        score = compareScore(counter, adversityCounter, score);
        if (score == 5){
            return score;
        }


        //Diagonal top left corner
        counter = 0;
        adversityCounter = 0;
        j=2;
        for(int i = 0; i < 3; i++){
            if(board[i][j] == joueurActuel ){
                counter++;
            }else if(board[i][j] == joueurAdverse){
                adversityCounter++;
            }
            j--;
        }
            score = compareScore(counter, adversityCounter, score);
            if (score == 5){
                return score;
            }
        return score;
    }
    //Recois les counter et done une score approprié.
    private int compareScore(int counter, int adversityCounter, int score){
        

        if(counter == 3){
            //Tictacto is the best score so emidiate return
            return 5;
        }else if(score == 0 && counter == 2 && adversityCounter == 0){
            //Else it is a twin
            score =2;
        }else if(score == 0 && counter == 2 && adversityCounter == 1){
            //On ne veut pas ajouter a une ligne qui contient déjà un adversaire et au moin un de notre. (gaspillage de tour)
            score = -1;
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


