package teamtreehouse.com.iamhere;

import java.util.ArrayList;

/**
 * Created by romane on 22/03/17.
 */
public class Membres extends ArrayList<String> {
//    private ArrayList<String> membres;

    //a changer
    private Mqtt_client mqtt_client;

    private static Membres ourInstance = new Membres();

    public static Membres getInstance() {
        return ourInstance;
    }

    private Membres() {
//        this.membres=arrayList
        super.add("Client1");
        super.add("Benoit");
        super.add("Cyril");
        super.add("David");
        super.add("Eloise");
        super.add("Florent");
    }


/*    public int nb_Membres (){
        return membres.size();
    }
*/

    public void setMqtt_client(Mqtt_client mqtt_client){
        this.mqtt_client=mqtt_client;
    }

    //peut etre a enlever
    public Mqtt_client getMqtt_client(){
        return mqtt_client;
    }

    @Override
    public boolean add(String a_ajouter){
//        this.membres.add(a_ajouter);
        mqtt_client.subscribeToTopic(a_ajouter);
        return super.add(a_ajouter);
    }

    @Override
    public boolean remove(Object memberName) {
        mqtt_client.unsubscribe((String) memberName);
        return super.remove(memberName);
    }


/*
    public void subscribeTo (String memberName){
        mqtt_clients.subscribeToTopic(memberName);
    }


    public void unsubscribeTo(String memberName) {
        mqtt_clients.unsubscribe(memberName);
    }

    public ArrayList<String> getMembres() {
        return membres;
    }

    public String get(int position) {
        return membres.get(position);
    }
*/
}
