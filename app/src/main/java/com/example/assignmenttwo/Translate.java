package com.example.assignmenttwo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Translate extends AppCompatActivity {

    private Button translateButton;
    private RequestQueue queue;
    private TextView userNameText;
    private Button logoutButton;

    private Spinner countriesSpinnerSource;
    private Spinner countriesSpinnerDestination;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translate_page);

        translateButton = findViewById(R.id.btnTranslate);
        userNameText = findViewById(R.id.userNameText);
        logoutButton = findViewById(R.id.logoutButton);
        countriesSpinnerSource = findViewById(R.id.countriesSpinnerSource);
        countriesSpinnerDestination = findViewById(R.id.countriesSpinnerDestination);

        try{
            SharedPreferences prefs = getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
            String currentUser = prefs.getString("CurrentUser", null);

            Log.d("DOES THIS EVEN WORK?!!!!!!!!", currentUser);

            Type listType = new TypeToken<ArrayList<String>>() {}.getType();
            ArrayList<String> loggedUser = new Gson().fromJson(currentUser, listType);


            String[] userDetails =  loggedUser.get(0).split("-");
            userNameText.setText(userDetails[0].trim());

        }catch (Exception e){
            Log.d("Error", "Error reading user details");
        }





        // the values for the countries are set in the arrays.xml in the resources
        // i did this because it does not make sense to create a request every time the client wants to choose a country
        // and they are only 195 countries that I got from the same api
        //--------------------------------------------------------------------------
//        the spinner can be populated with the api using this following commented code
         //String[] countries = getCountries(); // this function should return all the countries from the RESTFUl api
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//                this,
//            R.array.countries,
//                android.R.layout.simple_spinner_item
//        );

        //        adapter.setDropDownViewResource(android.R.layout.countriesSpinnerDestination);

//        adapter.setDropDownViewResource(android.R.layout.countriesSpinnerSource);

//    spinnerOptions.setAdapter(adapter);

        queue = Volley.newRequestQueue(this);

        Log.d("Test to see if it works", "This should work");

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.commit();


                Intent intent = new Intent(Translate.this, MainActivity.class);
                System.out.println(intent);
                startActivity(intent);
            }
        });

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sourceCountry = countriesSpinnerSource.getSelectedItem().toString();
                String destinationCountry = countriesSpinnerDestination.getSelectedItem().toString();

                String URL = "https://restcountries.com/v3.1/name/" + sourceCountry;
                String destURL = "https://restcountries.com/v3.1/name/" + destinationCountry;

                ArrayList<String> sourceCountryDetails = getCountryArray(URL);
                ArrayList<String> destinationCountryDetails = getCountryArray(destURL);

