package com.example.taxcodecheck;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;

import static com.example.taxcodecheck.LoginActivity.usernameString;

import static com.example.taxcodecheck.LoginActivity.isLoggedin;

public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //passes user login info into the navigation bar
        if (isLoggedin) {
            getPref();
        }

        taxcodes[] codes = loadJSONFromAsset(this);

    }

    //gets saved user login string from Login page and share to this activity page
    //to update the nav bar with login string
    private void getPref() {
        Context ctx = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        String prefVal = prefs.getString(LoginActivity.PREF_USERNAME, usernameString);
        boolean prefAuth = prefs.getBoolean(LoginActivity.PREF_AUTH_STATUS, isLoggedin);
        Log.d("PREF VALUE", prefVal);
        Log.d("PREF AUTH", String.valueOf(prefAuth));

        //conditional so that if user isn't logged in and sees about view
        //correctly sees "not logged in"
        if (prefAuth) {
            NavigationView navigationView = findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);
            TextView navUsername = headerView.findViewById(R.id.textView);
            Log.d("TEXT", navUsername.getText().toString());
            navUsername.setText("Logged in as: " + usernameString);
            navigationView.getMenu().findItem(R.id.login).setVisible(false);
            navigationView.getMenu().findItem(R.id.logout).setVisible(true);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.login) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        } else if (id == R.id.logout) {
            isLoggedin = false;
            usernameString = "Not logged in";
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("authStatus", isLoggedin)
                    .putExtra("username", usernameString);
            startActivity(intent);

        } else if (id == R.id.about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);

        } else if(id == R.id.search){
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public taxcodes[] loadJSONFromAsset(Context context) {
        String json = null;
        taxcodes[] codes;
        try {
            InputStream is = context.getAssets().open("taxcodes.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            Gson gson = new Gson();
            json = new String(buffer, "UTF-8");
            JsonObject obj = gson.fromJson(json, JsonObject.class);
            JsonArray value = obj.getAsJsonArray("value");
            codes = gson.fromJson(value, taxcodes[].class);
            Log.d("CODES", "" + codes.length);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return codes;

    }

}
