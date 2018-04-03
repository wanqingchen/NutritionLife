package com.example.wanqingchen.nutritionlife;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private String[] breakfastOutput;
    private String userID;

    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;

    private Float[] CalorieData;
    private Float SoFarToday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView dateview=(TextView) findViewById(R.id.Date);
        String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        dateview.setText(currentDateTimeString);


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

                    toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    toastMessage("Successfully signed out.");
                }
            }
        };

        readData(userID,currentDateTimeString);

//        readNutritionReport();
//
//        readProfile();

    }

    public void btnFood_Click(View v) {
        Intent i = new Intent(MainActivity.this, SearchFood.class);
        startActivity(i);
    }

    public void btnMyDay_Click(View v) {
        Intent i = new Intent(MainActivity.this, MyDay.class);
     //   readProfile();
        startActivity(i);
    }

    public void readData(String userID, String Date) {

        myRef.child("users").child(userID).child("foodreport").child(Date).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //use the onDataChange() method to read a static snapshot of the contents at a given path
                        // Get Post object and use the values to update the UI

                        //breakfast
                        String valueBreakfast = "";
                        DataSnapshot mealSnapshot = dataSnapshot.child("Breakfast");
                        Iterable<DataSnapshot> mealChildren = mealSnapshot.getChildren();
                        for(DataSnapshot Meal: mealChildren)
                        {
                            valueBreakfast += Meal.child("name").getValue().toString();
                            valueBreakfast += System.getProperty("line.separator");
                        }

                        TextView breakfast=(TextView) findViewById(R.id.breakfastItem);
                        breakfast.setText(valueBreakfast);
                        //Log.d(TAG, "Value is: " + value);


                        //lunch
                        String valueLunch = "";
                        DataSnapshot mealSnapshot2 = dataSnapshot.child("Lunch");
                        Iterable<DataSnapshot> mealChildren2 = mealSnapshot2.getChildren();
                        for(DataSnapshot Meal2: mealChildren2)
                        {
                            valueLunch += Meal2.child("name").getValue().toString();
                            valueLunch += System.getProperty("line.separator");
                        }

                        TextView lunch=(TextView) findViewById(R.id.lunchItem);
                        lunch.setText(valueLunch);

                        //dinner
                        String valueDinner = "";
                        DataSnapshot mealSnapshot3 = dataSnapshot.child("Dinner");
                        Iterable<DataSnapshot> mealChildren3 = mealSnapshot3.getChildren();
                        for(DataSnapshot Meal3: mealChildren3)
                        {
                            valueDinner += Meal3.child("name").getValue().toString();
                            valueDinner += System.getProperty("line.separator");
                        }
                        TextView dinner=(TextView) findViewById(R.id.dinnerItem);
                        dinner.setText(valueDinner);

                        //snack
                        String valueSnack ="";
                        DataSnapshot mealSnapshot4 = dataSnapshot.child("Snack");
                        Iterable<DataSnapshot> mealChildren4 = mealSnapshot4.getChildren();
                        for(DataSnapshot Meal4: mealChildren4)
                        {
                            valueSnack += Meal4.child("name").getValue().toString();
                            valueSnack += System.getProperty("line.separator");
                        }
                        TextView snack=(TextView) findViewById(R.id.snackItem);
                        snack.setText(valueSnack);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    private void readNutritionReport(){
        String currentDateTimeString = DateFormat.getDateInstance().format(new Date());

        myRef.child("users").child(userID).child("foodreport").child(currentDateTimeString).child("NutritionReport").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        NutritionReport currentR = new NutritionReport();
                        currentR = dataSnapshot.getValue(NutritionReport.class);
                        SoFarToday= Float.parseFloat(currentR.getEnergySum());
                        Log.d("DEBUG","SoFarToday "+String.valueOf(SoFarToday));
                        CalorieData[1] = SoFarToday;
                        Log.d("DEBUG1","SoFarToday "+String.valueOf(CalorieData[1]));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

    }

    private void readProfile(){
        Intent i = new Intent(MainActivity.this, MyDay.class);
        myRef.child("users").child(userID).child("profile").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        userProfile userProfile = new userProfile();
                        userProfile = dataSnapshot.getValue(userProfile.class);
                        CalorieData[0] = Float.parseFloat(userProfile.getCalories())-SoFarToday;
                        Log.d("DEBUG","profile "+String.valueOf(Float.parseFloat(userProfile.getCalories())-SoFarToday));
                        Log.d("DEBUG2","profile "+String.valueOf(CalorieData[0]));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        i.putExtra("EnergySum",String.valueOf(CalorieData[0]));
        Log.d("DEBUG222222","profile "+String.valueOf(CalorieData[0]));
        startActivity(i);
    }
}
