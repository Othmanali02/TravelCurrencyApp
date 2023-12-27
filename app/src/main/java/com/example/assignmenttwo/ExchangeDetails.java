package com.example.assignmenttwo;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ExchangeDetails extends AppCompatActivity {

    private ImageView sourceFlag;
    private ImageView destFlag;
    private TextView sourceCurrNumber;
    private TextView sourceCurrSymbol;
    private TextView destCurrNumber;
    private TextView destCurrSymbol;
    private TextView sourceCountryName;

    private TextView destCountryName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_details);

        SharedPreferences prefs = getSharedPreferences("ExchangeDetails", Context.MODE_PRIVATE);
        String currentUsers = prefs.getString("ExchangeDetails", null);

        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> registeredUsers = new Gson().fromJson(currentUsers, listType);


        Log.d("Exchange Details", registeredUsers.toString());



        sourceFlag = findViewById(R.id.sourceFlag);
        destFlag = findViewById(R.id.destFlag);

        sourceCurrNumber = findViewById(R.id.numberforCurr);
        sourceCurrSymbol = findViewById(R.id.SymbolCurr);
        destCurrNumber = findViewById(R.id.numberforDest);
        destCurrSymbol = findViewById(R.id.SymbolDest);
        sourceCountryName = findViewById(R.id.sourceCountryName);
        destCountryName = findViewById(R.id.destCountryName);



        String sourceFlagURL = registeredUsers.get(0);
        String destinationFlagURL = registeredUsers.get(4);
        sourceCurrNumber.setText(registeredUsers.get(8));
        sourceCurrSymbol.setText(registeredUsers.get(3));

        sourceCountryName.setText(registeredUsers.get(1));
        destCountryName.setText(registeredUsers.get(5));


        destCurrSymbol.setText(registeredUsers.get(7));
        destCurrNumber.setText(registeredUsers.get(9));



        Picasso.get().load(sourceFlagURL).into(sourceFlag);
        Picasso.get().load(destinationFlagURL).into(destFlag);

        Log.d("TETSTSTS", "Test??");
    }
}
