package com.example.taxcodecheck;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class LoginActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static boolean isLoggedin = false;
    public static String usernameString = "Not logged in";
    public static String authorization = null;


    public static final String PREF_USERNAME = "userString";
    public static final String PREF_AUTH_STATUS = "authStatus";
    public static final String PREF_PASSWORD = "password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //conditional to prompt users to login on page, if not already logged in
        if(!isLoggedin){
            makeToast("Please login for \ntax code checks");
        }

        final Button login = findViewById(R.id.loginButton);
        final EditText mUsername = findViewById(R.id.username);
        final EditText mPassword = findViewById(R.id.passwordInput);

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login(mUsername.getText().toString(), mPassword.getText().toString());
            }
        });

        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    login(mUsername.getText().toString(), mPassword.getText().toString());
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mPassword.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    return true;
                }
                return false;
            }
        });

        //passes user login info into the navigation bar
        if (isLoggedin) {
            getPref();
        }
    }

    //sets user login status and username values to be shared across application
    public void setPref() {
        Context ctx = getApplicationContext();
        PreferenceManager.getDefaultSharedPreferences(ctx)
                .edit()
                .putString(PREF_USERNAME,usernameString)
                .putBoolean(PREF_AUTH_STATUS, isLoggedin)
                .putString(PREF_PASSWORD, authorization)
                .apply();
    }

    //gets saved user login string from Login page and share to this activity page
    //to update the nav bar with login string
    private void getPref() {
        Context ctx = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        String prefVal = prefs.getString(LoginActivity.PREF_USERNAME, usernameString);
        boolean prefAuth = prefs.getBoolean(LoginActivity.PREF_AUTH_STATUS, isLoggedin);
        String prefPwd = prefs.getString(LoginActivity.PREF_PASSWORD, authorization);
        Log.d("PREF VALUE", prefVal);
        Log.d("PREF AUTH", String.valueOf(prefAuth));
        Log.d("PREF PWD", prefPwd);

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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            makeToast("Logged out");
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
            makeToast("Please login to use search");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //method that contacts our server to connect to AvaTax API for
    //user authentication
    //updates username value via SharedPreferences
    public void login(String username, String password){
        //set values to be passed through Shared Preferences
        usernameString = username;
        authorization = password;

        if (username == null || password == null) {

            makeToast("Please enter username and password \nto get started");

        } else {

            //setting variable for set Shared Preferences, if user login turns out to be valid
            usernameString = username;

            // Instantiate the RequestQueue
            RequestQueue queue = Volley.newRequestQueue(this);

            try {
                username = URLEncoder.encode(username, "UTF-8");
                password = URLEncoder.encode(password, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String url = "https://avatax-server.herokuapp.com/auth?username=" + username + "&password=" + password;
            Log.d("URL PASS", url);

            //Request a string response from the provided URL
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string
                        Log.d("RESPONSE: ", response);
                        boolean authStatus = Boolean.parseBoolean(response.toString());

                        if(authStatus){

                            //capture the username to share with all activities through the application
                            isLoggedin = authStatus;
                            setPref();

                            makeToast("You are now logged in \nGoing to search now");
                            Log.d("AUTH STATUS", String.valueOf(isLoggedin));


                            //gets saved user login string from Login page and share to this activity page
                            //to update the nav bar with login string
                            NavigationView navigationView = findViewById(R.id.nav_view);
                            View headerView = navigationView.getHeaderView(0);
                            TextView navUsername = headerView.findViewById(R.id.textView);

                            //confirm that username is being passed back to be assigned to text view
                            Log.d("TEXT", navUsername.getText().toString());

                            navUsername.setText("Logged in as: " + usernameString);
                            navigationView.getMenu().findItem(R.id.login).setVisible(false);
                            navigationView.getMenu().findItem(R.id.logout).setVisible(true);

                            goToSearch();

                        } else {

                            //make sure shared pref reset to not be logged in
                            isLoggedin = false;
                            usernameString = "Not logged in";
                            authorization = null;
                            setPref();

                            NavigationView navigationView = findViewById(R.id.nav_view);
                            makeToast("Login incorrect\nPlease try again");
                            navigationView.getMenu().findItem(R.id.login).setVisible(true);
                            navigationView.getMenu().findItem(R.id.logout).setVisible(false);
                        }
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            // Add the request to the RequestQueue
            queue.add(stringRequest);
        }
    }

    public void goToSearch(){
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}
