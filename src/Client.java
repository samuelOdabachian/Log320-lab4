import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.HashMap;

class Client {

  private static boolean IS_AI_ACTIVE = true;
  private static final boolean ACTIVATE_SERVER_MODE = true;

  private static final int CASE_VIDE = 0;
  private static final int SYMBOLE_O = 2;
  private static final int SYMBOLE_X = 4;

  private static int SYMBOLE_JOUEUR = -1;

  private static void testCaseMapper() {
    HashMap<String, int[]> listeIndexCadresEtCases = new HashMap<String, int[]>();
    Cadre[][] m = new Cadre[3][3];

    JeuUtils.caseIndexMapper(listeIndexCadresEtCases, m);
    // HashMap<String, int[]> test = JeuUtils.caseIndexMapper();
    System.out.println(m.length + " :: " + m[0].length);
    // for (int i = 0; i < 3; i++) {
    // for (int j = 0; j < 3; j++) {
    // // System.out.println( Arrays.toString(m[i][j].getIndex()) );
    // // System.out.println(m[i][j].getCases().length );
    // }
    // }
    // int counter = 0;
    // for (Cadre[] cadres : m) {
    // for (Cadre cadre : cadres) {
    // for (Case[] cases : cadre.getCases()) {
    // for (Case uneCase : cases) {
    // counter++;
    // System.out.println(counter+" :: "+uneCase.getId());
    // }
    // }
    // }
    // }

    System.out.println("FIND Cadre with C1");
    // System.out.println(
    // Arrays.toString(JeuUtils.getCadreFromMapper(listeIndexCadresEtCases, m,
    // "D5").getIndex()) );
    // System.out.println(
    // Arrays.toString(JeuUtils.getCaseFromMapper(listeIndexCadresEtCases, m,
    // "D5").getIndexDansCadre()) );
    Cadre cadre = JeuUtils.getCadreFromMapper(listeIndexCadresEtCases, m, "C1");
    Case case1 = JeuUtils.getCaseFromMapper(listeIndexCadresEtCases, m, "C1");
    case1.setSymbole(2);
    Case case2 = JeuUtils.getCaseFromMapper(listeIndexCadresEtCases, m, "A1");
    case2.setSymbole(2);
    Case case3 = JeuUtils.getCaseFromMapper(listeIndexCadresEtCases, m, "B3");
    case3.setSymbole(2);

    for (Case[] listeCases : cadre.getCases()) {
      for (Case cetteCase : listeCases) {
        System.out.println("Case " + cetteCase.getId());
      }
    }

    Boolean estCoupGagnant = JeuUtils.estCoupGagnant(cadre, case1.getIndex(), 2);

    System.out.println(estCoupGagnant);
  }

  public static void main(String[] args) {

    if (ACTIVATE_SERVER_MODE) {
      startGameOnServer(args);
    } else {
      System.out.println("TEST MODE");

      TicTacToeAI notreAI = new TicTacToeAI();
      SYMBOLE_JOUEUR = SYMBOLE_X;
      // notreAI.jouer(SYMBOLE_JOUEUR, "C7");
      // notreAI.jouer(SYMBOLE_JOUEUR, "E6");
      // notreAI.jouer(SYMBOLE_JOUEUR, "A7");
      // notreAI.jouer(SYMBOLE_JOUEUR, "C9");
      notreAI.jouer(SYMBOLE_JOUEUR, "B8");
      // notreAI.jouer(SYMBOLE_JOUEUR, "A9");
      // notreAI.rejouer();
      // notreAI.rejouer();
      // System.out.println("\n");

      // MinmaxTree minmaxTree = new MinmaxTree();
      // try to printout the tree during the creation instead of making a new methode.
      // minmaxTree.creatTree(testCase, notreAI.getGrille());
    }
  }

