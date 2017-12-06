package com.dmb.pruebapi;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecentMatches extends AppCompatActivity {

    private ProgressDialog dialog;
    private String selectedRegion,accountID,championID,gameID,lane,champImg,champName,matchResult,kills,deaths,assists,checkChampID;
    private ImageView champIcon;
    private TextView tvChampName,tvMatchScore,tvMatchResult,tvMatchLane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_view_matches);

        champIcon = (ImageView) findViewById(R.id.champIcon);
        tvChampName = (TextView)findViewById(R.id.champName);
        tvMatchScore = (TextView)findViewById(R.id.matchScore);
        tvMatchResult = (TextView)findViewById(R.id.matchResult);
        tvMatchLane = (TextView)findViewById(R.id.matchLane);

        retrieveData();
        requestLastMatches();
    }

    public void retrieveData() {
        SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        selectedRegion = preferences.getString("selectedRegion", "selectedRegion");
        accountID = preferences.getString("accountID", "accountID");
    }

    public void requestLastMatches(){
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        String url = "https://"+selectedRegion+".api.riotgames.com/lol/match/v3/matchlists/by-account/"+accountID+"/recent?api_key="+MainActivity.apiKey;

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseLastMatches(string);
                requestChampByID();
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "No se ha podido recuperar la informacion", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(RecentMatches.this);
        rQueue.add(request);
    }

    public void parseLastMatches(String jsonString){

        try{
            JSONObject object = new JSONObject(jsonString);
            JSONArray array = object.getJSONArray("matches");
                JSONObject obj = array.getJSONObject(0);
                championID = obj.getString("champion");
                gameID = obj.getString("gameId");
                lane = obj.getString("lane");
                tvMatchLane.setText(lane);

        }catch (JSONException e){
            e.printStackTrace();
        }
        dialog.dismiss();
    }

    public void requestChampByID(){
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        String url = "https://"+selectedRegion+".api.riotgames.com/lol/static-data/v3/champions/"+championID+"?locale=en_US&tags=image&api_key="+MainActivity.apiKey;

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseChampID(string);
                requestMatchInfo();
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "No se ha podido recuperar la informacion", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(RecentMatches.this);
        rQueue.add(request);
    }

    public void parseChampID(String jsonString){

        try{
            JSONObject object = new JSONObject(jsonString);
            champImg = object.getJSONObject("image").optString("full");
            champName = object.optString("name");
            tvChampName.setText(champName);
            Picasso.with(getApplicationContext()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/champion/"+champImg).into(champIcon);
        }catch (JSONException e){
            e.printStackTrace();
        }

        dialog.dismiss();
    }
    
    public void requestMatchInfo(){
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        String url = "https://"+selectedRegion+".api.riotgames.com/lol/match/v3/matches/"+gameID+"?api_key="+MainActivity.apiKey;

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseMatchInfo(string);
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "No se ha podido recuperar la informacion", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(RecentMatches.this);
        rQueue.add(request);
    }

    public void parseMatchInfo(String jsonString){
        try{
            JSONObject object = new JSONObject(jsonString);
            JSONArray array = object.getJSONArray("teams");

                JSONObject obj = array.getJSONObject(0);
                matchResult = obj.optString("win");

                if (matchResult.equals("Fail")){
                    tvMatchResult.setTextColor(Color.YELLOW);
                    tvMatchResult.setText("LOSS");
                }else {
                    tvMatchResult.setTextColor(Color.CYAN);
                    tvMatchResult.setText("WIN");
                }

                JSONArray array1 = object.getJSONArray("participants");

                for(int i=0;i<array1.length();i++){
                    JSONObject obj1 = array1.getJSONObject(i);
                    checkChampID = obj1.optString("championId");
                    if(checkChampID.equals(championID)){
                        JSONObject object1 = obj1.getJSONObject("stats");
                        kills = object1.optString("kills");
                        deaths = object1.optString("deaths");
                        assists = object1.optString("assists");
                    }
                }
                tvMatchScore.setText(kills+"/"+deaths+"/"+assists);
        }catch (JSONException e){
            e.printStackTrace();
        }

    }
}
