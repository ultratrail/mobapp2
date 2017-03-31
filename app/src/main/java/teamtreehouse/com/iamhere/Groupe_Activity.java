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
    ListView list;

    Membres membres= Membres.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupe_);
        getSupportActionBar().setTitle(R.string.groupe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list = (ListView) findViewById(R.id.list);
        UltraTeamApplication.getInstance().setAdapter(new GroupeAdapter(Groupe_Activity.this, membres));
        list.setAdapter(UltraTeamApplication.getInstance().getAdapter());

        button_add_someone = (Button) findViewById(R.id.Add_someone);
        button_add_someone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView textView = (TextView)findViewById(R.id.name);
                id_a_ajouter=textView.getText().toString();
                Log.i("ST",id_a_ajouter);
                UltraTeamApplication.getInstance().getAdapter().add(id_a_ajouter);// ajoute la personne dans ultrateam
                //textView.setText("entrez un nom");
            }
        });
    }
}