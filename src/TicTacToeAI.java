import java.lang.reflect.Constructor;

public class TicTacToeAI {

  private String _dernierCoupAdversaire;
  public TicTacToeAI() {

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
