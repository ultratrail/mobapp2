package teamtreehouse.com.iamhere;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.example.mqtt_service.MqttAndroidClient;

//import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by romane on 22/03/17.
 */

public class Mqtt_client  {
    MqttAndroidClient mqttAndroidClient;

    final String serverUri = "tcp://iot.eclipse.org:1883";

    String clientId = "Client1";
    final String subscriptionTopic = "exampleAndroidTopic";
    final String publishTopic = clientId;
    final String publishMessage = "Hello World!";



    public Mqtt_client(Context applicationContext){
        //clientId = clientId + System.currentTimeMillis();

        mqttAndroidClient = new MqttAndroidClient(applicationContext, serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                if (reconnect) {
                    Log.i("MQTT","Reconnected to : " + serverURI);
                    //addToHistory("Reconnected to : " + serverURI);
                    // Because Clean Session is true, we need to re-subscribe
                    //subscribeToTopic();
                    subscribeToallTopic();
                } else {
                    Log.i("MQTT","Connected to: " + serverURI);

                    //addToHistory("Connected to: " + serverURI);
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.i("MQTT","The Connection was lost.");
                //addToHistory("The Connection was lost.");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.i("MQTT","Incoming message: " + new String(message.getPayload()));
                //TODO en faire quelque chose
                //Recuperer les info dans le message.
                //Message m = new Message(message.toString());
                //Postions.getInstance().modfierPosition(m.getPoint(),m.getId());


                //addToHistory("Incoming message: " + new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });



        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);


        try {
            //addToHistory("Connecting to " + serverUri);
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                   //TODO peut etre a remettre
                    // mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    //subscribeToTopic();
                    subscribeToallTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i("MQTT","Failed to connect to: " + serverUri);
                    //addToHistory("Failed to connect to: " + serverUri);
                }
            });


        } catch (MqttException ex){
            ex.printStackTrace();
        }

    }

    public void subscribeToTopic(){
        try {
            mqttAndroidClient.subscribe("ultratrail/"+subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i("MQTT","Subscribed!  "+subscriptionTopic);
                    //addToHistory("Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i("MQTT","Failed to subscribe to "+subscriptionTopic);
                    //addToHistory("Failed to subscribe");
                }
            });

            // THIS DOES NOT WORK!
            mqttAndroidClient.subscribe(subscriptionTopic, 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // message Arrived!
                    Log.i("MQTT", "Message incomming2 dans subscribe sans topic special");
                    System.out.println("Message: " + topic + " : " + new String(message.getPayload()));
                }
            });

        } catch (MqttException ex){
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }

    public void subscribeToTopic(final String topic){
        try {
            Log.i("MQTT","tentative de soubscription "+ topic);
            mqttAndroidClient.subscribe("ultratrail/"+topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i("MQTT","Subscribed!"+ topic);
                    //addToHistory("Subscribed!");
                    publishMessage();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i("MQTT","Failed to subscribe"+topic);
                    //addToHistory("Failed to subscribe");
                }
            });

            // THIS DOES NOT WORK!
            mqttAndroidClient.subscribe(topic, 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // message Arrived!
                    Log.i("MQTT", "Message incomming2 dans subscribe avec topic : "+ topic);
                    System.out.println("Message: " + topic + " : " + new String(message.getPayload()));
                }
            });

        } catch (MqttException ex){
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }

    public void subscribeToallTopic(){
        Log.i("MQTT", "je souscrit a tous les topics");
        //TODo adapter et decomanter
        //for (int i=0; i<Membres.getInstance().nb_Membres();i++){
        //    subscribeToTopic(Membres.getInstance().get(i));
        //}
    }


    public void publishMessage(){

        try {
            MqttMessage message = new MqttMessage();
            //Message m = new Message(this.clientId,Postions.getInstance().myPosition);

            //message.setPayload(m.toString().getBytes());
            //message.setPayload(publishMessage.getBytes());
            mqttAndroidClient.publish(publishTopic, message);
            Log.i("MQTT","Message Published...  "+publishTopic+" : "+message);

            if(!mqttAndroidClient.isConnected()){
              //  Log.i("MQTT",mqttAndroidClient.getBufferedMessageCount() + " messages in buffer.");
                //addToHistory(mqttAndroidClient.getBufferedMessageCount() + " messages in buffer.");
            }
        } catch (MqttException e) {
            System.err.println("Error Publishing: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
