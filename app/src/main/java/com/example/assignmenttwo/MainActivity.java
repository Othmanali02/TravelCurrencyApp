package com.example.assignmenttwo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private EditText email;
    private EditText password;
    private Button loginButton;

    private CheckBox rememberMe;
    private Button redirectRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.txtEmail);
        password = findViewById(R.id.txtPwd);
        loginButton = findViewById(R.id.btnLogin);
        redirectRegister = findViewById(R.id.lnkRegister);
        rememberMe = findViewById(R.id.rememeeMeLabel);


        User[] users = new User[1];
        users[0] = new User("othman90hijawi@gmail.com", "1234", "Othman");



        try{
            SharedPreferences prefs = getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
            String currentUser = prefs.getString("CurrentUser", null);

            Log.d("DOES THIS EVEN WORK?!!!!!!!!", currentUser);

            Type listType = new TypeToken<ArrayList<String>>() {}.getType();
            ArrayList<String> loggedUser = new Gson().fromJson(currentUser, listType);

            if(loggedUser.get(1).equals("Remember")){
                Intent intent = new Intent(MainActivity.this, Translate.class);
                System.out.println(intent);
                startActivity(intent);
            }

            Log.d("CurrentUser", loggedUser.toString());

        }catch(Exception e){
            Log.d("Error in Prefs", "Error reading prefs");
        }



        System.out.println("BRo come one");


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Log.d("PLEASEEE WORK");
                // Get text from EditText widgets
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();
                boolean rememberMeCheck = rememberMe.isChecked();

                // Log the texts
                Log.d("MainActivity", "Email: " + emailString);
                Log.d("MainActivity", "Password: " + passwordString);

                SharedPreferences prefs = getSharedPreferences("LocalStorage", Context.MODE_PRIVATE);
                String currentUsers = prefs.getString("LocalStorage", null);

                Type listType = new TypeToken<ArrayList<String>>() {}.getType();
                ArrayList<String> registeredUsers = new Gson().fromJson(currentUsers, listType);

                Log.d("Register", registeredUsers.toString());

                boolean ValidUser = false;

                int userIndex = 0;

                for(int i = 0; i < registeredUsers.size(); i++){
                    String userRecord = registeredUsers.get(i);
//                    2 and last things to compare in this loop
                    String[] userRecordSplit = userRecord.split("-");
                    String dbUserEmail = "";
                    String dbUserPass = "";


                    dbUserEmail = userRecordSplit[1].toLowerCase().trim();
                    dbUserPass = userRecordSplit[userRecordSplit.length - 1].toLowerCase().trim();


                    Log.d("dbUserEmail", dbUserEmail);
                    Log.d("dbUserPass", dbUserPass);


                    if(dbUserEmail.equals(emailString.toLowerCase().trim()) && dbUserPass.equals(passwordString)){//this means it is a valid user
                        Log.d("User Validity", "This user is valid!!!");
                        ValidUser = true;
                        userIndex = i;
                    }
                }

                if(ValidUser){

                    ArrayList<String> loggedUserDetails = new ArrayList<String>();

                    String getUser = registeredUsers.get(userIndex);
                    String[] userRecordSplit = getUser.split("-");
                    String dbUserEmail =  userRecordSplit[1].toLowerCase().trim();
                    String dbUserName =  userRecordSplit[2].toLowerCase().trim();
                    String dbUserPass = userRecordSplit[3].toLowerCase().trim();

                    User RegisteredUser = new User(dbUserEmail, dbUserPass, dbUserName);

                    loggedUserDetails.add(RegisteredUser.toString());


                    if(rememberMeCheck){

                        Log.d("Remeber This user...", "Remember User");

//                        implement the accept user functionality

                        // here i'm creating the remember user functionality

                        loggedUserDetails.add("Remember");

                    }else{
                        loggedUserDetails.add("NuhUh");//don't remember the user if they didn't click on it
                    }


                    SharedPreferences prefs2 = getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = prefs2.edit();

                    Log.d("Logged User", loggedUserDetails.toString());

                    Gson gson = new Gson();
                    String currentUser = gson.toJson(loggedUserDetails);
                    editor.putString("CurrentUser", currentUser);
                    editor.apply();

                    Intent intent = new Intent(MainActivity.this, Translate.class);
                    System.out.println(intent);
                    startActivity(intent);
                }

            }
        });

        redirectRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,Register.class);

                startActivity(intent);
            }
        });

    }



}





