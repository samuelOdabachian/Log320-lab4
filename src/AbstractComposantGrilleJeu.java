public abstract class AbstractComposantGrilleJeu {
    
    private int symbole = 0;
    private int[] index = new int[2];

    AbstractComposantGrilleJeu(int[] index) {
        this.index = index;
    }

    public int[] getIndex() {
        return index;
    }

    public void setIndex(int[] index) {
        this.index = index;
    }

    public int getSymbole() {
        return symbole;
    }

    public void setSymbole(int symbole) {
        this.symbole = symbole;
    }
    
    @Override
    public String toString() {
        return "["+this.symbole+"]";
    }
}
