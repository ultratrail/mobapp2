package teamtreehouse.com.iamhere;

import android.content.Context;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

}
