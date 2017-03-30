package teamtreehouse.com.iamhere;

import java.util.ArrayList;

/**
 * Created by Ferrera on 26/03/2017.
 */

class BluetoothDevicesSingleton extends ArrayList<BluetoothDevice> {

    private static final BluetoothDevicesSingleton ourInstance = new BluetoothDevicesSingleton();

    static BluetoothDevicesSingleton getInstance() {
        return ourInstance;
    }

    private BluetoothDevicesSingleton() {
        this.add(new BluetoothDevice("Ceinture"));
        this.add(new BluetoothDevice("Montre"));
        this.add(new BluetoothDevice("Chaussures"));
    }
}
