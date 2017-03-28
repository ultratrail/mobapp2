package teamtreehouse.com.iamhere;

import android.app.Application;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
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

    private Mqtt_client mqtt_client;
    private Hashtable<String, Personne> personnes;

    public int getNbPersonnes(){
        return personnes.size();
    }

    public Hashtable<String, Personne> getPersonnes (){
        return personnes;
    }

    public Mqtt_client getMqtt_client() {
        return mqtt_client;
    }

    public void setMqtt_client(Mqtt_client mqtt_client) {
        this.mqtt_client = mqtt_client;
    }

    public void add_someone(String s){

        personnes.put(s,new Personne(s,0,new LatLng(0,0)));
        //TODO a changer
    }

}
