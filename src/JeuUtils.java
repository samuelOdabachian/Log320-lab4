import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;

public class JeuUtils {

        
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_BLACK = "\u001B[30m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String ANSI_YELLOW = "\u001B[33m";
  public static final String ANSI_BLUE = "\u001B[34m";
  public static final String ANSI_PURPLE = "\u001B[35m";
  public static final String ANSI_CYAN = "\u001B[36m";
  public static final String ANSI_WHITE = "\u001B[37m";
  
  static final String COLUMN_IDENTIFIERS = "ABCDEFGHI";

  public static String obtenirSymboleJoueur(int joueurId) {
    String signe = "ERREUR";

    switch (joueurId) {
      case 2:
        signe = "O";
        break;
      case 4:
        signe = "X";
        break;
      default:
        break;
    }

    return signe;
  }

  public static int obtenirIdSymboleAdverse(int joueurId) {
    return (joueurId == 2) ? 4 : 2;
  }

  public static int[][] AXES = new int[][]{
    {0, 1}, //ligne
    {1, 0}, //colonne
    {-1, 1}, //top down
    {1, 1}, //down top
  };

  /**
   * NON UTILISÉ
   * Initialise un dictionnaire de cases dont
   * les clés sont les codes des cases et les valeurs sont le symbole du joueur qui y a
   * fait son coup ou 0.
   * @return dictionnaire de cases
   */
  public static HashMap<String, Integer> initTab(){
    HashMap<String, Integer> listeCases = new HashMap<String, Integer>();

    for (int i = 1; i <= 9; i++) {
      String codeCase = Character.toString( JeuUtils.COLUMN_IDENTIFIERS.charAt(i-1) );
      for (int j = 1; j <= 9; j++) {
        listeCases.put(codeCase + j, 0);
      }
    }
    return listeCases;
  }

  /**
   * Méthode qui prend en paramètre un dictionnaire et un tableau 4D, puis les remplis.
   * Le tableau 4D est rempli de Cadres contenant des références à des objets Case
   * Pour chaque cases créées, une entrée du dictionnaire est initialisée avec comme clé le
   * code décrivant la position de la case et comme valeur, un tableau à 4 chiffres décrivant
   * l'index de la case dans le tableau à 4 dimensions.
   * 
   * @param listeIndexCadresEtCases le dictionnaire où insérer les index des cases
   * @param matriceCadres le tableau où insérer les références aux objets Cadres du jeu
   */
  public static void caseIndexMapper(HashMap<String, int[]> listeIndexCadresEtCases, Cadre[][] matriceCadres) {
    
    for (int k = 3 - 1; k >= 0; k--) { // ligne cadre sur 3. 2 -> 1 -> 0
      
      for (int l = 0; l < 3; l++) { // colonne cadre sur 3. 0 -> 1 -> 2

        Cadre cadre = new Cadre(new int[]{k, l});
        matriceCadres[k][l] = cadre;

        for (int i = 3 - 1; i >= 0; i--) { // ligne case sur 3.
          
          for (int j = 0; j < 3; j++) { // colonne case sur 3.
            // System.out.println("here");
            int idNombre = (k * 3) + i + 1;
            int idLettre = (l * 3) + j;
            String codeCase = Character.toString( JeuUtils.COLUMN_IDENTIFIERS.charAt(idLettre) ) + idNombre;
            int[] indexCase = {k, l, i, j};
            listeIndexCadresEtCases.put(codeCase, indexCase);
            matriceCadres[k][l].setCaseAtIndex(i, j, new Case(codeCase, new int[]{i, j}));
          }
        }
      }
    }
  }

  /**
   * À partir du code d'une case, utilise le dictionnaire d'index pour retourner 
   * le cadre contenant la case
   * @param listeIndexCadresEtCases 
   * @param matriceCadres 
   * @param idCase code d'une case auquel appartient le cadre recherchée
   * @return
   */
  public static Cadre getCadreFromMapper(HashMap<String, int[]> listeIndexCadresEtCases, Cadre[][] matriceCadres, String idCase) {
    int[] index = listeIndexCadresEtCases.get(idCase);
    return matriceCadres[index[0]][index[1]];
  }

  /**
   * À partir du code d'une case, utilise le dictionnaire d'index pour retourner 
   * le case
   * @param listeIndexCadresEtCases
   * @param matriceCadres
   * @param idCase code de la case recherchée
   * @return
   */
  public static Case getCaseFromMapper(HashMap<String, int[]> listeIndexCadresEtCases, Cadre[][] matriceCadres, String idCase) {
    int[] index = listeIndexCadresEtCases.get(idCase);
    return matriceCadres[index[0]][index[1]].getCases()[index[2]][index[3]];
  }

  /**
   * 
   * @param cadre
   * @param indexDansCadre
   * @param symbole
   * @return
   */
  public static boolean estCoupGagnant(Cadre cadre, int[] indexDansCadre, int symbole) {

    // System.out.println("\n"+ANSI_GREEN+"Vérifier si coup gagnant");
    int row = indexDansCadre[0];
    int col = indexDansCadre[1];

    int dimensionCadre = cadre.getCases().length;
    int compteurCoups = 1;
    
    for (int d = 0; d < AXES.length && compteurCoups < 3; d++) { // Itères sur les 4 axes statiques

      int[] axe = AXES[d];

      // System.out.println("axe en cours:"+Arrays.toString(axe)+" || compteurCoups: "+compteurCoups+" - info = "+row+" :: "+col);
      
      if (d == 2 && (row + col != 2)) { 
        continue; // Sauter la recherche de la diagonale du haut vers le bas \>
      }
      if (d == 3 && (row != col)) {
        break;  // Interrompre la recherche de la diagonale du bas vers le haut />
      }

      // Does all four axeections possible for 1 hit
      // Peu importe la position du coup, itere sur les 3-1 autres (dimensionCadre-1) cases
      // d'un axe. Interrompt l'exploration d'un axe si un coup de l'adversaire y est trouvé
      for (int c = 1; c < dimensionCadre && compteurCoups < 3; c++) {
        
        int nextRow = axe[0] == 0 ? row : prochainIndexCase(dimensionCadre, row, c * axe[0]);
        int nextCol = axe[1] == 0 ? col : prochainIndexCase(dimensionCadre, col, c * axe[1]);

        Case cetteCase = cadre.getCases()[nextRow][nextCol];

        if (cetteCase.getSymbole() == symbole) {
          compteurCoups++;
        } else {
          compteurCoups = 1;
          break;
        }
      }
    }
    return compteurCoups == 3;
  }

  /**
   * @param dimension nombre de cases sur un axe
   * @param indexActuel index du coup joué
   * @param iteration l'iteration actuelle multipliée par la direction 
   * @return l'index de la prochaine case à vérifier.
   */
  public static int prochainIndexCase(int dimension, int indexActuel, int iteration) {
    int nextIndex = indexActuel + iteration;
    if (nextIndex >= dimension)
      return nextIndex - dimension;
    else if (nextIndex < 0)
      return dimension + nextIndex;
    else
      return nextIndex;
  }

  /**
   * Transforme un dictionnaire des cases en tableau 2D
   * @param etatCadre
   * @return
   */
  public static int[][] mapTo2DArray(HashMap <String, Integer> etatCadre){
    int[][] board = new int[3][3];
    int caseNumber;
    Integer value;
    String key;

    for (HashMap.Entry<String, Integer> entry : etatCadre.entrySet()) {
        key = entry.getKey();
        value = entry.getValue();
        caseNumber = Integer.parseInt(String.valueOf(key.charAt(1)));  


        if(key.charAt(0) == 'A' || key.charAt(0) == 'D' ||key.charAt(0) == 'G'){
          // le '%' est pour inclure les trois possibilités 1,4 ou 7
          if(caseNumber % 3 == 1 ){
            // board[row][colomn]
            board[2][0]=value;
            //2,4 ou 8
          }else if(caseNumber % 3 == 2 ){
            board[1][0]=value;
            // 3,6 ou 9
          }else if(caseNumber % 3 == 0 ){
            board[0][0]=value;
          }
        }else if(key.charAt(0)=='B' || key.charAt(0)=='E' ||key.charAt(0)=='H'){
          if(caseNumber % 3 == 1 ){
            board[2][1]=value;
          }else if(caseNumber % 3 == 2 ){
            board[1][1]=value;
          }else if(caseNumber % 3 == 0 ){
            board[0][1]=value;
          }

        }else if(key.charAt(0)=='C' || key.charAt(0)=='F' ||key.charAt(0)=='I'){
          if(caseNumber % 3 == 1 ){
            board[2][2]=value;
          }else if(caseNumber % 3 == 2 ){
            board[1][2]=value;
          }else if(caseNumber % 3 == 0 ){
            board[0][2]=value;
          }
        }
          

        
    }
    return board;
  }
  

  //--- Methodes imprimantes ---//

  public static void detailsNode(Node n){
    System.out.println("Current Tree:   Parent node" + "\n");
    printBorad(n.array2DBoard());
    printDetails(n);
    System.out.println("\n" + "\n" + "\n" + "Children of parent: ");
    

    for(int i = 0; i <n.children.size(); i++ ){
      printBorad(n.children.get(i).array2DBoard());
      printDetails(n.children.get(i));
      if(n.children.get(i).pointage == -2){
        System.out.println("This node has children : " + n.children.get(i).children.size());
        detailsNode(n.children.get(i));
      }
    }
        
  }

  public static void printBorad(int[][] board){

    String printBoard = "";
    for(int i = 0; i < 3; i++){
        printBoard += "\n";
        for(int j =0; j < 3; j++){
            printBoard += board[i][j] + " ";
        }
    }
    
    System.out.println(printBoard); 
    

  }

  public static void printDetails(Node n){
    int alphaBeta =0;
    String type = "";
      if(n.typeNode.equals("Max")){
        alphaBeta = n.alpha;
        type = "   Alpha: ";
      }else if(n.typeNode.equals("Min")){
        alphaBeta = n.beta;
        type = "   Beta: ";
      }
      System.out.println("  -Type is: " + n.typeNode + "   Decision: " + n.decision + "   score = " + n.pointage + "    joueurActuel: " +n.symboleDuJoueurActuel + type + alphaBeta );
  }
}
