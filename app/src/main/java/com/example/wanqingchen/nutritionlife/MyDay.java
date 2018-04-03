package com.example.wanqingchen.nutritionlife;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MyDay extends AppCompatActivity {

    private String userID;

    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseDatabase mFirebaseDatabase;

    private int CalorieData[] = {1500,300};
    private Float SoFarToday;

    private String text[] = {"Under Budget","So Far Today"};

    private void readNutritionReport(){
//        String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
//
//        myRef.child("users").child(userID).child("foodreport").child(currentDateTimeString).child("NutritionReport").addListenerForSingleValueEvent(
//                new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        NutritionReport currentR = new NutritionReport();
//                        currentR = dataSnapshot.getValue(NutritionReport.class);
//                        SoFarToday= Float.parseFloat(currentR.getEnergySum());
//                        Log.d("DEBUG","SoFarToday "+String.valueOf(SoFarToday));
//                        //CalorieData[1] = SoFarToday;
//                        Log.d("DEBUG1","SoFarToday "+String.valueOf(CalorieData[1]));
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                    }
//                });

    }

    private void readProfile(){
//        myRef.child("users").child(userID).child("profile").addListenerForSingleValueEvent(
//                new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        userProfile userProfile = new userProfile();
//                        userProfile = dataSnapshot.getValue(userProfile.class);
//                        CalorieData[0] = Float.parseFloat(userProfile.getCalories())-SoFarToday;
//                        Log.d("DEBUG","profile "+String.valueOf(Float.parseFloat(userProfile.getCalories())-SoFarToday));
//                        Log.d("DEBUG2","profile "+String.valueOf(CalorieData[0]));
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                    }
//                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_day);
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

//        readProfile();
//        readNutritionReport();






        setupPieChart();


//        List<PieEntry> pieEntries = new ArrayList<>();
//        for(int i = 0; i < CalorieData.length; i++)
//        {
//            pieEntries.add(new PieEntry(CalorieData[i],text[i]));
//        }
//
//        PieDataSet dataSet = new PieDataSet(pieEntries,"Calorie");
//        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//        PieData data = new PieData(dataSet);
//
//        PieChart chart = (PieChart) findViewById(R.id.Calorieschart);
//        chart.setData(data);
//        chart.animateY(1000);
//        chart.invalidate();


    }

    private void setupPieChart(){
        List<PieEntry> pieEntries = new ArrayList<>();
        for(int i = 0; i < CalorieData.length; i++)
        {
            pieEntries.add(new PieEntry(CalorieData[i],text[i]));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries,"Calorie");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);

        PieChart chart = (PieChart) findViewById(R.id.Calorieschart);
        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();
    }



    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


}
