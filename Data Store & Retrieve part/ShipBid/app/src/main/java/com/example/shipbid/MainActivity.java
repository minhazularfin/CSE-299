package com.example.shipbid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private EditText pName, pType, pStartingValue;
    private Button pButton, rButton;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("post");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pName = findViewById(R.id.name);
        pType = findViewById(R.id.productType);
        pStartingValue = findViewById(R.id.startingValue);
        pButton = findViewById(R.id.postButton);
        rButton = findViewById(R.id.showDataBtn);


        pButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = pName.getText().toString();
                //root.child("01").setValue(productName);
                //root.setValue(productName);
                String productType = pType.getText().toString();
                String sValue = pStartingValue.getText().toString();

                HashMap<String, String> userMap = new HashMap<>();

                userMap.put("productName", productName);
                userMap.put("productType", productType);
                userMap.put("sValue", sValue);

                root.push().setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, "Data Stored Successfully!!!!",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        //Retrieve Data Button
        rButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShowData.class));
            }
        });

    }
}