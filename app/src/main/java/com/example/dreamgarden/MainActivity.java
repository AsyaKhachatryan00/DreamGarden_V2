package com.example.dreamgarden;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button sign_in, registration, not_now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sign_in = findViewById(R.id.sign_in);
        registration = findViewById(R.id.registration);
        not_now = findViewById(R.id.not_now);

        sign_in.setOnClickListener(this);
        registration.setOnClickListener(this);
        not_now.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in:
                startActivity(new Intent(this, Sign_in.class));
                break;
            case R.id.registration:
                startActivity(new Intent(this, Registration.class));
                break;
            case R.id.not_now:
                startActivity(new Intent(this, Home.class));
                break;
        }
    }

}