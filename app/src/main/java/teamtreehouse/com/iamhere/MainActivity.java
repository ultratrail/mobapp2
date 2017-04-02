package teamtreehouse.com.iamhere;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Hashtable;


public class MainActivity extends AppCompatActivity {
    private TextView mDataField;
    public static boolean BLUETOOTH_SERVICE_ACTIVE;
    private boolean BLUETOOTH_SERVICE_REGISTER;
    private MediaPlayer mediaPlayer;


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
            }
        });

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.enavant);

        Button goToMapActivity = (Button) findViewById(R.id.goToMapActivity);
        goToMapActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UltraTeamApplication.getInstance().getGroupeInitialized()){
                    if(BLUETOOTH_SERVICE_REGISTER) {
                        unregisterReceiver(mGattUpdateReceiver);
                        BLUETOOTH_SERVICE_REGISTER = false;
                    }
                    //SeekBar sb = (SeekBar) findViewById(R.id.seekBarNombreMembre);
                    Hashtable<String, Personne> personnes = UltraTeamApplication.getInstance().getPersonnes();
                    for (int i = 0; i < UltraTeamApplication.getInstance().getAdapter().getCount(); i++)
                        personnes.put(UltraTeamApplication.getInstance().getAdapter().getItem(i), new Personne(UltraTeamApplication.getInstance().getAdapter().getItem(i), i, null));


                    mediaPlayer.start();

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

        //SeekBar sb = (SeekBar) findViewById(R.id.seekBarNombreMembre);
        /*sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                TextView tv = (TextView) findViewById(R.id.textViewNbMembre);
                tv.setText(String.valueOf(seekBar.getProgress()));


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };

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
