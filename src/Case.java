public class Case extends AbstractComposantGrilleJeu {

    private String id;

    public Case(String id, int[] indexDansCadre) {
        super(indexDansCadre);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return this.getId();
    }
}
