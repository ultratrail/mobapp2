package teamtreehouse.com.iamhere;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

public class Groupe_Activity extends AppCompatActivity {
    Button button_add_someone;
    String id_a_ajouter;
    GroupeAdapter adapter;
    ListView list;

    Membres membres= Membres.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupe_);
        list = (ListView) findViewById(R.id.list);
        adapter = new GroupeAdapter(Groupe_Activity.this, membres);
        list.setAdapter(adapter);

        button_add_someone = (Button) findViewById(R.id.Add_someone);
        button_add_someone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView textView = (TextView)findViewById(R.id.name);
                id_a_ajouter=textView.getText().toString();
                Log.i("ST",id_a_ajouter);
                adapter.add(id_a_ajouter);// ajoute la personne dans ultrateam
                textView.setText("entrez un nom");
            }
        });
    }
}