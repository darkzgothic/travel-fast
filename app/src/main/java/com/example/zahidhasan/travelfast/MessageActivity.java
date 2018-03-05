package com.example.zahidhasan.travelfast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zahidhasan.travelfast.model.MessageList;
import com.example.zahidhasan.travelfast.model.Messages;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private ListView listView;
    private List<Messages> messageList;
    private List<Messages> mList;

    private DatabaseReference refMessages;
    private DatabaseReference refMsg;

    private EditText editText;
    private Button button, backToMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        messageList = new ArrayList<>();
        mList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.messageListView);

        editText = (EditText) findViewById(R.id.messageEditText);
        button = (Button) findViewById(R.id.sendButton);
        backToMap = (Button) findViewById(R.id.backToMap);

        refMessages = FirebaseDatabase.getInstance().getReference("Messages");
        refMsg = refMessages.child("01673144088_anto@gmail");

        refMsg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageList.clear();
                mList.clear();

                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    Messages msg = data.getValue(Messages.class);
                    messageList.add(msg);
                }

                for (int i = messageList.size()-1; i >= 0; i -- ){
                    Messages msg = new Messages(messageList.get(i).getId(), messageList.get(i).getMsg());
                    mList.add(msg);
                }

                final MessageList messageListAdapter = new MessageList(MessageActivity.this, mList);
                listView.setAdapter(messageListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str = editText.getText().toString().trim();
                String message = "Driver:- " + str;
                if (!TextUtils.isEmpty(str)){
                    String id = refMsg.push().getKey();
                    Messages msg = new Messages(id, message);
                    refMsg.child(id).setValue(msg);
                    Toast.makeText(MessageActivity.this, "Send", Toast.LENGTH_SHORT).show();
                    editText.setText("");
                }
            }
        });

        backToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageActivity.this.finish();
            }
        });
    }
}
