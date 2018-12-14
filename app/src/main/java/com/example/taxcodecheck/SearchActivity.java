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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup;

import java.io.IOException;
import java.io.InputStream;

import static com.example.taxcodecheck.LoginActivity.authorization;
import static com.example.taxcodecheck.LoginActivity.usernameString;

import static com.example.taxcodecheck.LoginActivity.isLoggedin;

public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public taxcodes[] codes;
    public String[] descArray;
    public String[] taxCodeArray;
    public TextView taxView;
    public static String taxResult;
    public EditText zipInput;
    public String taxCodeBoi;
    public String zipCodeBoi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        codes = loadJSONFromAsset(this);
        taxView = findViewById(R.id.taxRate);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //passes user login info into the navigation bar
        if (isLoggedin) {
            getPref();
        }

        descArray = new String[codes.length];
        for (int i = 0; i < codes.length; i++) {
            descArray[i] = codes[i].description;
        }

        taxCodeArray = new String[codes.length];
        for (int i = 0; i < codes.length; i++) {
            taxCodeArray[i] = codes[i].taxCode;
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, descArray);
        AutoCompleteTextView textView =
                findViewById(R.id.filterInput);
        textView.setAdapter(adapter);

        Button search = findViewById(R.id.searchButton);
        zipInput = findViewById(R.id.zipInput);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taxCodeBoi = grabTaxCode();
                System.out.println(taxCodeBoi);
                zipCodeBoi = zipInput.getText().toString();
                System.out.println(zipCodeBoi);
                searchTaxCode(taxCodeBoi, zipCodeBoi);
            }
        });

        zipInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    taxCodeBoi = grabTaxCode();
                    zipCodeBoi = zipInput.getText().toString();
                    searchTaxCode(taxCodeBoi, zipCodeBoi);
                    return true;
                }
                return false;
            }
        });
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
    public void makeToast(String toastString) {
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

        } else if (id == R.id.search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public taxcodes[] loadJSONFromAsset(Context context) {
        String json = null;
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

    public String grabTaxCode() {
        AutoCompleteTextView searchInput = findViewById(R.id.filterInput);
        String searchParam = searchInput.getText().toString();
        String result = "";

        for (int i = 0; i < codes.length; i++) {
            if (searchParam.equals(descArray[i])) {
                result = taxCodeArray[i];
                return result;
            }
        }
        return result;
    }

    public void searchTaxCode(String taxCode, String zip) {
        // Instantiate the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://ava-tax-springmvc.herokuapp.com/transaction?taxcode=" + taxCode + "&zip=" + zip + "&username=" + usernameString + "&password=" + authorization ;
        Log.d("URL PASS", url);

        //Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string
                        Log.d("RESPONSE: ", response);
                        taxResult = response.toString();
                        renderTax(taxResult);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.d("ERROR: ", error.getMessage());
                makeToast("AvaTax server unable to reach data services");
                makeToast("Also known as, gremlins ate your homework");
            }
        });

        // Add the request to the RequestQueue
        queue.add(stringRequest);
    }

    //shows tax result in the search view
    public void renderTax(String tax) {
        taxView.setText(tax);
    }
}