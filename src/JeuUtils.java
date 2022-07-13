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
}
