import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

/**
 * 0 -> case vide
 * 2 -> O
 * 4 -> X
 */
public class TicTacToeAI {

  private String _dernierCoupAdversaire;
  private HashMap<String, Integer> _grilleJeu= new HashMap<String, Integer>();
  private static final String COLUMN_IDENTIFIERS = "ABCDEFGHI";
  private int _joueurId = -1; //can be 2 for O or 4 for X

  public TicTacToeAI() {
    this._grilleJeu = this.initTab();
  }

  public HashMap<String, Integer> initTab() {

    HashMap<String, Integer> listeCases = new HashMap<String, Integer>();

    for (int i = 1; i <= 9; i++) {
      String codeCase = Character.toString( COLUMN_IDENTIFIERS.charAt(i-1) );
      for (int j = 1; j <= 9; j++) {
        listeCases.put(codeCase + j, 0);
      }
    }
  
    return listeCases;
  }

  public String jouer() {
    System.out.println("Notre AI a joué encore. Le coup de l'adversaire était " + this._dernierCoupAdversaire);
    return "A2";
  }

  public String jouer(int joueurId, String dernierCoupAdversaire) {
    
    this._dernierCoupAdversaire = dernierCoupAdversaire;
    this._joueurId = joueurId;

    int adversaireId = joueurId == 2 ? 4 : 2;

    System.out.println("Notre AI va jouer.");
    System.out.println(" - Nous somme: "+this.obtenirSigneJoueur(joueurId));
    System.out.println(" - Le coup de l'adversaire était " + this._dernierCoupAdversaire);

    int[] caseAction = determinerProchainCadreValide(dernierCoupAdversaire);
    
    this._grilleJeu.put(dernierCoupAdversaire, adversaireId);

    // MinmaxTree minmaxTree = new MinmaxTree();
    // minmaxTree.creatTree(caseAction, this._grilleJeu);
    return "A1";
  }

  private String obtenirSigneJoueur(int joueurId) {
    String signe = "";

    switch (joueurId) {
      case 2:
        signe = "O";
        break;
      case 4:
        signe = "X";
        break;
      default:
        signe = "ERREUR";
        break;
    }

    return signe;
  }

  /**
   * Deduit le prochain cadre dans lequel le joueur peut jouer
   * a partir du dernier coup de l'adversaire.
   * @param dernierCoupAdversaire derniere case dans lequel l'adversaire a joue (ex: F2)
   * @return un tableau de taille 2 indiquant dans quelle cadre le joueur pourra jouer.
   * (ex: {3,2} dans lequel 3 est l'index de la colonne de la grille et 2 l'index de la rangee)
   */
  public int[] determinerProchainCadreValide( String dernierCoupAdversaire) {

    char col = dernierCoupAdversaire.charAt(0);
    int  colIdx = this.ajusterIndexAGrilleJeu( COLUMN_IDENTIFIERS.indexOf(col)+1 ); 

    char row = dernierCoupAdversaire.charAt(1);
    int rowIdx = this.ajusterIndexAGrilleJeu( Character.getNumericValue(row) % 3 );

    System.out.println("Notre prochain coup doit être dans le cadre suivant: ");
    System.out.println(" - colonne: "+ colIdx);
    System.out.println(" - rangee: "+ rowIdx);
    
    return new int[]{colIdx, rowIdx};
  }

  /**
   * Ajuste l'index d'une rangee ou d'une colonne
   * a la taille de la grille de jeu. La taille d'une
   * grille est determinee par le nombre de cadre de tic-tac-toe
   * qu'elle possede. Ainsi, une grille ayant 9 cadres de jeu sera
   * de taille 3x3
   * @param index index d'une rangee ou d'une colonne (va presentement de 1 a 9)
   * @return l'index du cadre
   */
  private int ajusterIndexAGrilleJeu(int index) {
    int indexAjuste = index % 3;
    return (indexAjuste == 0) ? 3 : indexAjuste;
  }
 

  public String[][] determinerIntervalCaseAction(int[] caseAction) {

    int colFin = caseAction[0]*3;
    int rowFin = caseAction[1]*3;

    ArrayList<String> validCases = new ArrayList<String>();

    ArrayList<String> ourColumns = new ArrayList<String>();
    ArrayList<String> ourRows = new ArrayList<String>();

    for (int i = 3; 0 < i; i--) {
      
      String lettre = Character.toString( COLUMN_IDENTIFIERS.charAt(colFin-(i)) );
      String numero = Integer.toString(rowFin-(i-1));
      ourColumns.add(lettre);
      ourRows.add( numero );
      System.out.println("on est à "+(i)+" :: "+lettre+" :: "+numero);
    }

    for (int i = 0; i < ourColumns.size(); i++) {
      for (int j = 0; j < ourRows.size(); j++) {
        String caseCode = ourColumns.get(i) + ourRows.get(j);
        validCases.add(caseCode );
      }
    }
    System.out.println("Nos cases possibles:: "+validCases.toString());
    String[] tabLettre = ourColumns.stream().toArray(String[]::new);
    String[] tabNumero = ourRows.stream().toArray(String[]::new);
    String[][] tabCodes = {tabLettre, tabNumero};
    return tabCodes;
  }

  public HashMap getGrille(){
    return this._grilleJeu;
  }

}
