package teamtreehouse.com.iamhere;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Ferrera on 27/03/2017.
 */

public class GroupeAdapter extends ArrayAdapter<String> {

    public GroupeAdapter(@NonNull Context context, @NonNull Membres membres) {
        super(context, 0, membres);
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

        mainViewholder.name.setText(getItem(position));
        mainViewholder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String thisMember;
                thisMember = GroupeAdapter.super.getItem(position);
                GroupeAdapter.super.remove(thisMember);
                UltraTeamApplication.getInstance().remove_someone(thisMember);
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
