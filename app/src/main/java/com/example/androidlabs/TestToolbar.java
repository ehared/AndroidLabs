package com.example.androidlabs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.drm.DrmStore;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;


public class TestToolbar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        toolbar = (Toolbar)findViewById(R.id.toolbar); // gets the toolbar from layout
        setSupportActionBar(toolbar); // loads the toolbar, and calls OnCreateOptionsMenu

        // Navigation Drawer:
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // Inflate the menu items for use in the action bar

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actions, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        String msg = null;

        switch(item.getItemId()){
            case R.id.msg_item:
                msg = "You clicked on mail item";
                break;
            case R.id.display_item:
                msg = "You clicked on display item";
                break;
            case R.id.contacts_item:
                msg="You clicked on contacts item";
                break;
            case  R.id.settings_item:
                msg = "You clicked on settings item";
                break;
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item){

        String msg = "null";

        switch(item.getItemId()){
            case R.id.chat_item:
               Intent goChatPage = new Intent(TestToolbar.this, ChatRoomActivity.class);
               startActivity(goChatPage);
                break;
            case R.id.weather_item:
                Intent goWeatherPage = new Intent(TestToolbar.this, WeatherForecast.class);
                startActivity(goWeatherPage);
                break;
            case R.id.goLogin_item:
                Intent toProfile = new Intent();
                setResult(500, toProfile);
                finish();
                break;

        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer((GravityCompat.START));

        return true;
    }
}