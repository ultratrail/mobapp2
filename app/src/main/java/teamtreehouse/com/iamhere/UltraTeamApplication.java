package teamtreehouse.com.iamhere;

import android.app.Application;

import java.util.Hashtable;

/**
 * Created by anthony on 04/03/17.
 */

public class UltraTeamApplication extends Application {

    // Oh le beau singleton !!!
    private static UltraTeamApplication singleton;

    public static UltraTeamApplication getInstance(){
        return singleton;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        personnes=new Hashtable<>();
        singleton = this;
    }

    private Hashtable<Integer, Personne> personnes;

    public int getNbPersonnes(){
        return personnes.size();
    }

    public Hashtable<Integer, Personne> getPersonnes (){
        return personnes;
    }


}
