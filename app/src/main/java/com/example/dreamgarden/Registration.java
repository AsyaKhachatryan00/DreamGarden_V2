package com.example.dreamgarden;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dreamgarden.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity implements View.OnClickListener {

    private Button registration_now;
    private TextView sign_now;
    private EditText name, phone, email, password, confirm;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        sign_now = findViewById(R.id.sign_in_now);
        registration_now = findViewById(R.id.sign_up);
        name = findViewById(R.id.Name);
        phone = findViewById(R.id.Phone);
        email = findViewById(R.id.Email);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm);

        sign_now.setOnClickListener(this);
        registration_now.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_now:
                startActivity(new Intent(this, Sign_in.class));
                break;
            case R.id.sign_up:
                registerUser();
                break;
        }
    }
    private void registerUser() {
        String FName = name.getText().toString().trim();
        String PNumber = phone.getText().toString().trim();
        String E_mail = email.getText().toString().trim();
        String Password = password.getText().toString().trim();
        String Confirm = confirm.getText().toString().trim();

        if (FName.isEmpty()) {
            name.setError("Name is required!");
            name.requestFocus();
            return;
        }
        if (PNumber.isEmpty()) {
            phone.setError("Number is required!");
            phone.requestFocus();
            return;
        }

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
        if (Confirm.isEmpty()) {
            confirm.setError("Please confirm your password");
            confirm.requestFocus();
            return;
        }
        if (!Confirm.equals(Password)) {
            confirm.setError("Your passwords is not equality !");
            confirm.requestFocus();
        }

        final ProgressDialog mDialog = new ProgressDialog(Registration.this);
        mDialog.setMessage("Please wait... !");
        mDialog.show();
        mAuth.createUserWithEmailAndPassword(E_mail, Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(E_mail, FName, PNumber);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mDialog.dismiss();
                                        Toast.makeText(Registration.this, "User has been registered successfully", Toast.LENGTH_LONG).show();

                                    }else {
                                        mDialog.dismiss();
                                        Toast.makeText(Registration.this, "Try again", Toast.LENGTH_LONG).show();  }
                                }
                            });
                        }else  {
                            mDialog.dismiss();
                            Toast.makeText(Registration.this, "Email registered", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}