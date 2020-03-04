package eatec.cookery;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Jurdun-PC on 18/02/2020.
 */

class userList extends ArrayAdapter<user> {
    private Activity context;
    private List<user> users;

    public userList(Activity context, List<user> users){
        super(context, R.layout.fragment_row,users);
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Init inflater
        LayoutInflater inflater = context.getLayoutInflater();
        //Row
        View listViewItem = inflater.inflate(R.layout.fragment_user_row,null,true);
        //Row objects
        TextView titleText = (TextView) listViewItem.findViewById(R.id.titleText);
        TextView cookeryRank = (TextView) listViewItem.findViewById(R.id.cookeryRankText);
        TextView userID = (TextView) listViewItem.findViewById(R.id.userIDTV);
        ImageView userPP = listViewItem.findViewById(R.id.rowImage);//TODO change
        //init user
        user user = users.get(position);
        //Set Data
        Picasso.get()
                .load(user.getProfilePicture())
                .placeholder(R.drawable.user)
                .into(userPP);
        titleText.setText(user.getUserName());
        cookeryRank.setText(user.convertCookeryRank());
        userID.setText(user.getUserID());


        return listViewItem;
    }
}
