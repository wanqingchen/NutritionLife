package com.example.wanqingchen.nutritionlife;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;



/**
 * Created by wanqingchen on 3/13/18.
 *
 * AsyncTask for fetching from USDA API.
 *
 */


public class webAPItask extends AsyncTask<String, Integer, String>//first: input; last: output ,is a json string from the API
{
    private ProgressDialog progDialog;
    private Context context;
    //private TopTrackListActivity activity;
    private static final String debugTag = "LastFMWebAPITask";

//    /**
//     * Construct a task
//     * @param activity
//     */
//    public webAPItask(SearchFood activity) {
//        super();
//        this.activity = activity;
//        this.context = this.activity.getApplicationContext();
//    }

 //   @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//        progDialog = ProgressDialog.show(this.activity, "Search", this.context.getResources().getString(R.string.looking_for_tracks) , true, false);
//    }//be called on UI thread;pop a dialog

    @Override
    protected String doInBackground(String... params) {
        try {
            Log.d(debugTag,"Background:" + Thread.currentThread().getName());
            String result = helper.downloadFromServer(params);
            return result;
        } catch (Exception e) {
            return new String();
        }
    }//background;call the helper to download from the server

//    @Override
//    protected void onPostExecute(String result) //parse string
//    {
//
//        //ArrayList<TrackData> trackdata = new ArrayList<TrackData>();
//
//        progDialog.dismiss();//get the dialog off the screen
//        if (result.length() == 0) {
//            this.activity.alert ("Unable to find track data. Try again later.");
//            return;
//        }
//
//        try {
//            JSONObject respObj = new JSONObject(result);
//            JSONObject topTracksObj = respObj.getJSONObject("toptracks");
//            JSONArray tracks = topTracksObj.getJSONArray("track");
//            for(int i=0; i<tracks.length(); i++) {
//                JSONObject track = tracks.getJSONObject(i);
//                String trackName = track.getString("name");
//                String trackUrl = track.getString("url");
//                JSONObject artistObj = track.getJSONObject("artist");
//                String artistName = artistObj.getString("name");
//                String artistUrl = artistObj.getString("url");
//                String imageUrl;
//                try {
//                    JSONArray imageUrls = track.getJSONArray("image");
//                    imageUrl = null;
//                    for(int j=0; j<imageUrls.length(); j++) {
//                        JSONObject imageObj = imageUrls.getJSONObject(j);
//                        imageUrl = imageObj.getString("#text");
//                        if(imageObj.getString("size").equals("medium")) {
//                            break;
//                        }
//                    }
//                } catch (Exception e) {
//                    imageUrl = null;
//                }
//
//                trackdata.add(new TrackData(trackName,artistName,imageUrl, artistUrl, trackUrl));//see the TrackData class
//            }
//
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        this.activity.setTracks(trackdata);
//
//    }
}