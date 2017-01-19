package com.example.rajatbhagat.loginmodel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AfterLogin extends AppCompatActivity {

    private TextView emailText;
    private TextView nameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        emailText = (TextView)findViewById(R.id.userText);
        nameText = (TextView)findViewById(R.id.emailText);

        emailText.setText(Constants.getEmail(AfterLogin.this));
        nameText.setText(Constants.getUsername(AfterLogin.this));
    }
}
