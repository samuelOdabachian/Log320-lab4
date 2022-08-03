import java.util.HashMap;
import java.util.Vector;

public class Minimax {

    // private HashMap <String, Integer> _etatCases = new HashMap<String,
    // Integer>();
    int[] cadre;
    int symboleDuDernierJoeur;// X ou O.
    Node rootNode;
    private Cadre[][] _grilleCadres = new Cadre[3][3];
    // private Cadre _etatCadre;
    // static int currentPlayer = 0;

    public Minimax(int symboleDuDernierJoeur, Cadre[][] grilleCadre) {
        this.symboleDuDernierJoeur = symboleDuDernierJoeur;
        this._grilleCadres = grilleCadre;
    }

    public String genererMeilleurDecision(String coup) {
        int meilleureScore = Integer.MIN_VALUE;

        String move = "";
        Cadre cadreValide = determinerIndexProchainCadreValide(coup);
        Case[] casesValide = obtenirListeCoupsPossible(cadreValide);

        for (int i = 0; i < casesValide.length; i++) {
            int score = minimax2(cadreValide, 0, false, Integer.MIN_VALUE, Integer.MAX_VALUE, casesValide[i]);
            if (score > meilleureScore) {
                meilleureScore = score;
                move = casesValide[i].getId();
            }
        }
        System.out.println("move chosen="+move);
        return move;
    }

    /**
     * Deduit le prochain cadre dans lequel le joueur peut jouer
     * a partir du dernier coup de l'adversaire.
     * 
     * @param dernierCoupAdversaire derniere case dans lequel l'adversaire a joue
     *                              (ex: F2)
     * @return un tableau de taille 2 indiquant l'index du cadre dans lequel le
     *         joueur pourra jouer.
     *         // A rentrer dans une matrice/grille de cadres
     */
    private Cadre determinerIndexProchainCadreValide(String dernierCoupAdversaire) {

        char col = dernierCoupAdversaire.charAt(0);
        int colIdx = JeuUtils.COLUMN_IDENTIFIERS.indexOf(col) % 3;

        char row = dernierCoupAdversaire.charAt(1);
        int rowIdx = Character.getNumericValue(row - 1) % 3;

        System.out.println("*Notre prochain coup doit être dans le cadre suivant: ");
        System.out.println(" - colonne: " + colIdx);
        System.out.println(" - rangee: " + rowIdx);

        // TODO: Ajouter directement choix toute grille -> polymor

        // return new int[] { rowIdx, colIdx };
        return this._grilleCadres[rowIdx][colIdx];
    }

    private Case[] obtenirListeCoupsPossible(Cadre cadre) {
        // Case[][] grilleCases = this._etatCadre.getCases();
        Case[][] grilleCases = cadre.getCases();
        Vector<Case> pos = new Vector<Case>();
        // int[] index = this._etatCadre.getIndex();

        for (int i = 3 - 1; i >= 0; i--) { // toujours 3^2 = 9
            for (int j = 0; j < 3; j++) {
                if (grilleCases[i][j].getSymbole() == 0) {
                    pos.add(grilleCases[i][j]);
                }
            }
        }

        System.out.println(JeuUtils.ANSI_BLUE + "Positions possibles: " + pos.toString());
        Case[] posArr = new Case[pos.size()];
        return pos.toArray(posArr);
    }

    private int maxDepth = 3;
    private int _score = 0;

    public int minimax2(Cadre cadre, int depth, boolean isMaximizing, int alpha, int beta, Case dernierCoup) {
        this._score = evaluerCoup(cadre, dernierCoup.getIndex(), this.symboleDuDernierJoeur);
        System.out.println(JeuUtils.ANSI_GREEN+"score="+this._score+JeuUtils.ANSI_RESET);

        // Dependemment du dernier coup du joueur adverse, on determine le bon cadre dans lequel on
        // peut jouer. Ensuite, on en ressort les cases libres
        Cadre cadreValide = determinerIndexProchainCadreValide(dernierCoup.getId());
        Case[] casesValide = obtenirListeCoupsPossible(cadreValide);

        if (depth >= maxDepth || casesValide.length == 0) {
            if (this.symboleDuDernierJoeur == 2) return this._score;
            else return this._score * -1;
        }

        if (isMaximizing) { // le AI
            for (Case caseJouable : casesValide) {
                caseJouable.setSymbole(this.symboleDuDernierJoeur);
                this._score = minimax2(cadre, depth + 1, true, alpha, beta, caseJouable);
                caseJouable.setSymbole(0);
                if (this._score > alpha) alpha = this._score;
                if (alpha >= beta) break; // cuts off beta
            }
            this.symboleDuDernierJoeur = JeuUtils.obtenirIdSymboleAdverse(this.symboleDuDernierJoeur);
            return alpha;
        }
        else { // l'adversaire
            for (Case caseJouable : casesValide) {
                // caseJouable.setSymbole(this.symboleDuDernierJoeur);
                this._score = minimax2(cadre, depth + 1, false, alpha, beta, caseJouable);
                // caseJouable.setSymbole(0);
                if (this._score < alpha) beta = this._score;
                if (alpha >= beta) break; // cuts off alpha
            }
            this.symboleDuDernierJoeur = JeuUtils.obtenirIdSymboleAdverse(this.symboleDuDernierJoeur);
            return beta;
        }
    }

