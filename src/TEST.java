import java.util.HashMap;

public class TEST {
    public static void main(String[] args) {
        int[] cadre = {2,2};
        HashMap<String, Integer> map = JeuUtils.initTab();
        map.replace("F6", 2);
        map.replace("F5", 2);
        MinmaxTree mTree = new MinmaxTree(2, map);
        mTree.creatTree(cadre);
        Node root = mTree.rootNode; 
        root.decision = "D6";
        Node testNode = new Node();
        
    
       // Node rootChild1 = root.children.get(0);


        /*
        System.out.println("TEST ");
        System.out.println("Root Node cadre :" + root.getMap().toString());
        System.out.println("Root Node type " + root.typeNode);
        System.out.println("Root Node symboleduJoueur " + root.symboleDuJoueurActuel);
        //TEST the minimax method
        
        //Children of root
        for(int i = 0; i < root.children.size(); i++ ){
           System.out.println( "Child 1."+i+" cadre:"+root.children.get(i).getMap().toString());
           System.out.println("Node type is " + root.children.get(i).typeNode);
           System.out.println("Heuristique value: " + root.children.get(i).heuristiqueCounter);
            System.out.println("Is this leaf: " + root.children.get(i).isLeaf);
            System.out.println("Root child symboleduJoueur " + root.children.get(i).symboleDuJoueurActuel);

        }
        
        //Children of children
        
        for(int i = 0; i < rootChild1.children.size(); i++ ){
            System.out.println( "Child 2."+i+" cadre:"+ rootChild1.children.get(i).getMap().toString());
            System.out.println("Node type is " + rootChild1.children.get(i).typeNode);
            System.out.println("Heuristique value: " + rootChild1.children.get(i).heuristiqueCounter);
            System.out.println("Is this leaf: " + rootChild1.children.get(i).isLeaf);
            System.out.println("Child child symboleduJoueur " + rootChild1.children.get(i).symboleDuJoueurActuel);
        }
        */
        //It is getting the number substring of the case keys.
       // 
       /* 
        String printBoard = "";
        for(int i = 0; i < 3; i++){
            printBoard += "\n";
            for(int j =0; j < 3; j++){
                printBoard += board[i][j] + " ";
            }
        }
        testNode.board = board;
        testNode.isLeaf = true;
        testNode.symboleDuJoueurActuel = 4;
        System.out.println(mTree.minimax(testNode, testNode.symboleDuJoueurActuel, testNode.typeNode));
        System.out.println("Board: "+ printBoard);
        */

        //mTree.minimax(root, -100, 100);
        //mTree.determinTicTacToe(root);
        //JeuUtils.detailsNode(root);
        //System.out.println("RootChild1 score:"+ rootChild1.pointage);

       


        
        


    }
}
