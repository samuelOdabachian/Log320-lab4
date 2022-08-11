import java.io.*;
import java.net.*;

class Client {

  private static boolean IS_AI_ACTIVE = true;
  private static final boolean ACTIVATE_SERVER_MODE = true;

  private static final int SYMBOLE_O = 2;
  private static final int SYMBOLE_X = 4;

  private static int SYMBOLE_JOUEUR = -1;

  public static void main(String[] args) {

    if (ACTIVATE_SERVER_MODE) {
      startGameOnServer(args);
    } else {
      System.out.println("TEST MODE");
      TicTacToeAI notreAI = new TicTacToeAI();
      SYMBOLE_JOUEUR = SYMBOLE_X;
      notreAI.jouer(SYMBOLE_JOUEUR, "B8");
    }
  }

  public static void startGameOnServer(String[] args) {
    System.out.println("SERVER MODE");
    TicTacToeAI notreAI = new TicTacToeAI();

    Socket MyClient;
    BufferedInputStream input;
    BufferedOutputStream output;
    int[][] board = new int[9][9];

    int port = 8888;
    String host = "localhost";
    if (args.length == 2) {
      port = Integer.parseInt(args[1]);
      host = args[0];
    }

    try {
      MyClient = new Socket(host, port);
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
