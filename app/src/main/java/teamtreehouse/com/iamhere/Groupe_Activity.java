package teamtreehouse.com.iamhere;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static teamtreehouse.com.iamhere.R.id.textView;

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
                textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            hideKeyboard(v);
                        }
                    }
                });

                id_a_ajouter=textView.getText().toString();
                Log.i("ST",id_a_ajouter);
                if (!membres.contains(id_a_ajouter)) {
                    UltraTeamApplication.getInstance().getAdapter().add(id_a_ajouter);// ajoute la personne dans ultrateam
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "L'identifiant doit être unique!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                //textView.setText("entrez un nom");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}