package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  {

    private Button register1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = findViewById(R.id.createNewAccountBtn);

        ///button1.setOnClickListener(this);

        //register1 = (Button) findViewById(R.id.register1);
        //register1.setOnClickListener(this);
    }

    public void goToCreateNewAccount(View v){
        startActivity(new Intent(MainActivity.this, RegisterUser.class));

    }



    //@Override
    //public void onClick(View v) {
      //  switch(v.getId()){
        //    case R.id.register:
          //      startActivity(new Intent(this, RegisterUser.class));
            //    break;
       // }
    //}
}