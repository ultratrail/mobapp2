package teamtreehouse.com.iamhere;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Groupe_Activity extends AppCompatActivity {
    Button button_add_someone;
    String id_a_ajouter;
    ArrayAdapter<String> adapter;
    ListView list;


    UltraTeamApplication ultraTeamApplication=UltraTeamApplication.getInstance();

    // TODO recuperer la liste qui serait sauvegarde

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupe);


        list = (ListView) findViewById(R.id.list);
        adapter = new ArrayAdapter<String>(Groupe_Activity.this,
                android.R.layout.simple_list_item_1,(ArrayList)ultraTeamApplication.getPersonnes().keys() );
        list.setAdapter(adapter);

        button_add_someone = (Button) findViewById(R.id.Add_someone);
        button_add_someone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView textView = (TextView)findViewById(R.id.name);
                id_a_ajouter=textView.getText().toString();
                Log.i("ST",id_a_ajouter);
                ultraTeamApplication.add_someone(id_a_ajouter);
                adapter.add(id_a_ajouter);
                list.setAdapter(adapter);
                textView.setText("entrez un nom");

            }
        });
    }
}
