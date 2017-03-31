package teamtreehouse.com.iamhere;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by alexandre on 23/03/17.
 */

public class BluetoothDeviceAdapter extends ArrayAdapter<BluetoothDevice> {


    public BluetoothDeviceAdapter(Context context, List<BluetoothDevice> devices) {
        super(context, 0, devices);
    }

    //convertView est notre vue recyclée
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        ItemViewHolder mainViewholder = null;

        //Android nous fournit un convertView null lorsqu'il nous demande de la créer
        //dans le cas contraire, cela veux dire qu'il nous fournit une vue recyclée
        if(convertView == null){
            //Nous récupérons notre row_tweet via un LayoutInflater,
            //qui va charger un layout xml dans un objet View
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent, false);
            ItemViewHolder viewHolder = new ItemViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.deleteButton = (Button) convertView.findViewById(R.id.button);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        }

        mainViewholder = (ItemViewHolder) convertView.getTag();

        mainViewholder.name.setText(getItem(position).getName());
        mainViewholder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothDevice thisDevice;
                thisDevice = BluetoothDeviceAdapter.super.getItem(position);
                BluetoothDeviceAdapter.super.remove(thisDevice);
            }
        });

        //nous renvoyons notre vue à l'adapter, afin qu'il l'affiche
        //et qu'il puisse la mettre à recycler lorsqu'elle sera sortie de l'écran
        return convertView;
    }

    private class ItemViewHolder {
        public TextView name;
        public Button deleteButton;
        public ImageView imageView;
    }

}
