package com.dmb.pruebapi;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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


public class MainActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    private TextView tv3,tv6,tv7;
    private EditText et1;
    private String name,level,id,tier,rank,profileIcon,accountID,champID;
    private CardView cv;
    private ImageView img1,img2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv3 = (TextView)findViewById(R.id.tv3);
        tv6 = (TextView)findViewById(R.id.tv6);
        tv7 = (TextView)findViewById(R.id.tv7);
        et1 = (EditText)findViewById(R.id.et1);
        cv = (CardView)findViewById(R.id.card_view);
        img1 = (ImageView)findViewById(R.id.img1);
        img2 = (ImageView)findViewById(R.id.img2);

    }

    public void requestSummonerInfo(View v) {

        dialog = new ProgressDialog(this);
        dialog.setMessage("Cargando...");
        dialog.show();

        String url = "https://euw1.api.riotgames.com/lol/summoner/v3/summoners/by-name/" + et1.getText().toString() + "?api_key=RGAPI-de1e6d05-3e78-43b3-bf17-8929e968ff58";

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseSummonerInfo(string);
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
            id = object.optString("id");
            profileIcon = object.optString("profileIconId");
            accountID = object.optString("accountId");
            cv.setVisibility(View.VISIBLE);
            tv3.setText(name);
            tv7.setText("Nivel: "+level);
            Picasso.with(getApplicationContext()).load("http://ddragon.leagueoflegends.com/cdn/7.22.1/img/profileicon/"+profileIcon+".png").into(img2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.dismiss();
    }

    public void requestSummonerLeague(View v){
        dialog = new ProgressDialog(this);
        dialog.setMessage("Cargando...");
        dialog.show();

        String url = "https://euw1.api.riotgames.com/lol/league/v3/positions/by-summoner/"+id.toString()+"?api_key=RGAPI-de1e6d05-3e78-43b3-bf17-8929e968ff58";

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseSummonerLeague(string);
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
                if(obj.getString("queueType").contains("SOLO")){
                        tier = obj.getString("tier");
                        rank = obj.getString("rank");
                        tv6.setText(tier+" "+rank);
                }

                if(obj.getString("tier").contains("UNRANKED")){
                    img1.setImageResource(R.drawable.unranked_icon);
                }else if(obj.getString("tier").contains("BRONZE")){
                    img1.setImageResource(R.drawable.bronze_icon);
                }else if(obj.getString("tier").contains("SILVER")){
                    img1.setImageResource(R.drawable.silver_icon);
                }else if(obj.getString("tier").contains("GOLD")){
                    img1.setImageResource(R.drawable.gold_icon);
                }else if(obj.getString("tier").contains("PLATINUM")){
                    img1.setImageResource(R.drawable.platinum_icon);
                }else if(obj.getString("tier").contains("DIAMOND")){
                    img1.setImageResource(R.drawable.diamond_icon);
                }else if(obj.getString("tier").contains("MASTER")){
                    img1.setImageResource(R.drawable.master_icon);
                }else if(obj.getString("tier").contains("CHALLENGER")){
                    img1.setImageResource(R.drawable.challenger_icon);
                }

            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        dialog.dismiss();
    }

    public void requestChampionMasteries(View v){

        dialog = new ProgressDialog(this);
        dialog.setMessage("Cargando...");
        dialog.show();

        String url = "https://euw1.api.riotgames.com/lol/league/v3/positions/by-summoner/"+id.toString()+"?api_key=RGAPI-de1e6d05-3e78-43b3-bf17-8929e968ff58";

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseChampionMasteries(string);
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

    public void parseChampionMasteries(String jsonString){

        try{
            JSONArray array = new JSONArray(jsonString);

            for(int i =0;i<array.length();i++){
                JSONObject object = array.getJSONObject(i);
                champID = object.optString("championId");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }


    }
}