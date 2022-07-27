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
        //minimax(rootNode,rootNode.alpha,null );
        
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
           
            
           
            
        }
    }
    
    public int minimax(Node n, int a, int b){
        /* TO DO: For each children of parent node, recursive call until you find a leaf, give the leaf a score and return the score,
         * For each child leaf a score is returned, depending if the parent is a Min or Max player keep the appropriate score fromm all their children...
         */
        Node nChild;
        String typeJoueur = n.typeNode;
        int score = 0;
        int maxScore =-100;
        int minScore = 100;
        
    

        if(n.isLeaf == true){
            //check the node`s board and wright in it a score and return the score
                
            score = scoreCalculator(n.array2DBoard(), n.symboleDuJoueurActuel);
            n.pointage = score;
            JeuUtils.detailsNode(n);
            
            return score;
        }
        //Do alpha beta and root node will return an unused beta value. First recursion dept of level 2, checks its the leaf and keeps the score 
        //for alpha if beta permits(first recursion beta is -100 so yes),
        //Second recursion, beta now has a value, so level 2 depts compares current a with beta all the time before it continues the search. 
        //In that level, if a > than b then stop the recurson....eventualy use this methode to generate the tree and stop creating branches that dont matter.
        //Root node will have a beat value big enouph to not influence the best choice...I think
        if(typeJoueur.equals("Max")){
            int b = 100;
            int a = -100;
            
            for(int i = 0; i < n.children.size(); i ++){
                nChild = n.children.get(i);
                score = minimax(nChild);
                a = Math.max(a,score);
                b = Math.min(a,b);
                if(a >= b){
                    n.alpha = a;
                    return maxScore;
                }
            }
            
          
        }else if(typeJoueur.equals("Min")){
            

            for(int i = 0; i < n.children.size(); i ++){
                nChild = n.children.get(i);
              
                score = minimax(nChild);
                minScore = Math.min(minScore,score);
            }
            n.pointage = minScore;
            JeuUtils.detailsNode(n);

            return minScore;
        } 

        
        
        return score;
        
      }

    //scoreCalculator version 2!
    public int scoreCalculator(int[][] board, int joueurActuel){
        
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
        
        
        //Verification horizontal
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
                //Return socre of 5 witch is tictactoe.
                if (score == 5){
                    return score;
                }
            }
        }


        //Verification Verticale
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

        //Verification Diagonal top right corner
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


        //Verification Diagonal top left corner
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
        int beta = 100;
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


