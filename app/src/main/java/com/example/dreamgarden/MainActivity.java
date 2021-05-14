package com.example.dreamgarden;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dreamgarden.Common.Common;
import com.example.dreamgarden.Models.User;
import com.example.dreamgarden.Remote.ICloudFunctions;
import com.example.dreamgarden.Remote.RetrofitICloudClient;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Arrays;
import java.util.List;
import dmax.dialog.SpotsDialog;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity  {

    private static int APP_REQUEST_CODE = 7171;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener listener;
    private AlertDialog dialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ICloudFunctions cloudFunctions;

   private DatabaseReference userRef;
   private List<AuthUI.IdpConfig> providers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(listener);    }

    @Override
    protected void onStop() {
        if (listener != null )
            mAuth.removeAuthStateListener(listener);
        compositeDisposable.clear();
        super.onStop();    }

    private void init() {
        providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference(Common.USER_REFERENCES);

        dialog = new SpotsDialog.Builder().setCancelable(false).setContext(this).build();
        cloudFunctions = RetrofitICloudClient.getRetrofit().create(ICloudFunctions.class);
        listener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null ){
                checkUserFromFirebase(user);
            } else { phoneLogIn(); }
        };
    }

    private void checkUserFromFirebase(FirebaseUser user) {
        dialog.show();
        userRef.child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            User userModel = snapshot.getValue(User.class);
                            goToHomeActivity(userModel);
                        } else { showRegisterDialog(user); }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showRegisterDialog(FirebaseUser user) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Գրանցում");
        builder.setMessage("Լրացրեք ամբողջական ինֆորմացիա");

        View view = LayoutInflater.from(this).inflate(R.layout.registration, null);
        EditText name = view.findViewById(R.id.Name);
        EditText phone = view.findViewById(R.id.Phone);
        EditText email = view.findViewById(R.id.Email);
        EditText address = view.findViewById(R.id.address);

        phone.setText(user.getPhoneNumber());

        builder.setNegativeButton("Չեղարկել", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.setPositiveButton("Գրանցվել", (dialog, which) -> {
            if (TextUtils.isEmpty(name.getText().toString())) {
                Toast.makeText(this, "Մուտքագրեք Ձեր անունը", Toast.LENGTH_SHORT).show();
                return;
            } else  if (TextUtils.isEmpty(email.getText().toString())) {
                Toast.makeText(this, "Մուտքագրեք Ձեր էլ․ հասցեն", Toast.LENGTH_SHORT).show();
                return;
            } else  if (TextUtils.isEmpty(address.getText().toString())) {
                Toast.makeText(this, "Մուտքագրեք Ձեր հասցեն", Toast.LENGTH_SHORT).show();
                return;
            }
            User userModel = new User();
            userModel.setUid(user.getUid());
            userModel.setFName(name.getText().toString());
            userModel.setPNumber(phone.getText().toString());
            userModel.setE_mail(email.getText().toString());
            userModel.setAddress(address.getText().toString());

            userRef.child(user.getUid())
                    .setValue(userModel)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            Toast.makeText(MainActivity.this, "Գրանցումը հաջողվեց", Toast.LENGTH_SHORT).show();
                            goToHomeActivity(userModel);
                        }
                    });
        });

        builder.setView(view);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void phoneLogIn() {
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                        .setAvailableProviders(providers).build(),
                APP_REQUEST_CODE);
    }

@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == APP_REQUEST_CODE) {
        IdpResponse response = IdpResponse.fromResultIntent(data);
        if (requestCode == RESULT_OK) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        } else  {
//            Toast.makeText(this, "Failed to sign in", Toast.LENGTH_SHORT).show();
        }
    }
}

    private void goToHomeActivity(User userModel) {
        Common.currentUser = userModel;
        startActivity(new Intent(MainActivity.this, Home.class));
        finish();
    }
}