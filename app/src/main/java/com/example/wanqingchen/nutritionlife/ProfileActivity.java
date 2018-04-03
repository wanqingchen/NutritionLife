package com.example.wanqingchen.nutritionlife;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "AddToDatabase";

    private Button btnSave;

    private String gender,diabetesType;
    private EditText BloodGlucose,Calories,Fat,Protein,Carbs,Birthday;

    private String strBloodGlucose,strCalories,strFat,strProtein,strCarbs,strBirthday;

    private String userID;

    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BloodGlucose = (EditText) findViewById(R.id.editText4);
        Calories = (EditText) findViewById(R.id.inputCalories);
        Fat = (EditText) findViewById(R.id.inputFat);
        Protein = (EditText) findViewById(R.id.inputProtein);
        Carbs = (EditText) findViewById(R.id.inputCarbs);
        Birthday = (EditText) findViewById(R.id.editText2);

        //declare the database reference object. This is what we use to access the database.
        //NOTE: Unless you are signed in, this will not be usable.
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out.");
                }
            }
        };
    }

    public void genderOnCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkBox:
                if (checked)
                // female
                {
                    gender = "female";
                }
            else
                break;
            case R.id.checkBox2:
                // male
                if (checked)
                {
                    gender = "male";
                }
            else
                break;
        }
    }

    public void DiabetesOnCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkBox4:
                if (checked)
                {
                    diabetesType = "Type1";
                }
                else
                    break;
            case R.id.checkBox5:
                if (checked)
                {
                    diabetesType = "Type2";
                }
                else
                    break;
            case R.id.checkBox3:
                if (checked)
                {
                    diabetesType = "Gestational";
                }
                else
                    break;
            case R.id.checkBox6:
                if (checked)
                {
                    diabetesType = "Pre-diabetes";
                }
                else
                    break;
        }
    }

    public void btnSave_Click(View v) {

        strBloodGlucose = BloodGlucose.getText().toString();
        strCalories = Calories.getText().toString();
        strFat = Fat.getText().toString();
        strProtein = Protein.getText().toString();
        strCarbs = Carbs.getText().toString();
        strBirthday = Birthday.getText().toString();

        if(strBloodGlucose.equals("")||strCalories.equals("")||strFat.equals("")||strProtein.equals("")||strCarbs.equals("")||strBirthday.equals(""))
        {
            toastMessage("Please fill out all the fields");
        }
        else
        {
            userProfile userProfile= new userProfile(gender, diabetesType, strBloodGlucose,strCalories, strFat, strProtein, strCarbs, strBirthday);
            myRef.child("users").child(userID).child("profile").setValue(userProfile);
            toastMessage("User's information has been saved.");
            Intent i = new Intent(ProfileActivity.this, Welcome.class);
            startActivity(i);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}

