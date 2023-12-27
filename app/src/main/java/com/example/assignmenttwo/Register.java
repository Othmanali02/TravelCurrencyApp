package com.example.assignmenttwo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class Register extends AppCompatActivity {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private EditText email;
    private EditText password;

    private EditText name;
    private EditText confirm_password;
    private Button registerButton;
    private Button redirectLogin;

//    private void setupSharedPrefs() {
//        SharedPreferences prefs = getSharedPreferences("LocalStorage", MODE_PRIVATE);
//        editor = prefs.edit();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
//        setupSharedPrefs();

        email = findViewById(R.id.txtEmail);
        password = findViewById(R.id.txtPwd);
        name = findViewById(R.id.txtName);
        confirm_password = findViewById(R.id.txtConfirmPwd);
        registerButton = findViewById(R.id.btnRegister);
        redirectLogin = findViewById(R.id.lnkLogin);


        redirectLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Register.this,MainActivity.class);

                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Log.d("PLEASEEE WORK");
                // Get text from EditText widgets
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();
                String nameString = name.getText().toString();
                String confPasswordString = password.getText().toString();

                // Log the texts
                Log.d("Register", "Email: " + emailString);
                Log.d("Register", "Password: " + passwordString);

                Log.d("Register", "Name: " + nameString);
                Log.d("Register", "ConfPassword: " + confPasswordString);

                boolean isEqual = passwordString.equals(confPasswordString);

                if(isEqual){
                    Log.d("Check if its equal", "Yes, it is working");

                    SharedPreferences prefs = getSharedPreferences("LocalStorage", Context.MODE_PRIVATE);
                    String currentUsers = prefs.getString("LocalStorage", null);

                    Log.d("Users", currentUsers);


                    Type listType = new TypeToken<ArrayList<String>>() {}.getType();
                    ArrayList<String> registeredUsers = new Gson().fromJson(currentUsers, listType);


                    registeredUsers.add((registeredUsers.toArray().length + 1) + " - " + emailString + " - " +  nameString + " - " +   passwordString);


                    Log.d("Register", registeredUsers.toString());

                    SharedPreferences.Editor editor = prefs.edit();

                    Gson gson = new Gson();
                    String errands = gson.toJson(registeredUsers);
                    editor.putString("LocalStorage", errands);
                    editor.commit();

                    Intent intent = new Intent(Register.this, MainActivity.class);
                    System.out.println(intent);
                    startActivity(intent);

                }else{
                    Log.d("Register", "Not equal bruh");

                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setTitle("Form Error").setMessage("Passwords do not match").setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }}).show();
                }

//



            }
        });




    }//end of onCreate



}
