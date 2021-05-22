package com.nsu.shipbid.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nsu.shipbid.R;

import java.util.Objects;

public class TypeSelectActivity extends AppCompatActivity {

    private Button signUpAsRider;
    private Button signUpAsDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_select);
        Objects.requireNonNull(getSupportActionBar()).hide();
        bindWidgets();
        bindListeners();
    }

    private void bindWidgets() {
        signUpAsRider = findViewById(R.id.proceed_as_rider);
        signUpAsDriver = findViewById(R.id.proceed_as_driver);
    }

    private void bindListeners() {
       /* signUpAsRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TypeSelectActivity.this, UserSignUpActivity.class));
            }
        });*/

        signUpAsDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TypeSelectActivity.this, ShipperSignUpActivity.class));
            }
        });
        signUpAsRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String emailId = email.getText().toString();
                //String passwordValue = password.getText().toString();
                //verifyLogIn(emailId, passwordValue);
                startActivity(new Intent(TypeSelectActivity.this, UserSignUpActivity.class));
            }
        });
    }
}
