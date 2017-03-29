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


    // TODO recuperer la liste qui serait sauvegarde

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupe_);


        list = (ListView) findViewById(R.id.list);
/*        adapter = new ArrayAdapter<String>(Groupe_Activity.this,
                R.layout.list_item, membres.getMembres());
        list.setAdapter(adapter);
*/

        printMembres();

        button_add_someone = (Button) findViewById(R.id.Add_someone);
        button_add_someone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                final View popupView = layoutInflater.inflate(R.layout.popup, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

                Button btnOK = (Button) popupView.findViewById(R.id.ok);
                btnOK.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        TextView textView = (TextView) popupView.findViewById(R.id.id_a_ajouter);
                        id_a_ajouter =  textView.getText().toString();
                        Log.i("ST",id_a_ajouter);
                        membres.add(id_a_ajouter); //bizarre a modifier
 //                       adapter.add(id_a_ajouter); normalement, le membres.add doit suffire car l'adapter se base déjà sur la liste de membres.
                        list.setAdapter(adapter);

                        popupWindow.dismiss();
                    }
                });

                popupWindow.showAsDropDown(button_add_someone, 50, -30);
                */
                TextView textView = (TextView)findViewById(R.id.name);
                id_a_ajouter=textView.getText().toString();
                Log.i("ST",id_a_ajouter);
                adapter.add(id_a_ajouter);
                textView.setText("entrez un nom");

            }
        });


    }
    private void printMembres(){
        adapter = new GroupeAdapter(Groupe_Activity.this, membres);
        list.setAdapter(adapter);
    }

}


