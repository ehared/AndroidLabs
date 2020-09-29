 package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

 public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_linear);

        final TextView look_gui = findViewById(R.id.look_at_gui); /* text at the top of the UI */
        Button clickMeBtn = findViewById(R.id.click_here); /* click here button */
        CheckBox checkHere = findViewById(R.id.check_out); /* check box next to click here button */
        ImageButton flagBtn = findViewById(R.id.flag); /* flag button */
        Switch onOffSwitch = findViewById(R.id.switchOnOff); /* on and off switch */
        EditText editText = findViewById(R.id.editBox); /* text field at the bottom of the UI */


        clickMeBtn.setOnClickListener( (v) -> { /* toast message message will pop up once button is clicked */
            Toast.makeText(this, getResources().getString(R.string.toast_message),Toast.LENGTH_LONG).show();} );

        onOffSwitch.setOnCheckedChangeListener((boxClicked, isClicked) -> {
            String status;
            if(isClicked)
                status = "on";
            else
                status = "off";
            Snackbar.make(look_gui, getResources().getString(R.string.snap_message) + " " + status, Snackbar.LENGTH_LONG).setAction( "Undo", (v) -> boxClicked.setChecked(!isClicked) ).show();
        });
    }
}