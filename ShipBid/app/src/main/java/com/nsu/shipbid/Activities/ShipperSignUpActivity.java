package com.nsu.shipbid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nsu.shipbid.R;

import java.util.Objects;

public class ShipperSignUpActivity extends AppCompatActivity {
    private Button back;
    private Button confirm;
    private EditText name,phoneNumber,carNumber,email,password,confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_sign_up);
        Objects.requireNonNull(getSupportActionBar()).hide();
        bindWidgets();
        bindListeners();
    }

    private void bindWidgets() {
        back = findViewById(R.id.driver_back_button);
        confirm = findViewById(R.id.driver_sign_up_button);
        name = findViewById(R.id.driver_full_name);
        phoneNumber = findViewById(R.id.driver_sign_up_phone_number);
        carNumber = findViewById(R.id.driver_sign_up_car_number);
        email = findViewById(R.id.driver_sign_up_email_id);
        password = findViewById(R.id.driver_sign_up_password);
        confirmPassword = findViewById(R.id.driver_confirm_password);
    }

    private void bindListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verifyInput(new EditText[]{name,phoneNumber,carNumber,email,password,confirmPassword})){
                    if(verifyPassword()){
                        signUp();
                    }
                }
            }
        });
    }

    private boolean verifyInput(EditText[] editTexts){
        boolean state = true;
        for(EditText editText: editTexts){
            if(TextUtils.isEmpty(editText.getText().toString())) {
                state = false;
                editText.setError("Field can't be empty!");
            }
        }
        return  state;
    }

    private boolean verifyPassword(){
        String pass = password.getText().toString();
        String conPass = confirmPassword.getText().toString();
        return pass.equals(conPass);
    }

    private void signUp(){
        final String fullName = name.getText().toString();
        final String phone = phoneNumber.getText().toString();
        final String car = carNumber.getText().toString();
        final String mail = email.getText().toString();
        String pass = password.getText().toString();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(mail,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            assert firebaseUser != null;
                            String uId = firebaseUser.getUid();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/users/"+uId);
                            databaseReference.child("name").setValue(fullName);
                            databaseReference.child("phoneNumber").setValue(phone);
                            databaseReference.child("carNumber").setValue(car);
                            databaseReference.child("email").setValue(mail);
                            databaseReference.child("profileType").setValue("driver");
                            Toast.makeText(ShipperSignUpActivity.this, "Sign up successful for ".concat(fullName), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ShipperSignUpActivity.this, FindUserActivity.class));
                        }
                    }
                });
    }
}
