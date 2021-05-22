package com.nsu.shipbid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nsu.shipbid.Domains.Profile;
import com.nsu.shipbid.R;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null) {
            final String email = firebaseUser.getEmail();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/users/");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Profile profile = ds.getValue(Profile.class);
                        if (Objects.requireNonNull(profile).getEmail().equals(email)) {
                            if (profile.getProfileType().equals("driver")) {
                                startActivity(new Intent(MainActivity.this, FindUserActivity.class));
                            } else {
                                startActivity(new Intent(MainActivity.this, FindShipperActivity.class));
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }
}
