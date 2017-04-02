package teamtreehouse.com.iamhere;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;


public class MainActivity extends AppCompatActivity {
    private TextView mDataField;
    public static boolean BLUETOOTH_SERVICE_ACTIVE;
    private boolean BLUETOOTH_SERVICE_REGISTER;

    public Button valider;
    UltraTeamApplication ultraTeamApplication= UltraTeamApplication.getInstance();
    Hashtable<String,Personne> personnes = ultraTeamApplication.getPersonnes();


    @Override
    protected void onResume(){
        super.onResume();
        if(BLUETOOTH_SERVICE_ACTIVE) {
            registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
            BLUETOOTH_SERVICE_REGISTER = true;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        UltraTeamApplication ultraTeamApplication = UltraTeamApplication.getInstance();

        // Demarage du client mqtt
        Mqtt_client mqtt_client = new Mqtt_client(getApplicationContext());
        ultraTeamApplication.setMqtt_client(mqtt_client);

        //Bluetooth
        mDataField = (TextView) findViewById(R.id.data_value_service);
        setSupportActionBar(toolbar);
        if(BLUETOOTH_SERVICE_ACTIVE) {
            registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
            BLUETOOTH_SERVICE_REGISTER = true;
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //TODO
            }
        });

        //entre de coordonnées manuellement
        valider = (Button) findViewById(R.id.valider);
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Personne p = UltraTeamApplication.getInstance().getPersonnes().get("you");
                Personne p =personnes.get("you");
                TextView latitude = (TextView) findViewById(R.id.latitude2);
                TextView longitude = (TextView) findViewById(R.id.longitude);
                double lat = Double.valueOf(latitude.getText().toString());
                double lon = Double.valueOf(longitude.getText().toString());
                LatLng latLng = new LatLng(lat, lon);
                p.setPosition(latLng);
            }
        });


        Button goToMapActivity = (Button) findViewById(R.id.goToMapActivity);
        goToMapActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UltraTeamApplication.getInstance().getGroupeInitialized()){
                    if(BLUETOOTH_SERVICE_REGISTER) {
                        unregisterReceiver(mGattUpdateReceiver);
                        BLUETOOTH_SERVICE_REGISTER = false;
                    }
                    //TODO a changer
                    //initialisation de la carte ... ajout des personnes de l'adapter dans la classes personne bizarre de faire ca
                    //comme ca non ...


                    LatLng mexico = new LatLng(19, -99);
                    for (int i = 0; i < UltraTeamApplication.getInstance().getAdapter().getCount(); i++) {
                        mexico = new LatLng(mexico.latitude + 1, mexico.longitude + 1);
                        //je pense pas que ce soit utile
                        //personnes.put(UltraTeamApplication.getInstance().getAdapter().getItem(i), new Personne(UltraTeamApplication.getInstance().getAdapter().getItem(i), i, mexico));
                    }
                    //....
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    startActivity(intent);
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "La gestion des groupes n'est pas faites";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }


            }
        });

        Button goToGroupActivity = (Button) findViewById(R.id.goToGroupActivity);
        goToGroupActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (BLUETOOTH_SERVICE_REGISTER) {
                    unregisterReceiver(mGattUpdateReceiver);
                    BLUETOOTH_SERVICE_REGISTER = false;

                }

                Intent intent = new Intent(MainActivity.this, Groupe_Activity.class);
                startActivity(intent);
            }
        });

        Button goToBluetoothActivity = (Button) findViewById(R.id.goToBluetoothActivity);
        goToBluetoothActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(BLUETOOTH_SERVICE_REGISTER){
                    unregisterReceiver(mGattUpdateReceiver);
                    BLUETOOTH_SERVICE_REGISTER = false;

                }

                Intent intent = new Intent(MainActivity.this, BluetoothActivity.class);
                startActivity(intent);
            }
        });
    }

    //gestion des données bluetooth
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };
    //TODO traiter les données bluetooth
    private void displayData(String data) {
        if (data != null) {
            mDataField.setText(data);
        }
    }


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

}
