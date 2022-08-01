public class Case {
    
    private int[] indexDansCadre;
    private String id;
    private int symbole;
    
    public Case(String id, int[] indexDansCadre) {
        this.id = id;
        this.indexDansCadre = indexDansCadre;
    }

    public String getId() {
        return id;
    }

    public int getSymbole() {
        return symbole;
    }

    public void setSymbole(int symbole) {
        this.symbole = symbole;
    }

    public int[] getIndexDansCadre() {
        return indexDansCadre;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return this.getId();
    }
}
