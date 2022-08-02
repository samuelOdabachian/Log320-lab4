import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * 0 -> case vide
 * 2 -> O
 * 4 -> X
 */
public class TicTacToeAI {

  private HashMap<String, int[]> _positionDictionnary = new HashMap<String, int[]>(); // Grid and Case position in 4D
                                                                                      // matrix
  private Cadre[][] _grilleCadres = new Cadre[3][3];

  private String _dernierCoupAdversaire = new String();
  private int[] _prochainCadreValide = { -1, -1 };
  private HashMap<String, Integer> _grilleJeu = new HashMap<String, Integer>();
  private int _joueurId = -1; // can be 2 for O or 4 for X
  private List<int[]> _listeCadresNonDisponible = new ArrayList<int[]>();

  public TicTacToeAI() {
    this._grilleJeu = JeuUtils.initTab();

    JeuUtils.caseIndexMapper(this._positionDictionnary, this._grilleCadres); // grid and dictionnary builder
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

    System.out.println("[" + JeuUtils.obtenirSymboleJoueur(this._joueurId)
        + "] Notre AI va jouer encore. Le coup de l'adversaire était " + this._dernierCoupAdversaire);

    // int[] cadreNonDispo =
    // this.determinerCadreCoupDonne(this._derniereTentativeJoueur);
    // System.out.println("Cadre precedent (à enlever des places possibles dans
    // minmaxtree)"+ Arrays.toString(cadreNonDispo));

    // this._listeCadresNonDisponible.add( cadreNonDispo );

    // int[] tousCadres = {-1, -1};
    // String meilleureDecision = obtenirProchainCoupValide(tousCadres);

    // System.out.println("<- AI a decide de placer un coup a la case "+
    // meilleureDecision);
    // System.out.println("\n");

    return "A1";
  }

  /**
   * Code cmd == 1
   * Jouer premier coup de la partie.
   * 
   * @param joueurId
   * @return
   */
  public String jouer(int joueurId) {

    System.out.println("[" + JeuUtils.obtenirSymboleJoueur(joueurId) + "] Notre AI va jouer. On fait le premier coup");

    int[] coup = { 1, 1 };
    String meilleureDecision = obtenirProchainCoupValide(coup);
    // String meilleureDecision =
    // obtenirProchainCoupValide(this._prochainCadreValide);

    System.out.println("AI a decide de placer un coup a la case " + meilleureDecision);
    return meilleureDecision;
  }

  /**
   * Code cmd == 3
   * Jouer un coup.
   * 
   * @param joueurId
   * @param dernierCoupAdversaire
   * @return
   */
  public String jouer(int joueurId, String dernierCoupAdversaire) {

    this._dernierCoupAdversaire = dernierCoupAdversaire.trim();
    this._joueurId = joueurId;

    int adversaireId = joueurId == 2 ? 4 : 2;

    System.out.println("[" + JeuUtils.obtenirSymboleJoueur(joueurId) + "] Notre AI va jouer.");
    System.out.println(" - Le coup de l'adversaire était " + this._dernierCoupAdversaire);

    // important de mettre a jour la position de l'adversaire
    // avant de generer le prochain coup valide
    // System.out.println("mettre a jour adversaire:
    // ["+JeuUtils.obtenirSymboleJoueur(adversaireId) + "] =
    // "+this._dernierCoupAdversaire );
    mettreAJourGrilleEtatJeu(this._dernierCoupAdversaire, adversaireId);

    int[] cadre = determinerIndexProchainCadreValide(this._dernierCoupAdversaire);
    String meilleureDecision = obtenirProchainCoupValide(cadre);
    // String meilleureDecision = "B3";

    this._prochainCadreValide = cadre;

    System.out.println("AI a decide de placer un coup a la case " + meilleureDecision);
    mettreAJourGrilleEtatJeu(meilleureDecision, this._joueurId);
    return meilleureDecision;
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
  private int[] determinerIndexProchainCadreValide(String dernierCoupAdversaire) {

    char col = dernierCoupAdversaire.charAt(0);
    int colIdx = JeuUtils.COLUMN_IDENTIFIERS.indexOf(col) % 3;

    char row = dernierCoupAdversaire.charAt(1);
    int rowIdx = Character.getNumericValue(row - 1) % 3;

    System.out.println("*Notre prochain coup doit être dans le cadre suivant: ");
    System.out.println(" - colonne: " + colIdx);
    System.out.println(" - rangee: " + rowIdx);

    return new int[] { rowIdx, colIdx };
  }

  /**
   * obtenir valeur du prochain coup
   * 
   * @param cadre
   * @return
   */
  private String obtenirProchainCoupValide(int[] indexCadre) {

    System.out.println("Prochain coupe valide dans ce cadre: " + Arrays.toString(indexCadre));
    Cadre cadre = this._grilleCadres[indexCadre[0]][indexCadre[1]];

    // Cadre cadreValide = cadre.getGagnant() == 0 ? cadre :
    // MinmaxTree minmaxTree = new MinmaxTree(this._joueurId, this._grilleJeu);
    MinmaxTree minmaxTree = new MinmaxTree(this._joueurId, cadre);
    minmaxTree.createTree();
    // MinimaxTree minimaxTree = new MinimaxTree(this._joueurId, cadre);
    // minimaxTree.createTree();
    // retirerTousCadresNonDispo(minmaxTree); // on enleve tous les cadres qui ne
    // sont plus dispo
    // minmaxTree.creatTree(cadre);
    String meilleureDecision;

    if (cadre.getGagnant() == 0) { // pour un cadre
      meilleureDecision = minmaxTree.genererMeilleurDecision(cadre);
    } else { // pour les neuf cadres quand on a toute la grille
      meilleureDecision = minmaxTree.genererMeilleurDecision(this._grilleCadres);
    }

    // String meilleureDecision = "B5";

    // this._derniereTentativeJoueur = meilleureDecision;

    return meilleureDecision;
  }

  private void mettreAJourGrilleEtatJeu(String idCase, int symboleId) {
    Case caseJoue = JeuUtils.getCaseFromMapper(this._positionDictionnary, this._grilleCadres, idCase);
    Cadre cadre = JeuUtils.getCadreFromMapper(this._positionDictionnary, this._grilleCadres, idCase);
    boolean estCoupGagnant = JeuUtils.estCoupGagnant(cadre, caseJoue.getIndexDansCadre(), symboleId);
    caseJoue.setSymbole(symboleId);
    if (estCoupGagnant)
      cadre.setGagnant(symboleId);
    cadre.printBoard();
  }

  private HashMap getGrille() {
    return this._grilleJeu;
  }

}
