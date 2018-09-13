package com.example.taxcodecheck;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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

            //search button to communicate with Server - incomplete
            //Sooz
//            final Button search = findViewById(R.id.searchButton);
//            search.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    search(isLoggedin);
//                }
//            });
//
//            makeToast("Query sent to AvaTax, " + usernameString);

    }

      //shell of search method by Sooz with toast message for users when query sent
//    public void search (boolean isLoggedin, String itemCode, String postalCode){
//        if (!isLoggedin){
//            makeToast("Whoops, looks like you need to login again");
//        } else {
//            RequestQueue queue = Volley.newRequestQueue(this);
//
//            try {
//                itemCode = URLEncoder.encode(itemCode, "UTF-8");
//                postalCode = URLEncoder.encode(postalCode, "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//
//            //String URL currently incorrect and incomplete
//            String url = "https://avatax-server.herokuapp.com/" + itemCode + postalCode;
//            Log.d("URL PASS", url);
//
//            //request response and parse response to add value to page
//
//
//        }
//    }

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

    //generate login message to users
    public void makeToast(String toastString){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView text = layout.findViewById(R.id.text);
        text.setText(toastString);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, -400);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
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
}
