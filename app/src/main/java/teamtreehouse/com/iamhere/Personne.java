package teamtreehouse.com.iamhere;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by anthony on 25/03/17.
 */

class Personne {
//TODO ajouter le rythme cardiaque

    private String nom;

    private int id;

    private boolean isPositionSet;
    private LatLng position;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    private Marker marker;

    public Personne(String nom, int id, LatLng position) {
        this.nom = nom;
        this.id = id;
        this.position = position;
        this.isPositionSet=true;
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
    }

    public boolean isPositionSet() {
        return isPositionSet;
    }

    public void setPositionSet(boolean positionSet) {
        isPositionSet = positionSet;
    }
}
