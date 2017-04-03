package teamtreehouse.com.iamhere;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;

/**
 * Created by anthony on 25/03/17.
 */

class Personne {

final float distance = 1;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    private String nom;
    private int id;
    private boolean isPositionSet;
    private LatLng position;
    private Marker marker;
    private LatLng oldPosition;
    private Date dernier_message_recu= new Date((long)0);
    private int HeartRate;
    private boolean isHeartRate;


    public Date getDernier_message_recu() {
        return dernier_message_recu;
    }

    public void setDernier_message_recu(Date dernier_message_recu) {
        this.dernier_message_recu = dernier_message_recu;
    }
    public int getHeartRate() {
        return HeartRate;
    }

    public void setHeartRate(int heartRate) {
        HeartRate = heartRate;
        isHeartRate = true;
    }

    public boolean isHeartRate() {
        return isHeartRate;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Personne(String nom, int id, LatLng position) {
        this.nom = nom;
        this.id = id;
        this.position = position;
        this.isPositionSet=true;
        this.isHeartRate = false;
    }

    public Personne(String nom, int id) {
        this.id = id;
        this.nom = nom;
        this.isPositionSet = false;
        this.isHeartRate = false;
    }

    public boolean message_needed() {
        if (oldPosition == null) {
            oldPosition = position;
            return true;
        }
        boolean b = (position.latitude - oldPosition.latitude) >= distance || (position.longitude - oldPosition.longitude) >= distance;
        if (b) {
            oldPosition = position;
            return true;
        } else return false;
    }

    public Personne(int id) {
        this.id = id;
        this.isPositionSet=false;
        this.isHeartRate = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
        this.isPositionSet=true;
        if (this.nom!= UltraTeamApplication.getInstance().monID) {
            Log.i("MQTT","je publie sans mon nom");
            //UltraTeamApplication.getInstance().getMqtt_client().publishMessage(this.getNom());
        }
        else {
            //UltraTeamApplication.getInstance().getMqtt_client().publishMessage();
            Log.i("MQTT", "je publie en mon nom");
        }
    }

    public boolean isPositionSet() {
        return isPositionSet;
    }

    public void setPositionSet(boolean positionSet) {
        isPositionSet = positionSet;
    }
}
