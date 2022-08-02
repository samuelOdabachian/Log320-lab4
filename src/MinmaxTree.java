import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Cette arbre est responsable de creer les noeds recursivement en utilisant le
 * concept de minimax et elegage alpha beta.
 * 
 * Il gère chaque noeds et il donne un score sur chaque noed dépendant des
 * possibilité de gagner pour le joueur.
 */

public class MinmaxTree {

    // private HashMap <String, Integer> _etatCases = new HashMap<String,
    // Integer>();
    int[] cadre;
    int symboleDuDernierJoeur;// X ou O.
    Node rootNode;

    private Cadre _etatCadre;

    public MinmaxTree(int symboleDuDernierJoeur, Cadre cadre) {
        this.symboleDuDernierJoeur = symboleDuDernierJoeur;
        this._etatCadre = cadre;
    }

    /**
     * Pour chaque positions disponible, générer un noed avec une decision d'action
     * pour remplir un position disponible.
     * Ensuite évaluer le scénario possible en lui donnant un score.
     */
    public void createTree() {
        Case[] positions = obtenirListeCoupsPossible();

        rootNode = new Node();
        rootNode.setSymbole(symboleDuDernierJoeur);
        rootNode.typeNode = "Max";
        rootNode.heuristiqueCounter = 0;

        // for loop pour interoger le hashmap pour avoir info sur le cadre en question.
        for (int i = 0; i < positions.length; i++) {
            Integer value = positions[i].getSymbole();
            this.rootNode.put(positions[i].getId(), value);
        }
        // creatChildren(rootNode);
        minimaxAlphaBeta(this.rootNode, -100, 100);
    }

    private Case[] obtenirListeCoupsPossible() {
        Case[][] grilleCases = this._etatCadre.getCases();
        Case[] positions = new Case[9];
        // int[] index = this._etatCadre.getIndex();

        for (int i = 3 - 1; i >= 0; i--) { // toujours 3^2 = 9
            for (int j = 0; j < 3; j++) {

                positions[(i * 3) + j] = grilleCases[i][j];
            }
        }
        System.out.println(JeuUtils.ANSI_BLUE + "Positions possibles: " + Arrays.toString(positions));
        // System.out.println(JeuUtils.ANSI_BLUE+"Positions possibles: " +
        // Arrays.toString(positions) + ", without loop:
        // "+Arrays.toString(JeuUtils.CASES_GRILLE_JEU[index[0]][index[1]])+JeuUtils.ANSI_RESET);

        return positions;
    }

    // Pour chaque case vide, créer un scenario possible avec le choix fait.
    // Configurer le alpha et beta aussi
    private void creatChildren(Node node) {
        // Le cadre actuel dans le noed est un Hashmap
        for (HashMap.Entry<String, Integer> entry : node.getMap().entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();

            // Si heuristique est atteind, donc areter la creation de l'arbre.
            if (value == 0 && node.heuristiqueCounter != 1) {
                // value = JeuUtils.obtenirIdSymboleAdverse(node.symboleDuJoueurActuel);
                Node n = node.creatAChild(key);
                determinTicTacToe(n);
                System.out.println(
                        "Pointage for this node is : " + Math.abs(n.pointage) + "  Decision is : " + n.decision);
                // Recursively creat children as long as the heuristic permits.
                if (Math.abs(n.pointage) != 5) {
                    creatChildren(n);
                } else {
                    System.out.println("Broke the loop for: " + n.decision);
                    break;
                }

            }

        }
    }

