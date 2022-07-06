import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Vector;

/**
 * 0 -> case vide
 * 2 -> O
 * 4 -> X
 */
public class TicTacToeAI {

  private String _dernierCoupAdversaire;
  public TicTacToeAI() {
    initTab();
  }

  //Cette map permet de savoir pour chaque position s'il est dosponible ou s'il y a un X ou O.
  //Elle va être modifier après chaque coup.
  public HashMap initTab(){
    char[] codeCol = {'A','B', 'C', 'D',  'E', 'F', 'G', 'H', 'I'};
    HashMap<String, Integer> listeCases = new HashMap<String, Integer>();
    for (int i = 1; i <= 9; i++) {
      String codeCase = Character.toString(codeCol[i-1]);
      for (int j = 1; j <= 9; j++) {
        listeCases.put(codeCase+j, 0);
      }
    }
    System.out.println(listeCases);
    return listeCases;
  }

  public String jouer() {
    System.out.println("Notre AI a joué encore. Le coup de l'adversaire était " + this._dernierCoupAdversaire);
    return "A2";
  }

  public String jouer(String dernierCoupAdversaire) {
    this._dernierCoupAdversaire = dernierCoupAdversaire;
    System.out.println("Notre AI a joué. Le coup de l'adversaire était " + this._dernierCoupAdversaire);
    return "A1";
  }
}
