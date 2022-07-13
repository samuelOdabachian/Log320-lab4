import java.util.HashMap;

public class Utils {

    public static final String COLUMN_IDENTIFIERS = "ABCDEFGHI";
    // Les 3 cadres de bas sur le plateau de jeu
    public static final String[] cadreBasGauche = { "A1", "A2", "A3", "B1", "B2", "B3", "C1", "C2", "C3" };
    public static final String[] cadreBasMilieu = { "D1", "D2", "D3", "E1", "E2", "E3", "F1", "F2", "F3" };
    public static final String[] cadreBasDroite = { "G1", "G2", "G3", "H1", "H2", "H3", "I1", "I2", "I3" };

    // Les 3 cadres de centre sur le plateau de jeu
    public static final String[] cadreGauche = { "A4", "A5", "A6", "B4", "B5", "B6", "C4", "C5", "C6" };
    public static final String[] cadreMilieu = { "D4", "D5", "D6", "E4", "E5", "E6", "F4", "F5", "F6" };
    public static final String[] cadreDroite = { "G4", "G5", "G6", "H4", "H5", "H6", "I4", "I5", "I6" };

    // Les 3 cadres de haut sur le plateau de jeu
    public static final String[] cadreHautGauche = { "A7", "A8", "A9", "B7", "B8", "B9", "C7", "C8", "C9" };
    public static final String[] cadreHautMilieu = { "D7", "D8", "D9", "E7", "E8", "E9", "F7", "F8", "F9" };
    public static final String[] cadreHautDroite = { "G7", "G8", "G9", "H7", "H8", "H9", "I7", "I8", "I9" };

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";

    public static final String[][][] CASES_GRILLE_JEU = {
            { cadreBasGauche, cadreGauche, cadreHautGauche }, // colonne 1
            { cadreBasMilieu, cadreMilieu, cadreHautMilieu }, // colonne 2
            { cadreBasDroite, cadreDroite, cadreHautDroite } // colonne 3
    };

    public static HashMap<String, Integer> generer_grille_jeu() {

        HashMap<String, Integer> listeCases = new HashMap<String, Integer>();

        for (int i = 1; i <= 9; i++) {
            String codeColonne = Character.toString(COLUMN_IDENTIFIERS.charAt(i - 1));
            for (int j = 1; j <= 9; j++) {
                listeCases.put(codeColonne + j, 0);
            }
        }

        return listeCases;
    }

      /**
   * Determine le cadre dans lequel se trouve un coup
   * @param coup un coup
   * @return cadre
   */
  public static int[] determinerCadreCoupDonne(String coup) {

    char col = coup.charAt(0);
    int colIdx = (int) Math.ceil( (Utils.COLUMN_IDENTIFIERS.indexOf(col)+1)/3.0d );

    char row = coup.charAt(1);
    int rowIdx = (int)Math.ceil( Character.getNumericValue(row)/3.0d );

    return new int[]{colIdx, rowIdx};
  }

    public static int obtenirIdAdversaire(int joueurId) {
        return (joueurId == 2) ? 4 : 2;
    }

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

    // ---------------------- PRINTING METHODS ----------------
    public static void dessiner_grille_jeu(HashMap<String, Integer> etatGrille) {

        System.out.println("La grille de jeu");

        for (int i = 0; i <= 9; i++) {

            if (i > 0) {
                if ((10 - i) % 3 == 0)
                    printHorizontalLine(ANSI_BLUE);
                else
                    printHorizontalLine(ANSI_RESET);
            }

            for (int j = 0; j <= 9; j++) {
                if (i == 0 && j == 0) {
                    System.out.print(ANSI_BLUE + "  | ");
                }
                if (i == 0 && j > 0) {
                    System.out.print(ANSI_BLUE + COLUMN_IDENTIFIERS.charAt(j - 1) + " | ");
                }
                if (i == 0 && j == 9) {
                    System.out.println("");
                }

                if (i > 0) {

                    if (j == 0)
                        System.out.print(10 - i);
                    if (j > 0) {
                        String position = Character.toString(COLUMN_IDENTIFIERS.charAt(j - 1)) + (10 - i);

                        if (j == 1)
                            printVerticalLine(ANSI_BLUE);

                        System.out.print(visualiserStatutCase(etatGrille.get(position)));

                        if (j % 3 == 0)
                            printVerticalLine(ANSI_BLUE);
                        else
                            printVerticalLine(ANSI_RESET);
                    }

                }

            }
            if (i > 0) {
                System.out.println("");
            }

        }

        printHorizontalLine(ANSI_BLUE);

    }

    public static void dessiner_cadre_jeu(HashMap<String, Integer> etatCadre) {

        HashMap<String, Integer> grille = generer_grille_jeu();
        for (HashMap.Entry<String, Integer> entry : etatCadre.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            grille.replace(key, value);
        }

        dessiner_grille_jeu(grille);
    }

    public static void dessiner_cadre_simple(HashMap<String, Integer> etatCadre) {
        // etatCadre.keySet()
        for (int i = 0; i < 3; i++) {
            printHorizontalLine(ANSI_RESET);
            for (int j = 0; j < 3; j++) {
                if (j == 0)
                    printVerticalLine(ANSI_RESET);
                System.out.print('c');
                printVerticalLine(ANSI_RESET);
            }
            System.out.println("");
        }
        printHorizontalLine(ANSI_RESET);
    }

    public static void printHorizontalLine(String color) {
        System.out.println(color + " ----------------------------------------------" + ANSI_RESET);
    }

    public static void printVerticalLine(String color) {
        System.out.print(color + " | " + ANSI_RESET);
    }

    public static String visualiserStatutCase(int statutCase) {
        return (statutCase == 0) ? "-" : obtenirSymboleJoueur(statutCase);
    }
}
