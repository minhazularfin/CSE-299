package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity  {
    TextView newUserName, newEmail, newPassword;
    Button submitBtn;

    FirebaseDatabase rootNode;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        newUserName = findViewById(R.id.newUserName);
        newEmail = findViewById(R.id.newEmail);
        newPassword = findViewById(R.id.newPassword);
        submitBtn = findViewById(R.id.submitBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");

                //Getting Data From Users
                String uName = newUserName.getEditableText().toString();
                String uMail = newEmail.getEditableText().toString();
                String uPass = newPassword.getEditableText().toString();

                UserHelperClass helperClass = new UserHelperClass(uName, uMail, uPass);

                reference.child(uPass).setValue(helperClass);

            }
        });

    }
}