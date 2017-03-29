package teamtreehouse.com.iamhere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {
//TODO quand cette classe fonctionera suprimer Main activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        UltraTeamApplication ultraTeamApplication= UltraTeamApplication.getInstance();
        // Demarage du client mqtt
        Mqtt_client mqtt_client= new Mqtt_client(getApplicationContext());
        ultraTeamApplication.setMqtt_client(mqtt_client);


        Button Config = (Button) findViewById(R.id.configuration) ;
        Config.setOnClickListener(Listener_config);

        Button Groupe = (Button) findViewById(R.id.groupe);
        Groupe.setOnClickListener(Listener_groupe);

        Button map = (Button) findViewById(R.id.map);
        map.setOnClickListener(Listener_map);

    }

    View.OnClickListener Listener_config = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //TODO config ou bluetooth activity ?
            Intent myIntent = new Intent(Home.this, Config_Activity.class);
            startActivity(myIntent);
        }
    };

    View.OnClickListener Listener_groupe = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent myIntent = new Intent(Home.this, Groupe_Activity.class);
            startActivity( myIntent);
        }
    };

    View.OnClickListener Listener_map = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //TODO le groupe est parametre ?
            Intent myIntent = new Intent(Home.this, MapsActivity.class);
            startActivity( myIntent);
        }
    };

}