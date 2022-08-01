import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

//Tree a interoger pour savoir les pourcentages de ganger pour chaque coups possible. Il faut recréer le tree
//après chaque coup. Donc le tree n'est pas statique.
//Il faut reécrire dans le tree les positions et options possible dans le case 3x3 envisager. 
//Le tree envoie le meilleur choix possible après des calcules internes.

//Idées: le beta decide s'il faut arreter de construire l'arbre. le scoreCalculator ne calcule plus les tictactoes car on le fait a chaque fois
//pour determiner s'il est un leaf. Sinon, un node est automatiquement un leaf si la creation d'enfant arrete.
//The children creator in a node will determine using heuristique and tictactoe detecter if the node is a leaf or not every time it is created.
public class MinmaxTree {

    // private HashMap <String, Integer> _etatCases = new HashMap<String, Integer>();
    int[] cadre;
    int symboleDuDernierJoeur;//X ou O.
    Node rootNode;

    private Cadre _etatCadre;
    

    public MinmaxTree(int symboleDuDernierJoeur, Cadre cadre) {
        this.symboleDuDernierJoeur = symboleDuDernierJoeur;
        this._etatCadre = cadre;
    }

    //Créer le tree à partire de l'état du cadre courante. 
    //Dépendant de la liste de position à checker donné, la methode va interoger le hashMap pour
    //savoir l'état et construire l'arbre.
    //Il va construir l'état actuel avec des enfant designants les états future dependant des choix fait.

    public void createTree() {
        Case[] positions = obtenirListeCoupsPossible();

        rootNode = new Node();
        rootNode.setSymbole(symboleDuDernierJoeur);//Doit changer pour le joueur adverse du dernier joueur
        rootNode.typeNode = "Max";
        rootNode.heuristiqueCounter = 0;
        
        //for loop pour interoger le hashmap pour avoir info sur le cadre en question.
        for(int i = 0; i < positions.length; i++ ){
            Integer value = positions[i].getSymbole();
            this.rootNode.put(positions[i].getId(), value);
        }
        //creatChildren(rootNode);
        minimax(this.rootNode,-100, 100 );
    }

    private Case[] obtenirListeCoupsPossible() {
        Case[][] grilleCases = this._etatCadre.getCases();
        Case[] positions = new Case[9];
        // int[] index = this._etatCadre.getIndex();

        for (int i = 3-1; i >= 0; i--) { // toujours 3^2 = 9
            for (int j = 0; j < 3; j++) {
                
                positions[(i*3)+j] = grilleCases[i][j];
            }
        }
        System.out.println(JeuUtils.ANSI_BLUE+"Positions possibles: " + Arrays.toString(positions));
        // System.out.println(JeuUtils.ANSI_BLUE+"Positions possibles: " + Arrays.toString(positions) + ", without loop: "+Arrays.toString(JeuUtils.CASES_GRILLE_JEU[index[0]][index[1]])+JeuUtils.ANSI_RESET);

        return positions;
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
                determinTicTacToe(n);
                System.out.println("Pointage for this node is : "+ Math.abs(n.pointage )+ "  Decision is : " + n.decision);
                //Recursively creat children as long as the heuristic permits.
                if(Math.abs(n.pointage) != 5){
                    creatChildren(n);
                }else{
                    System.out.println("Broke the loop for: "+ n.decision);
                    break;
                }
                
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
        int bTemporaire = 0;
        int aTemporaire = 0;
    
        //If the node has no score already then do scorecalculator.
        // Maybe have the heuristique count turns and at a certain point, if it is possible to have leafs than start to check that level for tictactoes
        if(n.isLeaf == true || n.heuristiqueCounter == 2){
            //check the node`s board and wright in it a score and return the score
            if(Math.abs(n.pointage) == 5){
                //System.out.println("Le node a un core de 5 apparament :  "+ n.pointage);
                return n.pointage;
            }else {
                score = scoreCalculator(n.array2DBoard(), n.symboleDuJoueurActuel);
                n.pointage = score;
            
                return score;
            }
            
        }
        
        if(typeJoueur.equals("Max")){
               
            aTemporaire = -100;
            //For every possible option in board.
            for (HashMap.Entry<String, Integer> entry : n.getMap().entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();
            //for(int i = 0; i < n.children.size(); i ++){
                //nChild = n.children.get(i);
                if(value == 0 && n.heuristiqueCounter <= 2){
                    nChild = n.creatAChild(key);
                    determinTicTacToe(nChild);
                    score = minimax(nChild,Math.max(a,aTemporaire),b);
                    aTemporaire = Math.max(aTemporaire,score);
                    if(aTemporaire >= b){
                        n.alpha = aTemporaire;
                        n.pointage = aTemporaire;
                    
                        return aTemporaire;
                    }
                }                
            }
            n.pointage = aTemporaire;
            n.alpha = aTemporaire;
            return aTemporaire;
          
        }else if(typeJoueur.equals("Min")){
            
            bTemporaire = 100;
            for (HashMap.Entry<String, Integer> entry : n.getMap().entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();
            //for(int i = 0; i < n.children.size(); i ++){
               // nChild = n.children.get(i);
               if(value == 0 && n.heuristiqueCounter <= 2){
                    nChild = n.creatAChild(key);
                    determinTicTacToe(nChild);
                    score = minimax(nChild,a,Math.min(b,bTemporaire));
                    bTemporaire = Math.min(bTemporaire,score);
                    if(bTemporaire <= a){
                        n.beta = bTemporaire;
                        n.pointage = bTemporaire;
                    
                        return bTemporaire;
                    }
               }
            }
            n.pointage = bTemporaire;
            n.beta = bTemporaire;
            return bTemporaire;
        } 

        return score;
        
      }

