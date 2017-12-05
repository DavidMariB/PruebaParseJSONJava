package com.dmb.pruebapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    private TextView summonerName,summonerTier,summonerLevel;
    private EditText reqSummonerName;
    private String champName,champKey,champImg,name,level,summonerID,accountID,tier,rank,profileIcon,selectedRegion;
    private CardView summonerInfoCard;
    private ImageView summonerTierIcon,summonerProfileIcon;
    private Spinner selectRegion;
    private Button recentMatches;
    public static ArrayList<Champion> champ;
    public static String apiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiKey = "RGAPI-aac0cf53-f7ac-4f6e-b326-a498570717ac";

        requestAllChamps();

        summonerName = (TextView)findViewById(R.id.tvSummonerName);
        summonerTier = (TextView)findViewById(R.id.tvSummonerTier);
        summonerLevel = (TextView)findViewById(R.id.tvSummonerLevel);
        reqSummonerName = (EditText)findViewById(R.id.requestSummonerName);
        summonerInfoCard = (CardView)findViewById(R.id.summonerInfoCard);
        summonerTierIcon = (ImageView)findViewById(R.id.imgSummonerTier);
        summonerProfileIcon = (ImageView)findViewById(R.id.imgSummonerIcon);
        selectRegion = (Spinner)findViewById(R.id.selectRegion);
        recentMatches = (Button)findViewById(R.id.recentMatches);

        selectRegion();
    }

    public void requestAllChamps(){
        dialog = new ProgressDialog(this);
        dialog.setMessage("Cargando...");
        dialog.show();

        String url = "http://ddragon.leagueoflegends.com/cdn/7.23.1/data/en_US/champion.json";
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseAllChamps(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "No se ha encontrado el Invocador", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(request);
    }

    public void parseAllChamps(String jsonString){
        champ = new ArrayList<>();
        try{

            JSONObject obj = new JSONObject(jsonString);
            JSONObject object = obj.getJSONObject("data");
            Iterator<String> it = object.keys();
            while(it.hasNext()){
                JSONObject champion = (JSONObject) object.get(it.next());
                champName = champion.optString("name");
                champKey = champion.optString("key");
                champImg = champion.optString("full");
                Champion ch = new Champion(champName,champKey,champImg);
                champ.add(ch);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
        dialog.dismiss();
    }

    public void selectRegion(){
        String[] region = {"EUW","NA","LAN","LAS"};
        selectRegion.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, region));

        selectRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                selectedRegion = parent.getItemAtPosition(position).toString();
                switch (selectedRegion){
                    case "EUW":
                        selectedRegion="euw1";
                        break;
                    case "NA":
                        selectedRegion="na1";
                        break;
                    case "LAN":
                        selectedRegion="la1";
                        break;
                    case "LAS":
                        selectedRegion="la2";
                        break;
                    default:
                        System.out.print("Lo mismo no funciona");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void requestSummonerInfo(View v) {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Cargando...");
        dialog.show();

        String url = "https://"+selectedRegion+".api.riotgames.com/lol/summoner/v3/summoners/by-name/"+reqSummonerName.getText().toString().replace(" ","")+"?api_key="+apiKey;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseSummonerInfo(string);
                requestSummonerLeague();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "No se ha encontrado el Invocador", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(request);

    }

    public void parseSummonerInfo(String jsonString) {

        try {
            JSONObject object = new JSONObject(jsonString);
            name = object.optString("name");
            level = object.optString("summonerLevel");
            summonerID = object.optString("id");
            accountID = object.optString("accountId");
            profileIcon = object.optString("profileIconId");
            summonerInfoCard.setVisibility(View.VISIBLE);
            summonerName.setText(name);
            summonerLevel.setText("Nivel: "+level);
            Picasso.with(getApplicationContext()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/profileicon/"+profileIcon+".png").into(summonerProfileIcon);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.dismiss();
    }

    public void requestSummonerLeague(){
        dialog = new ProgressDialog(this);
        dialog.setMessage("Cargando...");
        dialog.show();

        String url = "https://"+selectedRegion+".api.riotgames.com/lol/league/v3/positions/by-summoner/"+summonerID+"?api_key="+apiKey;


        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                if(string.length()>2){
                    parseSummonerLeague(string);

                }else{
                    summonerTier.setText("Unranked");
                    summonerTierIcon.setImageResource(R.drawable.unranked_icon);
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "No se ha podido recuperar la informacion", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(request);
    }

    public void parseSummonerLeague(String jsonString){

        try{
            JSONArray arr = new JSONArray(jsonString);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                if(obj.getString("queueType").contains("SOLO") && obj != null){
                    tier = obj.getString("tier");
                    rank = obj.getString("rank");
                    summonerTier.setText(tier+" "+rank);

                    if(obj.getString("tier").contains("BRONZE")){
                        summonerTierIcon.setImageResource(R.drawable.bronze_icon);
                    }else if(obj.getString("tier").contains("SILVER")){
                        summonerTierIcon.setImageResource(R.drawable.silver_icon);
                    }else if(obj.getString("tier").contains("GOLD")){
                        summonerTierIcon.setImageResource(R.drawable.gold_icon);
                    }else if(obj.getString("tier").contains("PLATINUM")){
                        summonerTierIcon.setImageResource(R.drawable.platinum_icon);
                    }else if(obj.getString("tier").contains("DIAMOND")){
                        summonerTierIcon.setImageResource(R.drawable.diamond_icon);
                    }else if(obj.getString("tier").contains("MASTER")){
                        summonerTierIcon.setImageResource(R.drawable.master_icon);
                    }else if(obj.getString("tier").contains("CHALLENGER")){
                        summonerTierIcon.setImageResource(R.drawable.challenger_icon);
                    }
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        dialog.dismiss();

        recentMatches.setVisibility(View.VISIBLE);
    }

    public void recentMatchesActivity(View v){
        SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("selectedRegion",selectedRegion);
        edit.putString("accountID",accountID);
        edit.apply();
        Intent intent = new Intent(this,RecentMatches.class);
        startActivityForResult(intent,1);
    }
}