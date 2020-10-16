package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    private myListAdapter mAdapter;
    private ArrayList <Message> list = new ArrayList();
    private Button sendBtn;
    private Button receiveBtn;
    private EditText msgField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        sendBtn = (Button) findViewById(R.id.sendBtn);
        receiveBtn = (Button) findViewById(R.id.receiveBtn);
        msgField = (EditText) findViewById(R.id.messageBox);

        ListView chatList = (ListView) findViewById(R.id.chatList);
        chatList.setAdapter(mAdapter = new myListAdapter());

        /* send button listener */
        sendBtn.setOnClickListener( click -> {
            Message msg = new Message(msgField.getText().toString(), "SEND");
            list.add(msg);
            mAdapter.notifyDataSetChanged();
            msgField.setText("");
        });

        /* receive btn listener */
        receiveBtn.setOnClickListener( click -> {
            Message msg = new Message(msgField.getText().toString(), "RECEIVE");
            list.add(msg);
            mAdapter.notifyDataSetChanged();
            msgField.setText("");
        });

        /* list listener */
        chatList.setOnItemClickListener((parent, view, position, id) -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Do you want to delete this?").setMessage("The selected row is : " + position + "\nThe database id is: " + id)
                    .setPositiveButton("Yes", (click, args) -> {
                        list.remove(position);
                        mAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", (click, args) -> {

                    }).create().show();
        });
    }


    private class myListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            Message message = (Message)getItem(position);
            View newView;

            /* if send message, uses send layout , else receive layout */
            if(message.getMessageType().equalsIgnoreCase("send")){
                newView = inflater.inflate(R.layout.chat_send, parent, false);
                TextView textMsg = (TextView)newView.findViewById(R.id.message);
                textMsg.setText(message.getMsg());
                
            }
            else{
                newView = inflater.inflate(R.layout.chat_receive, parent, false);
                TextView textMsg = (TextView)newView.findViewById(R.id.message);
                textMsg.setText(message.getMsg());
            }
            return newView;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
