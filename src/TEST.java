public class TEST {
    public static void main(String[] args) {
        int[] cadre = {2,2};
        
        MinmaxTree mTree = new MinmaxTree(4, JeuUtils.initTab());
        mTree.creatTree(cadre);
        Node root = mTree.rootNode;
        int [][] board;
        Node rootChild1 = root.children.get(0);

        System.out.println("TEST ");
        System.out.println("Root Node cadre :" + root.getMap().toString());

        /*Children of root
        for(int i = 0; i < root.children.size(); i++ ){
           System.out.println( "Child 1."+i+" cadre:"+root.children.get(i).getMap().toString());
      
        }
        
        Children of children
        
        for(int i = 0; i < rootChild1.children.size(); i++ ){
            System.out.println( "Child 2."+i+" cadre:"+ rootChild1.children.get(i).getMap().toString());
      
        }
        It is getting the number substring of the case keys.
        mTree.minimax(rootChild1, 2);
        board = rootChild1.board;
        String printBoard = "";
        for(int i = 0; i < 3; i++){
            printBoard += "\n";
            for(int j =0; j < 3; j++){
                printBoard += board[i][j] + " ";
            }
        }
        System.out.println("rootChild1 board: "+printBoard);*/
    }
}
