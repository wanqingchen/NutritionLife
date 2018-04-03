package com.example.wanqingchen.nutritionlife;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private EditText txtEmailAddress;
    private EditText txtPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        txtEmailAddress = (EditText) findViewById(R.id.txtemailregistration);
        txtPassword = (EditText) findViewById(R.id.txtpasswordregistration);
        firebaseAuth = FirebaseAuth.getInstance();
        this.setTitle("Sign up");
    }

    public void btnRegistrationUser_Click(View v) {
        if ((!txtEmailAddress.getText().toString().equals("")) &&
                (!txtPassword.getText().toString().equals(""))) {
            (firebaseAuth.createUserWithEmailAndPassword(txtEmailAddress.getText().toString(),
                    txtPassword.getText().toString()))
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(RegistrationActivity.this,
                                        "Registration successful", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(RegistrationActivity.this,
                                        ProfileActivity.class);
                                startActivity(i);
                            } else {
                                Log.e("ERROR", task.getException().toString());
                                Toast.makeText(RegistrationActivity.this,
                                        task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}
