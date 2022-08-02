import java.util.Arrays;

public class Cadre extends AbstractComposantGrilleJeu {

    private int[] index;

    // private int gagnant = 0;
    private Case[][] cases = new Case[3][3];

    public Cadre(int[] index) {
        super(index);
        this.index = index;
    }

    // public Cadre(Case[][] cases) {
    //     this.cases = cases;
    // }

    // public int getGagnant() {
    //     return gagnant;
    // }

    // public void setGagnant(int gagnant) {
    //     this.gagnant = gagnant;
    // }

    public void setCases(Case[][] cases) {
        this.cases = cases;
    }

    public void setSymboleCaseAtIndex(int row, int col, int symbole) {
        this.cases[row][col].setSymbole(symbole);
    }

    public void setCaseAtIndex(int row, int col, Case caseCadre) {
        this.cases[row][col] = caseCadre;
    }

    public Case[][] getCases() {
        return cases;
    }

    public int[] getIndex() {
        return index;
    }

    public void printBoard() {
        String printBoard = "";
        for (int i = cases.length - 1; i >= 0; i--) {
            
            for (int j = 0; j < cases[i].length; j++) {
                printBoard += cases[i][j].getSymbole() + " ";
            }
            printBoard += "\n";
        }
        String color;
        switch (this.getSymbole()) {
            case 2:
                color = JeuUtils.ANSI_RED;
                break;
            case 4:
                color = JeuUtils.ANSI_PURPLE;
                break;
            default:
                color = JeuUtils.ANSI_RESET;
                break;
        }
        System.out.println(color + "Grid: " + Arrays.toString(this.index) + JeuUtils.ANSI_RESET); 
        System.out.println(color + printBoard + JeuUtils.ANSI_RESET); 
    }
}