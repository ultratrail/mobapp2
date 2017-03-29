package teamtreehouse.com.iamhere;

import java.util.ArrayList;

/**
 * Created by romane on 22/03/17.
 */
public class Membres extends ArrayList<String> {
//    private ArrayList<String> membres;

    private static Membres ourInstance = new Membres();

    public static Membres getInstance() {
        return ourInstance;
    }

    private Membres() {
        //TODO recuperer cette liste dans un fichier
        super.add("Client1");
        super.add("Benoit");
        super.add("Cyril");
        super.add("David");
        super.add("Eloise");
        super.add("Florent");
    }

    @Override
    public boolean add(String a_ajouter){
        //TODO surement a faire ailleurs
        // mqtt_client.subscribeToTopic(a_ajouter);
        return super.add(a_ajouter);
    }

    @Override
    public boolean remove(Object memberName) {
        //TODO
       // mqtt_client.unsubscribe((String) memberName);
        return super.remove(memberName);
    }
}
