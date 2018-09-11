package com.example.taxcodecheck;

import android.content.Context;
import android.content.Intent;
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

import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class LoginActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public Boolean isLoggedin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Button login = findViewById(R.id.loginButton);
        final EditText mUsername = findViewById(R.id.username);
        final EditText mPassword = findViewById(R.id.passwordInput);

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login(mUsername.getText().toString(), mPassword.getText().toString());
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        } else if (id == R.id.about) {

        } else if(id == R.id.search){

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void login(String username, String password){

        if (username == null || password == null) {
            Context context = getApplicationContext();
            CharSequence text = "Please enter a username and password!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);

            try {
                username = URLEncoder.encode(username, "UTF-8");
                password = URLEncoder.encode(password, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String url = "https://avatax-server.herokuapp.com/auth?username=" + username + "&password=" + password;
            System.out.println(url);
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("response: " + response.toString());
                            Boolean isLoggedin = Boolean.parseBoolean(response.toString());
                            // Display the first 500 characters of the response string.
                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;
                            System.out.println(response.toString());
                            Toast toast = Toast.makeText(context, response.toString(), duration);
                            toast.show();
                            System.out.println(isLoggedin);

                            if(isLoggedin == true) {
                                goToSearch();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("error: " + error.getMessage());

                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    System.out.println(error.getMessage());
                    Toast toast = Toast.makeText(context, error.getMessage(), duration);
                    toast.show();
                }
            });

// Add the request to the RequestQueue.
            queue.add(stringRequest);


        }

    }

    public void goToSearch(){
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}
