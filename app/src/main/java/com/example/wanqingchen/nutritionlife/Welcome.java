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

public class Welcome extends AppCompatActivity {

    private EditText txtEmailLogin;
    private EditText txtPwd;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        txtEmailLogin = (EditText) findViewById(R.id.txtemaillogin);
        txtPwd = (EditText) findViewById(R.id.txtpasswordlogin);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void btnRegistration_Click(View v) {
        Intent i = new Intent(Welcome.this, RegistrationActivity.class);
        startActivity(i);
    }

    public void btnSignin_Click(View v) {
        if (( !txtEmailLogin.getText().toString().equals("")) &&
                ( !txtPwd.getText().toString().equals(""))) {
            (firebaseAuth.signInWithEmailAndPassword(txtEmailLogin.getText().toString(),
                    txtPwd.getText().toString()))
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Welcome.this, "Login successful",
                                        Toast.LENGTH_LONG).show();
                                Intent i = new Intent(Welcome.this, MainActivity.class);
                                i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
                                startActivity(i);
                            }
                            else {
                                Log.e("ERROR", task.getException().toString());
                                Toast.makeText(Welcome.this, task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

}
