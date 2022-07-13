public class Utils {
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
