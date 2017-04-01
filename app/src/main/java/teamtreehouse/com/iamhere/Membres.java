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
    }

    @Override
    public boolean add(String a_ajouter){
        //TODO surement a faire ailleurs
        UltraTeamApplication.getInstance().add_someone(a_ajouter);
        return super.add(a_ajouter);
    }

    @Override
    public boolean remove(Object memberName) {
        UltraTeamApplication.getInstance().remove_someone((String)memberName);
        return super.remove(memberName);
    }
}
