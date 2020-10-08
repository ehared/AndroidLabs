package com.example.androidlabs;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences prefs = null;
    private static final String USERDATA = "User Data"; /* filename of the shared preferences file */
    private static final String EMAIL = "Email"; /* email key */
    private static final String ACTIVITY_NAME= "MAIN_ACTIVITY";
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_linear);
        Log.i(ACTIVITY_NAME, "In function: OnCreate()");

        final TextView emailTitle = findViewById(R.id.emailHeading);
        final TextView passTitle = findViewById(R.id.passHeading);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        Button login = findViewById(R.id.lgnButton); /* click here button */

        prefs = getSharedPreferences(USERDATA, Context.MODE_PRIVATE);
        String savedEmail = prefs.getString(EMAIL,"");
        email.setText(savedEmail);

        Intent goToPage = new Intent(MainActivity.this, ProfileActivity.class);

        login.setOnClickListener(btn ->  {

            Log.i("Main Activity", "login button clicked");
            saveData(email.getText().toString());
            goToPage.putExtra(EMAIL, email.getText().toString()); /* store email in the email field to be sent to the profile activity */
            startActivity(goToPage); /* start profile activity */
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(ACTIVITY_NAME, "In function: OnActivityResult()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In function: OnResume()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In function: OnStop()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In function: onPause()");
        this.saveData(this.email.getText().toString()); /* call save function to save the email in editText */

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In function: OnDestroy()");
    }

    /**
     * This method saves the user's email into the shared preferences file
     * @param emailToSave - the email to be saved
     */
    public void saveData(String emailToSave) {
        /* save email to shared prefs file */
        Log.i(ACTIVITY_NAME, "In function: saveData");
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(EMAIL, emailToSave);
        editor.commit();
    }
}