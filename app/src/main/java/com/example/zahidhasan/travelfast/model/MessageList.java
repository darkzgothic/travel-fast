package com.example.zahidhasan.travelfast.model;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.zahidhasan.travelfast.R;

import java.util.List;

/**
 * Created by DarkzGothic on 6/15/2017.
 */

public class MessageList extends ArrayAdapter<Messages> {

    private Activity context;
    private List<Messages> msgList;

    public MessageList(Activity context, List<Messages> msgList){
        super(context, R.layout.msg_list_layout, msgList);
        this.context = context;
        this.msgList = msgList;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.msg_list_layout, null, true);

        TextView textViewMessage = (TextView) listViewItem.findViewById(R.id.textViewMessage);

        Messages msgs = msgList.get(position);

        if (msgs.getMsg().contains("Driver:- ")){
            String str = msgs.getMsg().replace("Driver:- ", "");
            if (str.contains("\n")){
                textViewMessage.setTextColor(Color.MAGENTA);
                textViewMessage.setText(str.replace("\n", " "));
            }else {
                textViewMessage.setTextColor(Color.MAGENTA);
                textViewMessage.setText(str);
            }
        }
        else {
            String str = msgs.getMsg().replace("Passenger:- ", "");
            if (str.contains("\n")){
                textViewMessage.setTextColor(Color.BLUE);
                textViewMessage.setText(str.replace("\n", " "));
                textViewMessage.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            }else {
                textViewMessage.setTextColor(Color.BLUE);
                textViewMessage.setText(str);
                textViewMessage.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            }
        }

        return listViewItem;
    }

}

