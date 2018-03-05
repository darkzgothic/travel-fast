package com.example.zahidhasan.travelfast.model;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.zahidhasan.travelfast.R;

import java.util.List;

/**
 * Created by DarkzGothic on 7/6/2017.
 */

public class RideList extends ArrayAdapter<RideDetails> {

    private Activity context;
    private List<RideDetails> rideList;

    public RideList(Activity context, List<RideDetails> rideList){
        super(context, R.layout.ride_list_layout, rideList);
        this.context = context;
        this.rideList = rideList;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.ride_list_layout, null, true);

        TextView phnNum = (TextView) listViewItem.findViewById(R.id.phn_num_txtvw);
        TextView pickUpAdd = (TextView) listViewItem.findViewById(R.id.pickup_add_txtvw);
        TextView dropAdd = (TextView) listViewItem.findViewById(R.id.drop_add_txtvw);
        TextView time = (TextView) listViewItem.findViewById(R.id.time_txtvw);
        TextView distance = (TextView) listViewItem.findViewById(R.id.distance_txtvw);
        TextView cost = (TextView) listViewItem.findViewById(R.id.cost_txtvw);

        RideDetails ride = rideList.get(position);

        phnNum.setText(ride.getPassengerPhn());
        pickUpAdd.setText(ride.getPassengerSourceAdd());
        dropAdd.setText(ride.getPassengerDesAdd());
        time.setText(ride.getTime() + " Minutes");
        distance.setText(ride.getDistance() + " Km");
        cost.setText(ride.getCost() + " BDT");

        return listViewItem;
    }
}
