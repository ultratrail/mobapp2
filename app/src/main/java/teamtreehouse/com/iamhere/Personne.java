package teamtreehouse.com.iamhere;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.Date;

/**
 * Created by anthony on 25/03/17.
 */

class Personne {
//TODO ajouter le rythme cardiaque
final float distance = 20;
    private String nom;
    private int id;
    private boolean isPositionSet;
    private LatLng position;
    private Marker marker;
    private LatLng oldPosition;
    private Date dernier_message_recu;

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
    }

    public boolean message_needed() {
        return (position.latitude - oldPosition.latitude) >= distance || (position.longitude - oldPosition.longitude) >= distance;
    }

    public Personne(String nom, int id) {
        this.nom = nom;
        this.id = id;
        this.isPositionSet=false;
    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
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
    }

    public boolean isPositionSet() {
        return isPositionSet;
    }

    public void setPositionSet(boolean positionSet) {
        isPositionSet = positionSet;
    }
}
