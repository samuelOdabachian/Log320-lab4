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
  private static final char[] CODE_COL = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' };
  // private static final ArrayList<Character> CODE_COL_2 = new ArrayList<Character>{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' };

  public TicTacToeAI() {
    this._grilleJeu = initTab();
  }

  public HashMap initTab() {

    HashMap<String, Integer> listeCases = new HashMap<String, Integer>();

    for (int i = 1; i <= 9; i++) {
      String codeCase = Character.toString(CODE_COL[i - 1]);
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

  public String jouer(String dernierCoupAdversaire) {
    this._dernierCoupAdversaire = dernierCoupAdversaire;
    System.out.println("Notre AI a joué. Le coup de l'adversaire était " + this._dernierCoupAdversaire);
    int[] caseAction = determinerCaseAction();
    determinerIntervalCaseAction(caseAction);
    return "A1";
  }

  //Envoyer tableau de taille 2
  //Envoyer  tableau string [[G,H,I],[4,5,6]]
  public int[] determinerCaseAction() {

    char col = this._dernierCoupAdversaire.charAt(0);
    int colIdx = 0;
    char row = this._dernierCoupAdversaire.charAt(1);
    int rowIdx = Character.getNumericValue(row) % 3;

    System.out.println("ProchainCoup, "+ col+", "+row);

    for (int i = 0; i < CODE_COL.length; i++) {
      if (col == CODE_COL[i]) {
        colIdx = (i+1) % 3;
      }
    }

    colIdx = colIdx==0 ? 3 : colIdx;
    rowIdx = rowIdx==0 ? 3 : rowIdx;

    System.out.println("Prochain coup sera dans case générale: "+ colIdx + " :: "+ rowIdx);
    
    return new int[]{colIdx, rowIdx};
  }
 

  public String[][] determinerIntervalCaseAction(int[] caseAction) {

    int colFin = caseAction[0]*3;
    int rowFin = caseAction[1]*3;

    ArrayList<String> validCases = new ArrayList<String>();

    ArrayList<String> ourColumns = new ArrayList<String>();
    ArrayList<String> ourRows = new ArrayList<String>();

    for (int i = 3; 0 < i; i--) {
      
      String lettre = Character.toString( CODE_COL[(colFin-(i))] );
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
}
