import java.util.HashMap;

//Tree a interoger pour savoir les pourcentages de ganger pour chaque coups possible. Il faut recréer le tree
//après chaque coup. Donc le tree n'est pas statique.
//Il faut reécrire dans le tree les positions et options possible dans le case 3x3 envisager. 
//Le tree envoie le meilleur choix possible après des calcules internes.
public class MinmaxTree {

    private HashMap <String, Integer> etatCases;
    int[] cadre;


    //Créer le tree à partire de l'état du cadre courante. 
    //Dépendant de la liste de position à checker donné, la methode va interoger le hashMap pour
    //savoir l'état et construire l'arbre.
    //Il va construir l'état actuel avec des enfant designants les états future dependant des choix fait.
    //Il doit être capable de générer les hashmaps à partie des hashmaps. 
    public void creatTree(int[][] interval, HashMap <String, Integer> etatCases){
   // String positions[] = processInterval(interval);
       //for loop to interogat etatCases
    }

    //recois la case et retourn l'identification de la position corespondant à la case.
    public String genererMeilleurDecision(int[] cadre){
        
        
        return "Test1";
    }

    public void setEtatCases(HashMap <String, Integer> etatCases){
        this.etatCases = etatCases;
    }

    //Envoie une liste de tous les combinaison du cadre approprié 
    private String[] processInterval(int[] cadre){
        String[] positions = {"Test1","Test2"}; 
        //loop in loop to wright all possible combinations
        return positions;
    }
}