//                since Java does not have an await and async method i can use like in javascript i will be sleeping the program till the data is fetched

                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {// this should function as an await method just to make the code cleaner and robust
                    @Override
                    public void run() {
                        Log.d("Source Country Details", sourceCountryDetails.toString());
                        Log.d("Destination Country Details", destinationCountryDetails.toString());

                        //now we call the exchange

                        //i'm using a 2 week free key here so if it does not work right away you can create a new
                        //trial, use the key and it will work but hopefully we won't need that
                        String exchangeURL = "https://v6.exchangerate-api.com/v6/9609824abf77160cece4c6f9/latest/" + sourceCountryDetails.get(2);
                        String destinationCountryCurrency = destinationCountryDetails.get(2);

                        ArrayList<String> Rates = getExchangeRate(exchangeURL, destinationCountryCurrency);
                        //we're gonna do the same here so we can work with the data we got
                        //this is the second api i'm using

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {// this should function as an await method just to make the code cleaner and robust
                            @Override
                            public void run() {
                                Log.d("Exchange Rates", Rates.toString());
                                // now we render the code that generates an image for the countries we're travellig from and to
                                // then a text to put their
                                //probably better to put this in a new activity ngl

                                //solid so Rates has the conversion numbers, all we have to do is

                                //i'll add them to the sharedprefs, then take it to the other activity and deal with the data over there
                                SharedPreferences prefs = getSharedPreferences("ExchangeDetails", Context.MODE_PRIVATE);

                                ArrayList <String> ExchangeDetails = new ArrayList<String>();

                                ExchangeDetails.add(sourceCountryDetails.get(0));
                                ExchangeDetails.add(sourceCountryDetails.get(1));
                                ExchangeDetails.add(sourceCountryDetails.get(2));
                                ExchangeDetails.add(sourceCountryDetails.get(3));
                                ExchangeDetails.add(destinationCountryDetails.get(0));
                                ExchangeDetails.add(destinationCountryDetails.get(1));
                                ExchangeDetails.add(destinationCountryDetails.get(2));
                                ExchangeDetails.add(destinationCountryDetails.get(3));
                                ExchangeDetails.add(Rates.get(0));
                                ExchangeDetails.add(Rates.get(1));

                                SharedPreferences.Editor editor = prefs.edit();

                                Gson gson = new Gson();
                                String errands = gson.toJson(ExchangeDetails);
                                editor.putString("ExchangeDetails", errands);
                                editor.commit();

                                Intent intent = new Intent(Translate.this, ExchangeDetails.class);
                                System.out.println(intent);
                                startActivity(intent);

                            }
                        }, 2000);


                    }
                }, 2000);

            }
        });
    }
    public ArrayList<String> getCountryArray(String input) {
        ArrayList<String> countryDetails = new ArrayList<>();

        try {
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, input,
                    null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    try {
                        JSONObject obj = response.getJSONObject(0);

                        String nameDetails = obj.getString("name");
                        String flagDetails = obj.getString("flags");
                        String currencyDetails = obj.getString("currencies");

                        JSONObject nameObject = new JSONObject(nameDetails);
                        JSONObject flagObject = new JSONObject(flagDetails);
                        JSONObject currencyObject = new JSONObject(currencyDetails);


                        String commonName = nameObject.getString("common");
                        String flagPng = flagObject.getString("png");
                        String mainCurrency = currencyObject.keys().next(); //gets the first currency found in the api, not very accurate but it will do for the time being

                        JSONObject mainCurrencyObject = currencyObject.getJSONObject(mainCurrency);
                        String symbol = mainCurrencyObject.getString("symbol");

                        //we will use this variable to call the exchange api

                        if(commonName.equals("Palestine")){//for some reason it gives egyptian pound as the
                            //first currency for palestine so i have to hard code it like this
                            mainCurrency = "ILS";
                            symbol = "₪";
                        }

                        countryDetails.add(flagPng);
                        countryDetails.add(commonName);
                        countryDetails.add(mainCurrency);
                        countryDetails.add(symbol);


                        Log.d("countryDetails", countryDetails.toString());

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    Log.d("response", response.toString());

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("volley_error", error.toString());
                }
            });


            queue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return countryDetails;
    }


    public ArrayList<String> getExchangeRate(String input, String destCurr) {
        ArrayList<String> rates = new ArrayList<>();
        Log.d("DOES IT EVEN DO THIS?", "HELLO!");


        try {
                JsonObjectRequest request = new JsonObjectRequest
                        (Request.Method.GET, input, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject obj = response;

                                    Log.d("DOES IT EVEN DO THIS?", response.toString());

                                    String nameDetails = obj.getString("conversion_rates");

                                    JSONObject nameObject = new JSONObject(nameDetails);
                                    String commonName = nameObject.getString(destCurr);

                                    rates.add("1");
                                    rates.add(commonName);

                                    Log.d("exchangeRates", rates.toString());

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                Log.d("response", response.toString());

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("There was an error", "Yes, there was...");

                            }
                        });

                queue.add(request);

            }catch (Exception e){
                Log.d("There was an error", "Yes, there was...");
            }

            return rates;
    }

}