    //scoreCalculator version 2!
    public int scoreCalculator(int[][] board, int joueurActuel) {
        
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
        int j = 0;
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

    //Check for tictactoes only.
    public void determinTicTacToe(Node n){
        int[][] board = n.array2DBoard();
        int horizontal = 0;
        int vertical = 0;
        int diagonal1 = 0;
        int diagonal2 = 0;
        
        //Les verticales et horizontales.
        for(int i = 0; i < 3; i++){
            horizontal = 0;
            vertical =0;
            for (int j = 0; j < 3; j++){
                
                if(board[i][j] == n.symboleDuJoueurActuel){
                    horizontal += 1;
                    //System.out.println("board value = " +board[i][j] + " Horizontal value: " + horizontal);
                }if(board[j][i] == n.symboleDuJoueurActuel){
                    vertical += 1;
                   // System.out.println("board value = " +board[j][i] + " Vertical value: " + vertical);
                }
                if(horizontal == 3 || vertical == 3){
                   //if it is a min node make the score -5.
                    n.isLeaf = true;
                    n.pointage = 5;
                    //JeuUtils.detailsNode(n);
                    //System.out.println("Hori =" + horizontal + "  Verti =" + vertical);
                    //Un tictactoe sur un max board est focement un defaite car le joueur qui a fait l'action était l'adversaire(provenant du node beta parent)
                    if(n.typeNode.equals("Max")){
                        n.pointage *= -1;
                    }
                    break;
                }
            }
        } 
        //Les diagonales  
        int J = 0; 
        int k = 2;
        for(int I = 0; I < 3; I++){
            if(board[I][J] == n.symboleDuJoueurActuel){
                

                diagonal1 += 1;
            }if(board[I][k] == n.symboleDuJoueurActuel){
                diagonal2 += 1;
            }
            k--;
            J++;
        }if(diagonal1 == 3 || diagonal2 == 3){
            //if it is a min node make the score -5.
            n.isLeaf = true;
            n.pointage = 5;
            if(n.typeNode.equals("Max")){
                n.pointage *= -1;
            }
        }

    }

    public String genererMeilleurDecision(Cadre cadre){
        //Random number in the interval of the root node children arraylist.
        int rand1 = (int)(Math.random() * 10) % 3;
        int rand2 = (int)(Math.random() * 10) % 3;
        Case caseChoisie = cadre.getCases()[rand1][rand2];

        int highestScore = this.rootNode.alpha;

        System.out.println(JeuUtils.ANSI_PURPLE+"highest score=" + highestScore + JeuUtils.ANSI_RESET);
        return caseChoisie.getId();
    }

    // TODO: transformer grille de cadres en grille de cases pour n'avoir
    // qu'une seule fonction genererMeilleurDecision
    public String genererMeilleurDecision(Cadre[][] cadres){
        //Random number in the interval of the root node children arraylist.
        int rand1 = (int)(Math.random() * 10) % 3;
        int rand2 = (int)(Math.random() * 10) % 3;
        
        Cadre cadreChoisi = cadres[rand1][rand2];

        if (cadreChoisi.getGagnant() != 0) {
            return genererMeilleurDecision(cadres);
        }
        return genererMeilleurDecision(cadreChoisi);
    }
    
}


