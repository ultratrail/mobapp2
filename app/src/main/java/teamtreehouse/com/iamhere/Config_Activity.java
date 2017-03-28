package teamtreehouse.com.iamhere;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

public class Config_Activity extends AppCompatActivity {
// TODO a refaire
    ListView myListView;
    FloatingActionButton button_add_device;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //button_add_device = (FloatingActionButton) findViewById(R.id.Add_device);
        //button_add_device.setOnClickListener(add_deviceListener);

        myListView = (ListView) findViewById(R.id.list);

  //      printDevices();

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
 */
    }

/*
    View.OnClickListener add_deviceListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //TODO  add a bluetooth device. New activity ?
            BluetoothDeviceAdapter adapter = (BluetoothDeviceAdapter) myListView.getAdapter();
            adapter.add(new BluetoothDevice("default name"));
        }
    };



    private void printDevices(){
        BluetoothDevicesSingleton devices = generateDevices();

        BluetoothDeviceAdapter adapter = new BluetoothDeviceAdapter(Config_Activity.this, devices);
        myListView.setAdapter(adapter);

    }

    private BluetoothDevicesSingleton generateDevices() {
        BluetoothDevicesSingleton devicesList = BluetoothDevicesSingleton.getInstance();
        return devicesList;
    }

*/
}
