import java.util.HashMap;

//Tree a interoger pour savoir les pourcentages de ganger pour chaque coups possible. Il faut recréer le tree
//après chaque coup. Donc le tree n'est pas statique.
//Il faut reécrire dans le tree les positions et options possible dans le case 3x3 envisager. 
//Le tree envoie le meilleur choix possible après des calcules internes.
public class MinmaxTree {

    private HashMap <String, Integer> etatCases;
    int[] cadre;
    String symboleDuJoeur;//X ou O.
    Node rootNode;

    //Les 3 cadres de bas sur le plateau de jeu
    private String[] cadreBasGauche = {"A1","A2","A3","B1","B2","B3","C1","C2","C3"};
    private String[] cadreBasMilieu = {"D1","D2","D3","E1","E2","E3","F1","F2","F3"};
    private String[] cadreBasDriote = {"G1","G2","G3","H1","H2","H3","I1","I2","I3"};
        
    //Les 3 cadres de centre sur le plateau de jeu
    private String[] cadreGauche = {"A4","A5","A6","B4","B5","B6","C4","C5","C6"};
    private String[] cadreMilieu = {"D4","D5","D6","E4","E5","E6","F4","F5","F6"};
    private String[] cadreDroite = {"G4","G5","G36","H4","H5","H6","I4","I5","I6"};

    //Les 3 cadres de haut sur le plateau de jeu
    private String[] cadreHautGauche = {"A7","A8","A9","B7","B8","B9","C7","C8","C9"};
    private String[] cadreHautMilieu = {"D7","D8","D9","E7","E8","E9","F7","F8","F9"};
    private String[] cadreHautDroite = {"G7","G8","G9","H7","H8","H9","I7","I8","I9"};


    //Créer le tree à partire de l'état du cadre courante. 
    //Dépendant de la liste de position à checker donné, la methode va interoger le hashMap pour
    //savoir l'état et construire l'arbre.
    //Il va construir l'état actuel avec des enfant designants les états future dependant des choix fait.
    //Il doit être capable de générer les hashmaps à partie des hashmaps. 
    public void creatTree(int[] interval, HashMap <String, Integer> etatCases){
        String positions[] = processInterval(interval);
        this.etatCases = etatCases;  
        rootNode = new Node();
        Integer value;
        //for loop pour interoger le hashmap pour avoir info sur le cadre en question.
        for(int i = 0; i < positions.length; i++ ){
            value = etatCases.get(positions[i]);
            rootNode.put(positions[i],value);
        }
        creatChildren(rootNode);
    }

    //Pour chaque case vide, créer un scenario possible avec le choix fait.
    private void creatChildren(Node node){
        //Le cadre actuel dans le noed est un Hashmap
        for (HashMap.Entry<String, Integer> entry : node.getMap().entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            if(value == 0){
                value =  Integer.valueOf(symboleDuJoeur); 
                node.creatAChild(key, value);
            }
        }
        //Pour chaque prediction de tour du joueur, changer le symmbole pour essayer de prédire l'action de l'adversaire.
        if(symboleDuJoeur.equals("X")){
            symboleDuJoeur = "Y";
        }else if(symboleDuJoeur.equals("Y")){
            symboleDuJoeur = "X";
        }
        //while this node has children, call this function recursivvely.
        for(int i = 0; i < node.children.size(); i++ ){
            creatChildren(node.children.get(i));
        }
        
    }
    

    //recois la case et retourn l'identification de la position corespondant à la case.
    public String genererMeilleurDecision(int[] cadre){
        //Random number in the interval of the root node children arraylist.
         int index = (int) Math.random() % rootNode.children.size();
         
         return rootNode.children.get(index).desision;
    }

    public void setEtatCases(HashMap <String, Integer> etatCases){
        this.etatCases = etatCases;
    }

    //Envoie une liste de tous les combinaison du cadre approprié 
    private String[] processInterval(int[] cadre){
        
        if(cadre[1] == 1 ){//Si la ligne est la première
            if(cadre[0] == 1){//Si la colone est la première (celui du plus bas)
                return cadreBasGauche;
            }else if(cadre[0] == 2){
                return cadreBasMilieu;
            }else if(cadre[0] == 3){
                return cadreBasDriote;
            }
            
        }else if(cadre[1] == 2){
            if(cadre[0] == 1){//Si la colone est la deuxième (celui du plus bas)
                return cadreGauche;
            }else if(cadre[0] == 2){
                return cadreMilieu;
            }else if(cadre[0] == 3){
                return cadreDroite;
            }
        }else if(cadre[1] == 3){
            if(cadre[0] == 1){//Si la colone est la deuxième (celui du plus bas)
                return cadreHautGauche;
            }else if(cadre[0] == 2){
                return cadreHautMilieu;
            }else if(cadre[0] == 3){
                return cadreHautDroite;
            }
        }

        
        
        return null;
    }
}




/*
 * Memo:
 * 
 * Pour chaque enfant du Node, setter le nouveau symbole et creer des enfants pour chaque enfant des autre desision possible.
 * - Creat the new Node
 * - Set the symbole
 * - add node to the children list of parent
 * - 
 * - Create the scenario
 *  - Recursive call until no more "0" in the cadre.
 */