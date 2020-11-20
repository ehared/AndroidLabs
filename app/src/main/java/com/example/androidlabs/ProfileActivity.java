package com.example.androidlabs;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";
    private TextView nameHead;
    private TextView emailHead;
    private EditText email;
    private EditText name;
    private ImageButton mImageButton;
    private static int REQUEST_IMAGE_CAPTURE = 1;
    private Button mChatBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.e(ACTIVITY_NAME, "In function: onCreate()");

        email = (EditText) findViewById(R.id.email);
        name = (EditText) findViewById(R.id.name);
        mImageButton = (ImageButton) findViewById(R.id.profileBtn);
        mChatBtn = (Button) findViewById(R.id.chatBtn);

        Intent fromMain = getIntent();
        String userEmail = fromMain.getStringExtra("Email"); /* grab email passed from main activity */
        email.setText(userEmail); /* display email address */

        mImageButton.setOnClickListener(click -> {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

        });

        mChatBtn.setOnClickListener(click -> {
            Log.i(ACTIVITY_NAME, "starting chat activity");
            Intent goToChat = new Intent (ProfileActivity.this, ChatRoomActivity.class);
            startActivity(goToChat);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(ACTIVITY_NAME, "In function: onStart()");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(ACTIVITY_NAME, "In function: onPause()");
    }

    @Override
    protected void onResume() {

        super.onResume();
        Log.e(ACTIVITY_NAME, "In function: onResume()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME, "In function: onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME, "In function: onDestroy()");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
            Log.i(ACTIVITY_NAME, "In function onActivityResult: successful upload");
        }
        Log.e(ACTIVITY_NAME, "In function: onActivityResult()");
    }
}