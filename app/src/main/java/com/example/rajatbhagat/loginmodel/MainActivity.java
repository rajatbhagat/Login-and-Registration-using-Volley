package com.example.rajatbhagat.loginmodel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;
    private TextInputLayout usernameTextInputLayout;
    private TextInputLayout passwordTextInputLayout;

    private String email;
    private String name;
    private String firstName;
    private String lastName;

    private ProgressDialog progressDialog;

    final static int PASSWORD_MIN_LENGTH = 6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = (EditText) findViewById(R.id.edit_text_username);
        passwordEditText = (EditText) findViewById(R.id.edit_text_password);

        loginButton = (Button) findViewById(R.id.button_login);
        registerButton = (Button) findViewById(R.id.button_register);

        progressDialog = new ProgressDialog(MainActivity.this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                 Form Validator and Intent start
                if (true) {
                    progressDialog.setMessage("Logging in...");
                    progressDialog.show();
                    login();
                } else {
                    Toast.makeText(MainActivity.this, "Invalid entry", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Registration Activity", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    // Form Validator algorithm
//    private boolean isFormValid() {
//        if (!isUsernameValid() || !isPasswordValid()) {
//            if (!isUsernameValid()) {
//                usernameTextInputLayout.setErrorEnabled(true);
//                usernameTextInputLayout.setError("Invalid Username");
//            } else {
//                usernameTextInputLayout.setError(null);
//                usernameTextInputLayout.setErrorEnabled(false);
//            }
//            if (!isPasswordValid()) {
//                passwordTextInputLayout.setErrorEnabled(true);
//                passwordTextInputLayout.setError("Invalid Password");
//            } else {
//                passwordTextInputLayout.setError(null);
//                passwordTextInputLayout.setErrorEnabled(false);
//            }
//            return false;
//        } else {
//            usernameTextInputLayout.setErrorEnabled(false);
//            passwordTextInputLayout.setErrorEnabled(false);
//            return true;
//        }
//    }
//
//    // Username Validator
//    public boolean isUsernameValid() {
//        return !usernameEditText.getText().toString().trim().isEmpty();
//    }
//
//    // Password Validator
//    public boolean isPasswordValid() {
//        if (passwordEditText.getText().toString().trim().isEmpty())
//            return false;
//        else if (passwordEditText.getText().toString().trim().length() < PASSWORD_MIN_LENGTH)
//            return false;
//        return true;
//    }

    private void login() {
        String url = Constants.LOGIN_URL;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Email", response);
                email = response.substring(1, response.indexOf('-'));
                name = response.substring(response.indexOf('-') + 1, response.length() - 1);
                firstName = name.substring(0, name.indexOf('-'));
                lastName = name.substring(name.indexOf('-') + 1, name.length());
                setSharedPreferences();
                Intent intent = new Intent(MainActivity.this, AfterLogin.class);
                startActivity(intent);
                progressDialog.dismiss();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse.statusCode == 401)
                    Toast.makeText(MainActivity.this, "Wrong username/password", Toast.LENGTH_SHORT).show();
                else if (error.networkResponse.statusCode == 400)
                    Toast.makeText(MainActivity.this, "Error logging in", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Error:" + error.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.e("Error: ", error.getMessage());
            }
        }) {
            //This method sends data to the server
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", usernameEditText.getText().toString());
                params.put("password", passwordEditText.getText().toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void setSharedPreferences() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("credentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.KEY_USERNAME, usernameEditText.getText().toString().trim());
        editor.putBoolean(Constants.KEY_LOGIN_STATUS, true);
        editor.putString(Constants.KEY_EMAIL, email);
        editor.putString(Constants.KEY_FIRST_NAME, firstName);
        editor.putString(Constants.KEY_LAST_NAME, lastName);
        editor.apply();
    }
}
