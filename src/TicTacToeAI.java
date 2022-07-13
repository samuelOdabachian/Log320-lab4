import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.rmi.CORBA.Util;

/**
 * 0 -> case vide
 * 2 -> O
 * 4 -> X
 */
public class TicTacToeAI {

  private String _dernierCoupAdversaire = new String();
  private String _derniereTentativeJoueur = new String();
  private int[] _prochainCadreValide = {-1, -1};
  private HashMap<String, Integer> _grilleJeu= new HashMap<String, Integer>();
  private int _joueurId = -1; //can be 2 for O or 4 for X
  private List<int[]> _listeCadresNonDisponible = new ArrayList<int[]>();

  public TicTacToeAI() {
    this._grilleJeu = this.initTab();
  }

  public HashMap<String, Integer> initTab() {

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
   * Code cmd == 4
   * Rejouer sur une case differente apres
   * qu'on ait tente un coup invalide.
   * 
   * Si un coup joue est invalide, c'est que
   * le cadre n'est plus disponible. Alors ce cadre est enleve des
   * prochaine possibilites
   * 
   * @return
   */
  public String rejouer() {

    System.out.println("["+JeuUtils.obtenirSymboleJoueur(this._joueurId)+"] Notre AI va jouer encore. Le coup de l'adversaire était " + this._dernierCoupAdversaire);
    
    int[] cadreNonDispo = this.determinerCadreCoupDonne(this._derniereTentativeJoueur);
    System.out.println("Cadre precedent (à enlever des places possibles dans minmaxtree)"+ Arrays.toString(cadreNonDispo));
    
    this._listeCadresNonDisponible.add( cadreNonDispo );
    
    int[] tousCadres = {-1, -1};
    String meilleureDecision = obtenirProchainCoupValide(tousCadres);

    System.out.println("<- AI a decide de placer un coup a la case "+ meilleureDecision);
    System.out.println("\n");

    return meilleureDecision;
  }

  /**
   * Code cmd == 1
   * Jouer premier coup de la partie.
   * @param joueurId
   * @return
   */
  public String jouer(int joueurId) {

    System.out.println("["+JeuUtils.obtenirSymboleJoueur(joueurId)+"] Notre AI va jouer. On fait le premier coup");
    
    String meilleureDecision = obtenirProchainCoupValide(this._prochainCadreValide);

    System.out.println("AI a decide de placer un coup a la case "+ meilleureDecision);
    System.out.println("\n");
    return meilleureDecision;
  }

  /**
   * Code cmd == 3
   * Jouer un coup.
   * @param joueurId
   * @param dernierCoupAdversaire
   * @return
   */
  public String jouer(int joueurId, String dernierCoupAdversaire) {

    this._dernierCoupAdversaire = dernierCoupAdversaire.trim();
    this._joueurId = joueurId;

    int adversaireId = joueurId == 2 ? 4 : 2;

    System.out.println("["+JeuUtils.obtenirSymboleJoueur(joueurId)+"] Notre AI va jouer.");
    System.out.println(" - Le coup de l'adversaire était " + this._dernierCoupAdversaire);

    int[] cadre = determinerProchainCadreValide(this._dernierCoupAdversaire);
    
    // important de mettre a jour la position de l'adversaire
    // avant de generer le prochain coup valide
    System.out.println("mettre a jour adversaire: ["+JeuUtils.obtenirSymboleJoueur(adversaireId) + "] = "+this._dernierCoupAdversaire );
    mettreAJourGrille(this._dernierCoupAdversaire, adversaireId);
    
    String meilleureDecision = obtenirProchainCoupValide(cadre);
    
    this._prochainCadreValide = cadre;

    System.out.println("AI a decide de placer un coup a la case "+ meilleureDecision);
    System.out.println("\n");
    return meilleureDecision;
  }

  /**
   * Mettre a jour la grille de jeu par
   * rapport aux cases qui ont ete jouees
   * @param coup
   * @param symboleId
   */
  public void mettreAJourGrille(String coup, int symboleId) {
    this._grilleJeu.replace(coup, symboleId);
  }

  /**
   * Met a jour la derniere case que le joueur a joue
   * @param symboleIdJoueur
   */
  public void mettreAJourGrilleJoueur(int symboleIdJoueur) {
    // System.out.println("le joueur a pu joué. "+ this._derniereTentativeJoueur + " vs "+this._dernierCoupAdversaire);
    mettreAJourGrille(this._derniereTentativeJoueur, symboleIdJoueur);
  }

  /**
   * obtenir valeur du prochain coup
   * @param cadre
   * @return
   */
  private String obtenirProchainCoupValide(int[] cadre) {

    MinmaxTree minmaxTree = new MinmaxTree(this._joueurId, this._grilleJeu);
    retirerTousCadresNonDispo(minmaxTree); // on enleve tous les cadres qui ne sont plus dispo
    minmaxTree.creatTree(cadre, JeuUtils.obtenirIdSymboleAdverse(this._joueurId));

    String meilleureDecision = minmaxTree.genererMeilleurDecision(cadre);

    this._derniereTentativeJoueur = meilleureDecision;

    return meilleureDecision;
  }

  /**
   * Supprime tous les cadres qui ne sont plus disponible
   * du nouvel arbre minmax
   * @param tree un arbre minmax
   */
  private void retirerTousCadresNonDispo(MinmaxTree tree) {
    for (int[] cadre : this._listeCadresNonDisponible) {
      tree.retirerCadre(cadre);
    }
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
    int  colIdx = this.ajusterIndexAGrilleJeu( JeuUtils.COLUMN_IDENTIFIERS.indexOf(col)+1 ); 

    char row = dernierCoupAdversaire.charAt(1);
    int rowIdx = this.ajusterIndexAGrilleJeu( Character.getNumericValue(row) % 3 );
    
    System.out.println("Notre prochain coup doit être dans le cadre suivant: ");
    System.out.println(" - colonne: "+ colIdx);
    System.out.println(" - rangee: "+ rowIdx);
    
    return new int[]{colIdx, rowIdx};
  }

  /**
   * Determine le cadre dans lequel se trouve un coup
   * @param coup un coup
   * @return cadre
   */
  public int[] determinerCadreCoupDonne(String coup) {

    char col = coup.charAt(0);
    int colIdx = (int) Math.ceil( (JeuUtils.COLUMN_IDENTIFIERS.indexOf(col)+1)/3.0d );

    char row = coup.charAt(1);
    int rowIdx = (int)Math.ceil( Character.getNumericValue(row)/3.0d );

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
 
  public HashMap getGrille(){
    return this._grilleJeu;
  }

}