    public int minimax(Cadre cadre, int depth, boolean isMaximizing, Case dernierCoup) {

        // int gagnant = evaluerGagnant();
        int scoreCoup = evaluerCoup(cadre, dernierCoup.getIndex(), this.symboleDuDernierJoeur);
            if (scoreCoup != 0) {
                if (this.symboleDuDernierJoeur == 2) return scoreCoup;
                else return scoreCoup * -1;
            }
        // if (gagnant != -1) {
        //     return 0;
        // }
        System.out.println(JeuUtils.ANSI_RED+"depth="+depth+", "+JeuUtils.ANSI_RESET);

        if (depth >= maxDepth) {
            return 0;
        }

        if (isMaximizing) {
            int meilleureScore = Integer.MAX_VALUE;

            Cadre cadreValide = determinerIndexProchainCadreValide(dernierCoup.getId());
            Case[] casesValide = obtenirListeCoupsPossible(cadreValide);

            for (int i = 0; i < casesValide.length; i++) {
                casesValide[i].setSymbole(this.symboleDuDernierJoeur);
                int score = minimax(cadreValide, depth+1, true, casesValide[i]);
                casesValide[i].setSymbole(0);
                meilleureScore = Integer.max(score, meilleureScore);
            }
            this.symboleDuDernierJoeur = JeuUtils.obtenirIdSymboleAdverse(this.symboleDuDernierJoeur);
            return meilleureScore;
        } else {
            int meilleureScore = Integer.MIN_VALUE;

            Cadre cadreValide = determinerIndexProchainCadreValide(dernierCoup.getId());
            Case[] casesValide = obtenirListeCoupsPossible(cadreValide);

            for (int i = 0; i < casesValide.length; i++) {
                casesValide[i].setSymbole(this.symboleDuDernierJoeur);
                int score = minimax(cadreValide, depth+1, true, casesValide[i]);
                casesValide[i].setSymbole(0);
                meilleureScore = Integer.min(score, meilleureScore);
            }
            this.symboleDuDernierJoeur = JeuUtils.obtenirIdSymboleAdverse(this.symboleDuDernierJoeur);
            return meilleureScore;
        }
    }


    public static int[][] DIRS = new int[][]{
        {0, 1}, //ligne
        {1, 0}, //colonne
        {-1, 1}, //top down
        {1, 1}, //down top
      };
    public static int evaluerCoup(Cadre cadre, int[] indexDansCadre, int symbole) {
        cadre.printBoard();
        // System.out.println("\n"+ANSI_GREEN+"Vérifier si coup gagnant");
        int row = indexDansCadre[0];
        int col = indexDansCadre[1];
    
        int dimensionCadre = cadre.getCases().length;
        int compteurCoups = 1;
        int adversaire = JeuUtils.obtenirIdSymboleAdverse(symbole);
        int compteurCoupsAdversaire = 0;
        
        for (int d = 0; d < DIRS.length && compteurCoups < 3; d++) { // 4 directions
            
          int[] dir = DIRS[d];
    
          // System.out.println("Dir en cours:"+Arrays.toString(dir)+" || compteurCoups: "+compteurCoups+" - info = "+row+" :: "+col);
          
          if (d == 2 && (row + col != 2)) {
            // System.out.println("- skip diago top down = "+row+" :: "+col);
            continue;
          }
          if (d == 3 && (row != col)) {
            // System.out.println("- skip diago bottom top = "+row+" :: "+col);
            break;
          }
    
          // Does all four directions possible for 1 hit
          for (int c = 1; c < dimensionCadre && compteurCoups < 3; c++) {
            
            int nextRow = dir[0] == 0 ? row : loopedJumpToNextIndex(dimensionCadre, row, c * dir[0]);
            int nextCol = dir[1] == 0 ? col : loopedJumpToNextIndex(dimensionCadre, col, c * dir[1]);
    
            Case cetteCase = cadre.getCases()[nextRow][nextCol];
            // System.out.println("- Case en cours: "+cetteCase.getId());
    
            if (cetteCase.getSymbole() == symbole) {
              compteurCoups++;
            } 
            if (cetteCase.getSymbole() == adversaire) {
              compteurCoupsAdversaire++;
              compteurCoups--;
              break;
            } 
            // else {
            //   compteurCoups = 1;
            //   break;
            // }
          }
        }
    
        // System.out.println("Coup gagnant: "+compteurCoups==3+ANSI_RESET);
        return  (compteurCoups)*100;
      }

      public static int loopedJumpToNextIndex(int dimension, int currentIndex, int iteration) {
        int nextIndex = currentIndex + iteration;
        if (nextIndex >= dimension)
          return nextIndex - dimension;
        else if (nextIndex < 0)
          return dimension + nextIndex;
        else
          return nextIndex;
      }

}
