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
    private Button mWeatherBtn;
    private Button mToolbarBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.e(ACTIVITY_NAME, "In function: onCreate()");

        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        mImageButton = findViewById(R.id.profileBtn);
        mChatBtn = findViewById(R.id.chatBtn);
        mWeatherBtn = findViewById(R.id.weatherBtn);
        mToolbarBtn = findViewById(R.id.toolbarBtn);


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

        mWeatherBtn.setOnClickListener(click -> {
            Log.i(ACTIVITY_NAME, "starting weather forecast activity");
            Intent goToWeather = new Intent (ProfileActivity.this, WeatherForecast.class);
            startActivity(goToWeather);
        });

        mToolbarBtn.setOnClickListener(click-> {
            Log.i(ACTIVITY_NAME, "starting test toolbar activity");
            Intent goToToolber = new Intent(ProfileActivity.this, TestToolbar.class);
            startActivityForResult(goToToolber, 100);
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

        if(resultCode == 500){
            finish();
        }
        Log.e(ACTIVITY_NAME, "In function: onActivityResult()");
    }
}