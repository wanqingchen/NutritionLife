package com.example.wanqingchen.nutritionlife;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.List;

public class SearchFood extends AppCompatActivity {

    private EditText userinput;
    ListView lv;
    SearchView sv;
    List<String> nameArray = new ArrayList<String>();
    List<String> ndbnoArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("DEBUG","testing print");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);

        Button getData = (Button) findViewById(R.id.getservicedata);

        getData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                userinput = (EditText) findViewById(R.id.userinput);
                final String s = userinput.getText().toString();

                String restURL = "https://api.nal.usda.gov/ndb/search/?format=json&q="+s+"&ds=Standard%20Reference&sort=r&max=25&offset=0&api_key=TDs1ml6fSksOyqgf9zK0uIhRrgjw3dvbsBj5QuRs";
                new RestOperation().execute(restURL);

                lv=(ListView) findViewById(R.id.listview_search);


                ArrayAdapter<String> adapter = new ArrayAdapter<>(SearchFood.this,android.R.layout.simple_list_item_1,nameArray);
                lv.setAdapter(adapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        long selected_id =adapterView.getItemIdAtPosition(i);

                        String strLong = Long.toString(selected_id);
                        Log.d("DEBUG",strLong);

                        String selected_ndbno = ndbnoArray.get((int) selected_id);

                        Log.d("DEBUG",selected_ndbno);

                        Intent j = new Intent(SearchFood.this, AddFood.class);
                        j.putExtra("ndbno",selected_ndbno);
                        j.putExtra("name",nameArray.get((int) selected_id));
                        startActivity(j);
                    }
                });
            }
        });
    }

    private class RestOperation extends AsyncTask<String, Void, Void> {

        final HttpClient httpClient = new DefaultHttpClient();
        String content;
        String error;
        ProgressDialog progressDialog = new ProgressDialog(SearchFood.this);
        String data = "";



        //TextView serverDataReceived = (TextView) findViewById(R.id.serverDataReceived);
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

                JSONObject jsonResponse;

                try {
                    jsonResponse = new JSONObject(content);
                    JSONObject list = jsonResponse.getJSONObject("list");
                    JSONArray itemList = list.getJSONArray("item");

                    for (int i = 0; i < itemList.length(); i++) {
                        JSONObject singleItem = itemList.getJSONObject(i);
                        String name = singleItem.getString("name");
                        String ndbno = singleItem.getString("ndbno");
                        nameArray.add(name);
                        ndbnoArray.add(ndbno);
                    }

//                    for(int i = 0; i < 5; i++)
//                    {
//                        Log.d("DEBUG",nameArray.get(i));
//                        Log.d("DEBUG",ndbnoArray.get(i));
//
//                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }
        }
    }

}