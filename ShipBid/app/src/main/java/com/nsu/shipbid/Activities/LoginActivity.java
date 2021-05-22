package com.nsu.shipbid.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.nsu.shipbid.R;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private Button signUp;
    private Button logIn;
    private EditText email;
    private EditText password;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();
        bindWidgets();
        bindListeners();
        requestPermissions();
    }

    private void requestPermissions() {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
            }
            else{
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
        }
    }

    private void bindWidgets() {
        signUp = findViewById(R.id.sign_up_button);
        logIn = findViewById(R.id.sign_in_button);
        email = findViewById(R.id.email_id);
        password = findViewById(R.id.password);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void bindListeners() {
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, TypeSelectActivity.class));
            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String emailId = email.getText().toString();
                //String passwordValue = password.getText().toString();
                //verifyLogIn(emailId, passwordValue);
                startActivity(new Intent(LoginActivity.this, FindShipperActivity.class));
            }
        });
    }

    /*private void verifyLogIn(final String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            databaseReference = FirebaseDatabase.getInstance().getReference("/users/");
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    setProfileTypeAndChangeActivity(dataSnapshot);
                                }

                                private void setProfileTypeAndChangeActivity(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        Profile profile = ds.getValue(Profile.class);
                                        if (Objects.requireNonNull(profile).getEmail().equals(email)) {
                                            final String profile_type = profile.getProfileType();
                                            if(profile_type.equals("driver")){
                                                startActivity(new Intent(LoginActivity.this, FindUserActivity.class));
                                            }
                                            else{
                                                startActivity(new Intent(LoginActivity.this, FindShipperActivity.class));
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, "failed!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }*/

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.exit_title)
                .setMessage(R.string.exit_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
}
