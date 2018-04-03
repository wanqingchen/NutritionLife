package com.example.wanqingchen.nutritionlife;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddFood extends AppCompatActivity {

    private CheckBox Breakfast,Lunch,Dinner,Snack;

    private EditText userinput;
    private NumberPicker npicker;
    private int selectedIndex;
    private String[] measureArray;
    private String ndbno;
    private EditText number;
    private int num;
    private String Energy,Protein,TotalFat,SaturatedFat,Carbohydrate,Cholesterol;
    private String name;

    private String meal;
    private String userID;


    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);




        Button getData = (Button) findViewById(R.id.getservicedata);
        npicker = (NumberPicker) findViewById(R.id.measures);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ndbno = extras.getString("ndbno");
            Log.d("DEBUG_AddFood",ndbno);
        }

        String restURL = "https://api.nal.usda.gov/ndb/reports/?ndbno=" + ndbno + "&type=f&format=json&api_key=TDs1ml6fSksOyqgf9zK0uIhRrgjw3dvbsBj5QuRs";
        new RestOperation().execute(restURL);



        npicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {

                number = (EditText) findViewById(R.id.inputCarbs);
                num = Integer.parseInt(number.getText().toString());

                selectedIndex = numberPicker.getValue();
                String restURL = "https://api.nal.usda.gov/ndb/reports/?ndbno=" + ndbno + "&type=f&format=json&api_key=TDs1ml6fSksOyqgf9zK0uIhRrgjw3dvbsBj5QuRs";
               new RestOperation2().execute(restURL);
            }
        });


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

    }




    public void btnAdd_Click(View v) {
        Breakfast = (CheckBox) findViewById(R.id.BreakfastCheckBox);
        Lunch = (CheckBox) findViewById(R.id.LunchCheckBox);
        Dinner = (CheckBox) findViewById(R.id.DinnerCheckBox);
        Snack = (CheckBox) findViewById(R.id.SnackCheckBox);

        if(Breakfast.isChecked()) meal = "Breakfast";
        if(Lunch.isChecked()) meal = "Lunch";
        if(Dinner.isChecked()) meal = "Dinner";
        if(Snack.isChecked()) meal = "Snack";

        Log.d("DEBUG","meal = "+meal);

        Intent i = new Intent(AddFood.this, MainActivity.class);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            i.putExtra("name",name);
        }

        final String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        FoodReport FoodReport;
        FoodReport = new FoodReport(name,Energy, Protein, TotalFat, SaturatedFat, Carbohydrate, Cholesterol);
        toastMessage("Food information has been saved.");
        Log.d("DEGUB","addmeal:"+meal);
        myRef.child("users").child(userID).child("foodreport").child(currentDateTimeString).child(meal).child(name).setValue(FoodReport);

        //check if there is nutrition report for today, if not, new a new one
        //ValueEventListener valueEventListener =
        myRef.child("users").child(userID).child("foodreport").child(currentDateTimeString).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("NutritionReport")) {
                            Log.d("DEBUG", "already has a nutrition report");
                            updateData();
                        } else {
                            Log.d("DEBUG", "create first nutrition report");
                            NutritionReport NutritionReport;
                            String EnergySum, ProteinSum, TotalFatSum, CarbohydrateSum;
                            EnergySum = Energy;
                            ProteinSum = Protein;
                            TotalFatSum = TotalFat;
                            CarbohydrateSum = Carbohydrate;
                            NutritionReport = new NutritionReport(EnergySum, ProteinSum, TotalFatSum, CarbohydrateSum);
                            myRef.child("users").child(userID).child("foodreport").child(currentDateTimeString).child("NutritionReport").setValue(NutritionReport);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });

        startActivity(i);
    }

    public void btnCancel_Click(View v) {
        Intent i = new Intent(AddFood.this, MainActivity.class);
        startActivity(i);
    }


    private class RestOperation2 extends AsyncTask<String, Void, Void> {

        final HttpClient httpClient = new DefaultHttpClient();
        String content;
        String error;
        ProgressDialog progressDialog = new ProgressDialog(AddFood.this);
        String data = "";
        TextView showParsedJSON = (TextView) findViewById(R.id.showParsedJSON);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setTitle("Please wait ...");
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(String... params) {
            BufferedReader br = null;

            URL url;
            try {
                url = new URL(params[0]);

                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);

                OutputStreamWriter outputStreamWr = new OutputStreamWriter(connection.getOutputStream());
                outputStreamWr.write(data);
                outputStreamWr.flush();

                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append(System.getProperty("line.separator"));
                }

                content = sb.toString();

            } catch (MalformedURLException e) {
                error = e.getMessage();
                e.printStackTrace();
            } catch (IOException e) {
                error = e.getMessage();
                e.printStackTrace();
            } finally {
                try {

                    br.close();


                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            if (error != null) {
                //serverDataReceived.setText("Error " + error);
            } else {


                String output = "";
                JSONObject jsonResponse;

                try {
                    jsonResponse = new JSONObject(content);
                    JSONObject report = jsonResponse.getJSONObject("report");
                    JSONObject food = report.getJSONObject("food");
                    JSONArray nutrients = food.getJSONArray("nutrients");

                    for (int i = 0; i < 50; i++) {
                        JSONObject singleNutrient = nutrients.getJSONObject(i);
                        String id = singleNutrient.getString("nutrient_id");

                        if (id.equals("208")) {
                            JSONArray measures = singleNutrient.getJSONArray("measures");
                            for (int j = 0; j < measures.length(); j++) {
                                JSONObject singleMeasure = measures.getJSONObject(j);
                                String label = singleMeasure.getString("label");
                                if(label.equals(measureArray[selectedIndex]))
                                {
                                    String value1 = singleMeasure.getString("value");
                                    Log.d("DEBUG_value",value1);
                                    Log.d("DEBUG_num",Integer.toString(num));
                                    double val1 = Double.parseDouble(value1);
                                    value1 = Double.toString(num * val1);
                                    Log.d("DEBUG_value*num",value1);
                                    output +="Energy :     ";
                                    output += value1;
                                    output +=" kcal";
                                    output += System.getProperty("line.separator");
                                    output += System.getProperty("line.separator");
                                    Energy = value1;
                                }
                            }
                        }

                        if (id.equals("203")) {
                            JSONArray measures = singleNutrient.getJSONArray("measures");
                            for (int j = 0; j < measures.length(); j++) {
                                JSONObject singleMeasure = measures.getJSONObject(j);
                                String label = singleMeasure.getString("label");
                                if(label.equals(measureArray[selectedIndex]))
                                {
                                    String value2 = singleMeasure.getString("value");
                                    output +="Protein :     ";
                                    output += value2;
                                    output +=" g";
                                    output += System.getProperty("line.separator");
                                    output += System.getProperty("line.separator");
                                    Protein = value2;
                                }
                            }
                        }

                        if (id.equals("204")) {
                            JSONArray measures = singleNutrient.getJSONArray("measures");
                            for (int j = 0; j < measures.length(); j++) {
                                JSONObject singleMeasure = measures.getJSONObject(j);
                                String label = singleMeasure.getString("label");
                                if(label.equals(measureArray[selectedIndex]))
                                {
                                    String value3 = singleMeasure.getString("value");
                                    output +="Total fat :     ";
                                    output += value3;
                                    output +=" g";
                                    output += System.getProperty("line.separator");
                                    output += System.getProperty("line.separator");
                                    TotalFat = value3;
                                }
                            }
                        }

                        if (id.equals("606")) {
                            JSONArray measures = singleNutrient.getJSONArray("measures");
                            for (int j = 0; j < measures.length(); j++) {
                                JSONObject singleMeasure = measures.getJSONObject(j);
                                String label = singleMeasure.getString("label");
                                if(label.equals(measureArray[selectedIndex]))
                                {
                                    String value4 = singleMeasure.getString("value");
                                    output +="Saturated fat :     ";
                                    output += value4;
                                    output +=" g";
                                    output += System.getProperty("line.separator");
                                    output += System.getProperty("line.separator");
                                    SaturatedFat = value4;
                                }
                            }
                        }

                        if (id.equals("205")) {
                            JSONArray measures = singleNutrient.getJSONArray("measures");
                            for (int j = 0; j < measures.length(); j++) {
                                JSONObject singleMeasure = measures.getJSONObject(j);
                                String label = singleMeasure.getString("label");
                                if(label.equals(measureArray[selectedIndex]))
                                {
                                    String value5 = singleMeasure.getString("value");
                                    output +="Carbohydrate :     ";
                                    output += value5;
                                    output +=" g";
                                    output += System.getProperty("line.separator");
                                    output += System.getProperty("line.separator");
                                    Carbohydrate = value5;
                                }
                            }
                        }

                        if (id.equals("601")) {
                            JSONArray measures = singleNutrient.getJSONArray("measures");
                            for (int j = 0; j < measures.length(); j++) {
                                JSONObject singleMeasure = measures.getJSONObject(j);
                                String label = singleMeasure.getString("label");
                                if(label.equals(measureArray[selectedIndex]))
                                {
                                    String value6 = singleMeasure.getString("value");
                                    output +="Cholesterol :     ";
                                    output += value6;
                                    output +=" mg";
                                    output += System.getProperty("line.separator");
                                    output += System.getProperty("line.separator");
                                    Cholesterol = value6;
                                }
                            }
                        }
                    }

                    showParsedJSON.setText(output);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }
        }
    }


    private class RestOperation extends AsyncTask<String, Void, Void> {

        final HttpClient httpClient = new DefaultHttpClient();
        String content;
        String error;
        ProgressDialog progressDialog = new ProgressDialog(AddFood.this);
        String data = "";
        //TextView serverDataReceived = (TextView) findViewById(R.id.serverDataReceived);
        TextView showParsedJSON = (TextView) findViewById(R.id.showParsedJSON);
        List<String> measureList = new ArrayList<String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setTitle("Please wait ...");
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(String... params) {
            BufferedReader br = null;

            URL url;
            try {
                url = new URL(params[0]);

                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);

                OutputStreamWriter outputStreamWr = new OutputStreamWriter(connection.getOutputStream());
                outputStreamWr.write(data);
                outputStreamWr.flush();

                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append(System.getProperty("line.separator"));
                }

                content = sb.toString();

            } catch (MalformedURLException e) {
                error = e.getMessage();
                e.printStackTrace();
            } catch (IOException e) {
                error = e.getMessage();
                e.printStackTrace();
            } finally {
                try {

                    br.close();


                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            if (error != null) {
                //serverDataReceived.setText("Error " + error);
            } else {

                JSONObject jsonResponse;

                try {
                    jsonResponse = new JSONObject(content);
                    JSONObject report = jsonResponse.getJSONObject("report");
                    JSONObject food = report.getJSONObject("food");
                    JSONArray nutrients = food.getJSONArray("nutrients");

                    for (int i = 0; i < 10; i++) {
                        JSONObject singleNutrient = nutrients.getJSONObject(i);
                        String id = singleNutrient.getString("nutrient_id");

                        if (id.equals("208")) {
                            JSONArray measures = singleNutrient.getJSONArray("measures");
                            for (int j = 0; j < measures.length(); j++) {
                                JSONObject singleMeasure = measures.getJSONObject(j);
                                String label = singleMeasure.getString("label");
                                measureList.add(label);
                            }

                        }
                    }

                    measureArray = new String[measureList.size()];
                    measureList.toArray(measureArray);

                    npicker.setDisplayedValues(measureArray);
                    npicker.setMinValue(0);
                    npicker.setMaxValue(measureList.size() - 1);


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    private void updateData(){
        final String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        //addListenerForSingleValueEvent()
        myRef.child("users").child(userID).child("foodreport").child(currentDateTimeString).child("NutritionReport").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        NutritionReport currentR = new NutritionReport();
                        currentR = dataSnapshot.getValue(NutritionReport.class);
                        currentR.setTotalFatSum(String.valueOf(Float.parseFloat(currentR.getTotalFatSum())+Float.parseFloat(TotalFat)));
                        currentR.setCarbohydrateSum(String.valueOf(Float.parseFloat(currentR.getCarbohydrateSum())+Float.parseFloat(Carbohydrate)));
                        currentR.setProteinSum(String.valueOf(Float.parseFloat(currentR.getProteinSum())+Float.parseFloat(Protein)));
                        currentR.setEnergySum(String.valueOf(Float.parseFloat(currentR.getEnergySum())+Float.parseFloat(Energy)));
                        myRef.child("users").child(userID).child("foodreport").child(currentDateTimeString).child("NutritionReport").setValue(currentR);
//                        Intent i = new Intent(AddFood.this, MyDay.class);
//                        i.putExtra("EnergySum",String.valueOf(Float.parseFloat(currentR.getEnergySum())+Float.parseFloat(Energy)));
//                        startActivity(i);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                    });

    }

}


