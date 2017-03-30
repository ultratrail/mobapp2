package teamtreehouse.com.iamhere;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Hashtable;

public class MainActivity extends AppCompatActivity {
    private TextView mDataField;
    public static boolean BLUETOOTH_SERVICE_ACTIVE;
    private boolean BLUETOOTH_SERVICE_REGISTER;


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

        Button goToMapActivity = (Button) findViewById(R.id.goToMapActivity);
        goToMapActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BLUETOOTH_SERVICE_REGISTER) {
                    unregisterReceiver(mGattUpdateReceiver);
                    BLUETOOTH_SERVICE_REGISTER = false;
                }
                SeekBar sb = (SeekBar) findViewById(R.id.seekBarNombreMembre);

                Hashtable<Integer, Personne> personnes = UltraTeamApplication.getInstance().getPersonnes();

                for (int i = 1; i<= sb.getProgress(); i++){
                    personnes.put(i,new Personne("Zimoule " + i, i, null));
                }

                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
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

        SeekBar sb = (SeekBar) findViewById(R.id.seekBarNombreMembre);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        });
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
