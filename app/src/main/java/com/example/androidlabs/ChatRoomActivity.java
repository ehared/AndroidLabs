package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    private myListAdapter mAdapter;
    private ArrayList<Message> list = new ArrayList();
    private Button sendBtn;
    private Button receiveBtn;
    private EditText msgField;
    private SQLiteDatabase db;
    protected final static String ACTIVITY_NAME = "ChatRoomActivity";
    private Cursor results;
    private boolean isTablet;
    private DetailsFragment dFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        sendBtn = (Button) findViewById(R.id.sendBtn);
        receiveBtn = (Button) findViewById(R.id.receiveBtn);
        msgField = (EditText) findViewById(R.id.messageBox);
        isTablet = findViewById(R.id.fragmentLoc) != null; // check if frameLayout is loaded
        ListView chatList = (ListView) findViewById(R.id.chatList);
        loadDatabase();
        chatList.setAdapter(mAdapter = new myListAdapter());

        /* send button listener */
        sendBtn.setOnClickListener(click -> {

            createMessage(true);
        });

        /* receive btn listener */
        receiveBtn.setOnClickListener(click -> {

            createMessage(false);
        });

        /* list listener */
        chatList.setOnItemClickListener((parent, view, position, id) -> {

            Bundle bundle = new Bundle();
            bundle.putString("item", list.get(position).getMsg());
            bundle.putInt("position", position);
            bundle.putLong("id", id);
            bundle.putBoolean("isSent", list.get(position).getMessageType());

            if (isTablet) {
                dFragment = new DetailsFragment();
                bundle.putBoolean("isTablet", true);
                dFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLoc, dFragment).commit();
            } else { // phone
                Intent emptyActivity = new Intent(ChatRoomActivity.this, EmptyActivity.class);
                bundle.putBoolean("isTablet", false);
                emptyActivity.putExtras(bundle);
                startActivity(emptyActivity);

            }
        });

        chatList.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Do you want to delete this?").setMessage("The selected row is : " + position + "\nThe database id is: " + id)
                    .setPositiveButton("Yes", (click, args) -> {
                        deleteMessage(mAdapter.getItem(position));
                        list.remove(position);
                        if (isTablet && dFragment != null) {

                            getSupportFragmentManager().beginTransaction().remove(dFragment).commit();
                        }
                        mAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", (click, args) -> {

                    }).create().show();
            return true;
        });
    }

    protected void createMessage(boolean messageType) {
        int type = 0;

        if (!messageType)
            type = 1;

        String message = msgField.getText().toString();
        ContentValues cValues = new ContentValues();
        cValues.put(DatabaseOpener.COL_MESSAGE, message);
        cValues.put(DatabaseOpener.COL_IS_SENT, type);

        // grab id
        long id = db.insert(DatabaseOpener.TABLE_NAME, null, cValues);
        Message msg = new Message(message, messageType, id);

        Log.d(ACTIVITY_NAME, "new msg added ID: " + id + " message: " + message + " isSent: " + type);

        list.add(msg);
        mAdapter.notifyDataSetChanged();
        msgField.setText("");
    }

    private void loadDatabase() {

        //Open Database
        DatabaseOpener dbOpener = new DatabaseOpener(this);
        db = dbOpener.getWritableDatabase();

        String[] columns = {DatabaseOpener.COL_ID, DatabaseOpener.COL_MESSAGE, DatabaseOpener.COL_IS_SENT};
        Cursor results = db.query(false, DatabaseOpener.TABLE_NAME, columns, null, null, null, null, null, null);
        int idIndex = results.getColumnIndex(DatabaseOpener.COL_ID);
        int msgIndex = results.getColumnIndex(DatabaseOpener.COL_MESSAGE);
        int isSentIndex = results.getColumnIndex(DatabaseOpener.COL_IS_SENT);

        while (results.moveToNext()) {
            Long id = results.getLong(idIndex);
            String message = results.getString(msgIndex);
            boolean type = false;

            if (results.getInt(isSentIndex) == 0)
                type = true;
            printCursor(results, db.getVersion());
            list.add(new Message(message, type, id));

        }

    }

    protected void deleteMessage(Message m) {
        db.delete(DatabaseOpener.TABLE_NAME, DatabaseOpener.COL_ID + "= ?", new String[]{Long.toString(m.getId())});
        Log.d(ACTIVITY_NAME, "message deleted ID: " + m.getId() + " message: " + m.getMsg() + " isSent: " + m.getMessageType());
    }

    public void printCursor(Cursor c, int version) {
        int idIndex = c.getColumnIndex(DatabaseOpener.COL_ID);
        int msgIndex = c.getColumnIndex(DatabaseOpener.COL_MESSAGE);
        int isSentIndex = c.getColumnIndex(DatabaseOpener.COL_IS_SENT);

        Log.d(ACTIVITY_NAME, "version: " + version + " Column count: " + c.getColumnCount() + " Row Count: " + c.getCount());
        Log.d(ACTIVITY_NAME, c.getColumnName(idIndex) + ": " + c.getLong(idIndex) + " "
                + c.getColumnName(msgIndex) + ": " + c.getString(msgIndex) + " "
                + c.getColumnName(isSentIndex) + ": " + c.getInt(isSentIndex));

    }

    private class myListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Message getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            Message message = getItem(position);
            View newView;

            /* if send message, uses send layout , else receive layout */
            if (message.getMessageType()) {
                newView = inflater.inflate(R.layout.chat_send, parent, false);
            } else {
                newView = inflater.inflate(R.layout.chat_receive, parent, false);
            }

            TextView textMsg = (TextView) newView.findViewById(R.id.message);
            textMsg.setText(message.getMsg());

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
