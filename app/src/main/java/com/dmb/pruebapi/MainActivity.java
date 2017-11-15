package com.dmb.pruebapi;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    private TextView tv3,tv4,tv5,tv6;
    private EditText et1;
    private String name,level,id,tier,rank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv3 = (TextView)findViewById(R.id.tv3);
        tv4 = (TextView)findViewById(R.id.tv4);
        tv5 = (TextView)findViewById(R.id.tv5);
        tv6 = (TextView)findViewById(R.id.tv6);
        et1 = (EditText)findViewById(R.id.et1);

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
            tv3.setText(name);
            tv4.setText(level);
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
                if(obj.getString("queueType").indexOf("SOLO")>-1){
                        tier = obj.getString("tier");
                        rank = obj.getString("rank");
                        tv6.setText(tier+" "+rank);
                }

            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        dialog.dismiss();
    }
}