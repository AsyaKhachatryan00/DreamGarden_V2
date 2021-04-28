package com.example.dreamgarden;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Sign_in extends AppCompatActivity implements View.OnClickListener {

    private Button sign;
    private TextView sign_up;
    private EditText email, password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        sign = findViewById(R.id.sign_now);
        sign_up = findViewById(R.id.registration_now);
        email = findViewById(R.id.E_mail);
        password = findViewById(R.id.pass);

        sign_up.setOnClickListener(this);
        sign.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.registration_now:
                startActivity(new Intent(this, Registration.class));
                break;
            case R.id.sign_now:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String E_mail = email.getText().toString().trim();
        String Password = password.getText().toString().trim();

        if (E_mail.isEmpty()) {
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(E_mail).matches()) {
            email.setError("Please enter valid email!");
            email.requestFocus();
            return;
        }

        if (Password.isEmpty()) {
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }
        if (Password.length() < 6) {
            password.setError("Min Password length should be 6 characters ");
            password.requestFocus();
            return;
        }

        final ProgressDialog mDialog = new ProgressDialog(Sign_in.this);
        mDialog.setMessage("Please wait... !");
        mDialog.show();

        mAuth.signInWithEmailAndPassword(E_mail, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mDialog.dismiss();
                    startActivity(new Intent(Sign_in.this, Home.class));
                } else{
                    mDialog.dismiss();
                    Toast.makeText(Sign_in.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}