package teamtreehouse.com.iamhere;

import android.app.Application;
import android.graphics.Point;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import com.google.android.gms.maps.model.LatLng;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by anthony on 04/03/17.
 */

public class UltraTeamApplication extends Application {

    // Oh le beau singleton !!!
    private static UltraTeamApplication singleton;

    public GroupeAdapter adapter;
    public String monID ="Romane";

    public static UltraTeamApplication getInstance(){
        return singleton;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        personnes=new Hashtable<>();

        personnes.put("you", new Personne(monID, 0));//TODO arrêter de faire n'importe quoi
        singleton = this;
        groupeInitialized = false;
    }

    private Mqtt_client mqtt_client;
    private Hashtable<String, Personne> personnes;

    public int getNbPersonnes(){
        return personnes.size();
    }

   /* public Hashtable<String, Personne> getPersonnes (){
        return personnes;
    }*/


    private Marker base;
    public Mqtt_client getMqtt_client() {
        return mqtt_client;
    }

    public Marker getBase(){
        return base;
    }

    public void setBase(Marker m){
        base=m;
    }

    public void setAdapter(GroupeAdapter g) {
        adapter = g;
    }

    public GroupeAdapter getAdapter() {
        return adapter;
    }

    public void setMqtt_client(Mqtt_client mqtt_client) {
        this.mqtt_client = mqtt_client;
    }

    public void add_someone(String s){
        Log.i("MQTT", "j'ajoute "+s);
        personnes.put(s,new Personne(s,0,new LatLng(10,10)));
        mqtt_client.subscribeToTopic(s);
    }
    public void setPosition(String s, Point p){
        LatLng l = new LatLng(p.x,p.y);
        if (!(personnes.containsKey(s))){
            personnes.put(s,new Personne(s,0,l));
        }
        else personnes.get(s).setPosition(l);
        // TODO necesite de faire quelque chose avec le marker
    }

    public void remove_someone(String s){
        personnes.remove(s);
        //mqtt_client.unsubscribe(s);
        //TODO possible problemme avec le marker
    }


    private boolean groupeInitialized;

    public boolean getGroupeInitialized (){
        return groupeInitialized;
    }

    public void setGroupeInitialized (boolean b){
        groupeInitialized=b;
    }

    public void traiterMessage(Message message) {
        Log.i("MQTT", "je commence a traiter le message");
        //if (message.checked()) {
        Log.i("MQTT", "le message est bon et le nom est : "+ message.getNom() );
        String nom = message.getNom();
        Log.i("MQTT", "La Map est : "+personnes.toString());
            if (personnes.containsKey(nom)) {
                Log.i("MQTT", "je connais cette personne ");
                Personne p = personnes.get(nom);
                if (message.getDate().after(p.getDernier_message_recu())) {
                    Log.i("MQTT", "la date est bonne ");
                    p.setPosition(message.getPos());
                    if (p.getMarker()!= null){
                        Log.i("MQTT", "je change le marker");
                        p.getMarker().position(message.getPos());
                        
                    }
                    else {
                        Log.i("MQTT","traitement de message j'ai recu la position de quelqu'un qui n'as pas de marker");
                    }
                    if (message.isSOS()) {
                        //TODO faire quelque chose
                    }
                    if (message.isHeartRate()) {
                        p.setHeartRate(message.getHeartRate());
                    }
                    p.setDernier_message_recu(message.getDate());
                }





            }
            else {
                Log.i("MQTT", "je ne connais pas cette personne");
            }
    }

    public Hashtable<String, Personne> getPersonnes() {
        return personnes;
    }
}
