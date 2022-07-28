import java.lang.reflect.Array;
import java.util.HashMap;

public class JeuUtils {
  
  static final String COLUMN_IDENTIFIERS = "ABCDEFGHI";
  // Les 3 cadres de bas sur le plateau de jeu
  static final String[] cadreBasGauche = { "A1", "A2", "A3", "B1", "B2", "B3", "C1", "C2", "C3" };
  static final String[] cadreBasMilieu = { "D1", "D2", "D3", "E1", "E2", "E3", "F1", "F2", "F3" };
  static final String[] cadreBasDroite = { "G1", "G2", "G3", "H1", "H2", "H3", "I1", "I2", "I3" };

  // Les 3 cadres de centre sur le plateau de jeu
  static final String[] cadreGauche = { "A4", "A5", "A6", "B4", "B5", "B6", "C4", "C5", "C6" };
  static final String[] cadreMilieu = { "D4", "D5", "D6", "E4", "E5", "E6", "F4", "F5", "F6" };
  static final String[] cadreDroite = { "G4", "G5", "G6", "H4", "H5", "H6", "I4", "I5", "I6" };

  // Les 3 cadres de haut sur le plateau de jeu
  static final String[] cadreHautGauche = { "A7", "A8", "A9", "B7", "B8", "B9", "C7", "C8", "C9" };
  static final String[] cadreHautMilieu = { "D7", "D8", "D9", "E7", "E8", "E9", "F7", "F8", "F9" };
  static final String[] cadreHautDroite = { "G7", "G8", "G9", "H7", "H8", "H9", "I7", "I8", "I9" };

  static final String[][][] CASES_GRILLE_JEU = {
      { cadreBasGauche, cadreGauche, cadreHautGauche }, // colonne 1
      { cadreBasMilieu, cadreMilieu, cadreHautMilieu }, // colonne 2
      { cadreBasDroite, cadreDroite, cadreHautDroite } // colonne 3
  };

  public static int obtenirIdSymboleJoueur(String symboleJoueur) {
    int idSymbole = -1;
    switch (symboleJoueur) {
      case "O":
        idSymbole = 2;
        break;
      case "X":
        idSymbole = 4;
        break;
      default:
        break;
    }

    return idSymbole;
  }

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

  public static void detailsNode(Node n){
    int alphaBeta = 0;  
    String type = "  ";
    int[][] rootBoard = n.array2DBoard();

    System.out.println("Current Tree:   Parent node" + "\n");
    printBorad(n.array2DBoard());
    printDetails(n);
    System.out.println("\n" + "\n" + "\n" + "Children of parent: ");
    

    for(int i = 0; i <n.children.size(); i++ ){
      printBorad(n.children.get(i).array2DBoard());
      printDetails(n.children.get(i));
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
