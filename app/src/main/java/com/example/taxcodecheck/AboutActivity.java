package com.example.taxcodecheck;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.taxcodecheck.LoginActivity.isLoggedin;
import static com.example.taxcodecheck.LoginActivity.usernameString;

public class AboutActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //generates all the developer profile buttons with links
        makeButtons();

        //passes user login info into the navigation bar
        if (isLoggedin) {
            getPref();
        }
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

    public void makeButtons(){
        // Link to each developer's external Github and LinkedIn profiles
        final Button githubSooz = findViewById(R.id.githubSooz);
        githubSooz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url = getString(R.string.sooz_github);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        final Button linkedinSooz = findViewById(R.id.linkedinSooz);
        linkedinSooz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url = getString(R.string.sooz_linkedin);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        final Button githubAhmed = findViewById(R.id.githubAhmed);
        githubAhmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url = getString(R.string.ahmed_github);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        final Button linkedinAhmed = findViewById(R.id.linkedinAhmed);
        linkedinAhmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url = getString(R.string.ahmed_linkedin);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        final Button githubTyler = findViewById(R.id.githubTyler);
        githubTyler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url = getString(R.string.tyler_github);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        final Button linkedinTyler = findViewById(R.id.linkedinTyler);
        linkedinTyler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url = getString(R.string.tyler_linkedin);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        final Button githubTara = findViewById(R.id.githubTara);
        githubTara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url = getString(R.string.tara_github);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        final Button linkedinTara = findViewById(R.id.linkedinTara);
        linkedinTara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url = getString(R.string.tara_linkedin);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
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
        getMenuInflater().inflate(R.menu.about, menu);
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

        }
        //conditional statement that updates values for shared preferences to
        //change user login status and update username values and pass back to
        //login activity page
        else if (id == R.id.logout) {
            isLoggedin = false;
            usernameString = "Not logged in";
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("authStatus", isLoggedin)
                    .putExtra("username", usernameString);
            startActivity(intent);

        } else if (id == R.id.about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);

        //Added conditional when clicking search for logged in vs not logged in
        //needs to route back to login if user isn't logged in
        } else if(id == R.id.search && isLoggedin == true){
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);

        } else if(id == R.id.search && isLoggedin == false){
            Toast.makeText(this, "Please login", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