    /**
     * Concepte vue dans le cours LOG 320. Il s'agis de creer l'arbre recursivement
     * en l'évaluant et utilisant l'élégage alpha et beta
     * pour couper des branche qui ne von pas être utilisé.
     * 
     * @param n le noed quiva être évaluer et des enfants vont s'ajouter s'il y a
     *          lieu
     * @param a la valeur alpha du noeud parent
     * @param b la valeur beta du noeud parent
     * @return le pointage que le noeud a reçu
     */
    private int minimaxAlphaBeta(Node n, int a, int b) {
        Node nChild;
        String typeJoueur = n.typeNode;
        int score = 0;
        int bTemporaire = 0;
        int aTemporaire = 0;

        // If the node has no score already then do scorecalculator.
        if (n.isLeaf == true || n.heuristiqueCounter == 2) {
            // check the node`s board and wright in it a score and return the score
            if (Math.abs(n.pointage) == 5) {

                return n.pointage;
            } else {
                score = scoreCalculator(n.array2DBoard(), n.symboleDuJoueurActuel, n.typeNode);
                n.pointage = score;

                return score;
            }

        }

        if (typeJoueur.equals("Max")) {

            aTemporaire = -100;
            // For every possible option in board.
            for (HashMap.Entry<String, Integer> entry : n.getMap().entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();
                if (value == 0 && n.heuristiqueCounter <= 2) {
                    nChild = n.creatAChild(key);
                    determinTicTacToe(nChild);
                    score = minimaxAlphaBeta(nChild, Math.max(a, aTemporaire), b);
                    aTemporaire = Math.max(aTemporaire, score);
                    if (aTemporaire >= b) {
                        n.alpha = aTemporaire;
                        n.pointage = aTemporaire;

                        return aTemporaire;
                    }
                }
            }
            n.pointage = aTemporaire;
            n.alpha = aTemporaire;
            return aTemporaire;

            // Même concept que le cas du "Max".
        } else if (typeJoueur.equals("Min")) {

            bTemporaire = 100;
            for (HashMap.Entry<String, Integer> entry : n.getMap().entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();
                if (value == 0 && n.heuristiqueCounter <= 2) {
                    nChild = n.creatAChild(key);
                    determinTicTacToe(nChild);
                    score = minimaxAlphaBeta(nChild, a, Math.min(b, bTemporaire));
                    bTemporaire = Math.min(bTemporaire, score);
                    if (bTemporaire <= a) {
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

    /**
     * Calcule le pointage d'un cadre selon sont état.
     * 
     * @param board        le cadre a évaluer
     * @param joueurActuel le joueur qui vient de jouer
     * @param type         le type de joueur "Min" ou "Max"
     * @return le pointage calculé
     */
    private int scoreCalculator(int[][] board, int joueurActuel, String type) {

        int joueurAdverse = JeuUtils.obtenirIdSymboleAdverse(joueurActuel);

        int score = lineairCalculator(board, joueurActuel, joueurAdverse);
        int sccoreSelonAdversaire = lineairCalculator(board, joueurAdverse, joueurActuel);

        if (score <= sccoreSelonAdversaire && type.equals("Min")) {
            score = -1 * sccoreSelonAdversaire;
        } else if (score <= sccoreSelonAdversaire && type.equals("Max")) {
            score = sccoreSelonAdversaire;
        }
        return score;
    }

    /**
     * Compte le nombre de case occupé ligne par ligne dans un cadre.
     * 
     * @param board        le cadre a évaluer
     * @param joueurActuel le joueur qui vient de jouer
     * @param type         le type de joueur "Min" ou "Max"
     * @return le pointage calculé
     */
    private int lineairCalculator(int[][] board, int joueurActuel, int joueurAdverse) {
        int counter;
        int adversityCounter;
        int score = 0;
        int tempScore = 0;

        // Verification horizontal
        for (int i = 0; i < 3; i++) {
            counter = 0;
            adversityCounter = 0;
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == joueurActuel) {
                    counter++;
                } else if (board[i][j] == joueurAdverse) {
                    adversityCounter++;
                }
                tempScore = compareScore(counter, adversityCounter, score);
                if (tempScore != 0) {
                    score = tempScore;
                }

            }
        }

        // Verification Verticale
        for (int i = 0; i < 3; i++) {
            counter = 0;
            adversityCounter = 0;
            for (int j = 0; j < 3; j++) {
                if (board[j][i] == joueurActuel) {
                    counter++;
                } else if (board[j][i] == joueurAdverse) {
                    adversityCounter++;
                }
                tempScore = compareScore(counter, adversityCounter, score);
                if (tempScore != 0) {
                    score = tempScore;
                }

            }
        }

        // Verification Diagonal top right corner
        counter = 0;
        adversityCounter = 0;
        int j = 0;
        for (int i = 0; i < 3; i++) {

            if (board[i][j] == joueurActuel) {
                counter++;
            } else if (board[i][j] == joueurAdverse) {
                adversityCounter++;
            }
            j++;
        }
        tempScore = compareScore(counter, adversityCounter, score);
        if (tempScore != 0) {
            score = tempScore;
        }

        // Verification Diagonal top left corner
        counter = 0;
        adversityCounter = 0;
        j = 2;
        for (int i = 0; i < 3; i++) {
            if (board[i][j] == joueurActuel) {
                counter++;
            } else if (board[i][j] == joueurAdverse) {
                adversityCounter++;
            }
            j--;
        }
        tempScore = compareScore(counter, adversityCounter, score);
        if (tempScore != 0) {
            score = tempScore;
        }
        return score;
    }

    /**
     * Selon la valeur reçu du nombre de joueur dans une ligne, un score est
     * associé.
     * 
     * @param counter          nombre de symbole dans une ligne
     * @param adversityCounter nombre de symbole adverse dans une ligne
     * @param score            le score calculé courant s'il y a lieu.
     * @return un score
     */
    private int compareScore(int counter, int adversityCounter, int score) {

        if (score == 0 && counter == 2 && adversityCounter == 0) {
            score = 1;
        } else if (counter == 2 && adversityCounter == 1) {
            // On ne veut pas ajouter a une ligne qui contient déjà un adversaire et au moin
            // un de notre. (gaspillage de tour)
            score = -1;
        }
        return score;
    }

    /**
     * Vérifie les ligne seulement pour trouver un tictactoe. Modifiec l'attribut
     * "pointage" dans le noeud.
     * 
     * @param n le noeud a vérifier pour un tictactoe
     */
    public void determinTicTacToe(Node n) {
        int[][] board = n.array2DBoard();
        int horizontal = 0;
        int vertical = 0;
        int diagonal1 = 0;
        int diagonal2 = 0;

        // Les verticales et horizontales.
        for (int i = 0; i < 3; i++) {
            horizontal = 0;
            vertical = 0;
            for (int j = 0; j < 3; j++) {

                if (board[i][j] == n.symboleDuJoueurActuel) {
                    horizontal += 1;
                    // System.out.println("board value = " +board[i][j] + " Horizontal value: " +
                    // horizontal);
                }
                if (board[j][i] == n.symboleDuJoueurActuel) {
                    vertical += 1;
                    // System.out.println("board value = " +board[j][i] + " Vertical value: " +
                    // vertical);
                }
                if (horizontal == 3 || vertical == 3) {
                    n.isLeaf = true;
                    n.pointage = 5;
                    // JeuUtils.detailsNode(n);
                    // System.out.println("Hori =" + horizontal + " Verti =" + vertical);
                    // Un tictactoe sur un max board est focement un defaite car le joueur qui a
                    // fait l'action était l'adversaire(provenant du node beta parent)
                    if (n.typeNode.equals("Max")) {
                        n.pointage *= -1;
                    }
                    break;
                }
            }
        }
        // Les diagonales
        int J = 0;
        int k = 2;
        for (int I = 0; I < 3; I++) {
            if (board[I][J] == n.symboleDuJoueurActuel) {

                diagonal1 += 1;
            }
            if (board[I][k] == n.symboleDuJoueurActuel) {
                diagonal2 += 1;
            }
            k--;
            J++;
        }
        if (diagonal1 == 3 || diagonal2 == 3) {
            // if it is a min node make the score -5.
            n.isLeaf = true;
            n.pointage = 5;
            if (n.typeNode.equals("Max")) {
                n.pointage *= -1;
            }
        }

    }

    /**
     * 
     * 
     * @param cadre
     * @return
     */
    public String genererMeilleurDecision(Cadre cadre) {
        int highestScore = this.rootNode.alpha;

        Node bestChild = this.rootNode.children.get(0);
        for (Node child : this.rootNode.children) {
            if (child.alpha >= this.rootNode.alpha) {
                bestChild = child;
            }
        }

        System.out.println("BEST DECISION: " + bestChild.decision);
        // découper décision
        int caseLettre = ((int) bestChild.decision.charAt(0) - 65) % 3;
        int caseNum = (Character.getNumericValue(bestChild.decision.charAt(1)) - 1) % 3;

        System.out.println("BEST DECISION: " + caseLettre + caseNum);

        Case caseChoisie = cadre.getCases()[caseNum][caseLettre];

        // if (_etatCadre[])

        System.out.println(JeuUtils.ANSI_PURPLE + "highest score=" + highestScore + JeuUtils.ANSI_RESET);
        return caseChoisie.getId();
    }

    // TODO: transformer grille de cadres en grille de cases pour n'avoir
    // qu'une seule fonction genererMeilleurDecision
    public String genererMeilleurDecision(Cadre[][] cadres) {
        // Random number in the interval of the root node children arraylist.
        int rand1 = (int) (Math.random() * 10) % 3;
        int rand2 = (int) (Math.random() * 10) % 3;

        Cadre cadreChoisi = cadres[rand1][rand2];

        if (cadreChoisi.getGagnant() != 0) {
            return genererMeilleurDecision(cadres);
        }
        return genererMeilleurDecision(cadreChoisi);
    }

}