  public static void startGameOnServer(String[] args) {
    System.out.println("SERVER MODE");
    TicTacToeAI notreAI = new TicTacToeAI();

    Socket MyClient;
    BufferedInputStream input;
    BufferedOutputStream output;
    int[][] board = new int[9][9];

    try {
      MyClient = new Socket("localhost", 8888);
      input = new BufferedInputStream(MyClient.getInputStream());
      output = new BufferedOutputStream(MyClient.getOutputStream());
      BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
      while (1 == 1) {
        char cmd = 0;

        cmd = (char) input.read();
        System.out.println(cmd);
        // Debut de la partie en joueur blanc
        if (cmd == '1') {
          byte[] aBuffer = new byte[1024];

          int size = input.available();
          // System.out.println("size " + size);
          input.read(aBuffer, 0, size);
          String s = new String(aBuffer).trim();
          System.out.println(s);
          String[] boardValues;
          boardValues = s.split(" ");
          int x = 0, y = 0;
          for (int i = 0; i < boardValues.length; i++) {
            board[x][y] = Integer.parseInt(boardValues[i]);
            x++;
            if (x == 9) {
              x = 0;
              y++;
            }
          }

          System.out.println("Nouvelle partie! Vous jouer blanc, entrez votre premier coup : ");
          String move = null;
          // On assigne un symbole au joueur
          SYMBOLE_JOUEUR = SYMBOLE_X; // TODO: Revoir methode d'assigner symbole
          move = (IS_AI_ACTIVE) ? notreAI.jouer(SYMBOLE_JOUEUR) : console.readLine(); // TODO: Revoir
          output.write(move.getBytes(), 0, move.length());
          output.flush();
        }
        // Debut de la partie en joueur Noir
        if (cmd == '2') {
          System.out.println("Nouvelle partie! Vous jouer noir, attendez le coup des blancs");
          byte[] aBuffer = new byte[1024];

          int size = input.available();
          // System.out.println("size " + size);
          // On assigne un symbole au joueur
          SYMBOLE_JOUEUR = SYMBOLE_O; // TODO: Revoir methode d'assigner symbole
          input.read(aBuffer, 0, size);
          String s = new String(aBuffer).trim();
          System.out.println(s);
          String[] boardValues;
          boardValues = s.split(" ");
          int x = 0, y = 0;
          for (int i = 0; i < boardValues.length; i++) {
            board[x][y] = Integer.parseInt(boardValues[i]);
            x++;
            if (x == 9) {
              x = 0;
              y++;
            }
          }
        }

        // Le serveur demande le prochain coup
        // Le message contient aussi le dernier coup joue.
        if (cmd == '3') {
          byte[] aBuffer = new byte[16];

          int size = input.available();
          System.out.println("size :" + size);
          input.read(aBuffer, 0, size);

          String s = new String(aBuffer);
          System.out.println("Dernier coup :" + s);
          System.out.println("Entrez votre coup : ");
          String move = null;
          // move = console.readLine();
          // Mettre a jouer dernier coup jouer par nous dans la grille
          move = (IS_AI_ACTIVE) ? notreAI.jouer(SYMBOLE_JOUEUR, s) : console.readLine();
          output.write(move.getBytes(), 0, move.length());
          output.flush();

        }
        // Le dernier coup est invalide
        if (cmd == '4') {
          System.out.println("Coup invalide, entrez un nouveau coup : ");
          // !!!

          String move = null;
          move = (IS_AI_ACTIVE) ? notreAI.rejouer() : console.readLine(); // AJOUT
          output.write(move.getBytes(), 0, move.length());
          output.flush();

        }
        // La partie est terminée
        if (cmd == '5') {
          byte[] aBuffer = new byte[16];
          int size = input.available();
          input.read(aBuffer, 0, size);
          String s = new String(aBuffer);
          System.out.println("Partie Terminé. Le dernier coup joué est: " + s);
          String move = null;
          move = console.readLine();
          output.write(move.getBytes(), 0, move.length());
          output.flush();

        }
      }
    } catch (IOException e) {
      System.out.println(e);
    }

  }
}
