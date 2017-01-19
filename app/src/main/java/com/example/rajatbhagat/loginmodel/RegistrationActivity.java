package com.example.rajatbhagat.loginmodel;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class RegistrationActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;

    private TextInputLayout usernameTextInputLayout;
    private TextInputLayout emailTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    private TextInputLayout confirmPasswordTextInputLayout;

    private Button registerButton;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        usernameEditText = (EditText) findViewById(R.id.edit_text_register_username);
        emailEditText = (EditText) findViewById(R.id.edit_text_register_email);
        passwordEditText = (EditText) findViewById(R.id.edit_text_register_password);
        confirmPasswordEditText = (EditText) findViewById(R.id.edit_text_register_confirm_password);

        usernameTextInputLayout = (TextInputLayout) findViewById(R.id.text_ip_layout_register_username);
        emailTextInputLayout = (TextInputLayout) findViewById(R.id.text_ip_layout_register_email);
        passwordTextInputLayout = (TextInputLayout) findViewById(R.id.text_ip_layout_register_password);
        confirmPasswordTextInputLayout = (TextInputLayout) findViewById(R.id.text_ip_layout_register_confirm_password);

        registerButton = (Button) findViewById(R.id.button_register_submit);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating Account..");

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                createAccount();
                Log.e("Clicking button", "Registering user");
            }
        });
    }

    private void createAccount() {

        final String url = Constants.REGISTER_URL;

        final String userName = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        final String confirmPassword = confirmPasswordEditText.getText().toString();
        final String email = emailEditText.getText().toString();

        if (confirmPassword.equalsIgnoreCase(password)) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "User Registered", Toast.LENGTH_SHORT).show();
                    Log.e("Response on registering", response);

                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                    RegistrationActivity.this.startActivity(intent);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RegistrationActivity.this, "Error in creating User", Toast.LENGTH_SHORT).show();
                    Log.e("Error on registering", error.getMessage());
                    progressDialog.dismiss();
                }
            }) {
                //This method sends data to the server
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", userName);
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivity.this);
            requestQueue.add(stringRequest);
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(RegistrationActivity.this, "Passwords dont match", Toast.LENGTH_SHORT).show();
            passwordEditText.setText("");
            confirmPasswordEditText.setText("");
        }
    }
}
