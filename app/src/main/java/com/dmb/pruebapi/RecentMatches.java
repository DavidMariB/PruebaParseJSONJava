package com.dmb.pruebapi;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import java.util.List;

public class RecentMatches extends AppCompatActivity {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private ProgressDialog dialog;
    private String championID,gameID,lane,champImg,champName;
    private MainActivity main = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_matches);

        createMatchesList();
        requestLastMatches();
    }

    public void createMatchesList(){

        List items = new ArrayList();



        recycler = (RecyclerView) findViewById(R.id.recentMatches);
        recycler.setHasFixedSize(true);

        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        adapter = new RecentMatchesAdapter(items);
        recycler.setAdapter(adapter);
    }

    public void requestLastMatches(){
        dialog = new ProgressDialog(this);
        dialog.setMessage("Cargando...");
        dialog.show();

        String url = "https://"+main.selectedRegion+".api.riotgames.com/lol/match/v3/matchlists/by-account/"+main.accountID+"/recent?api_key=RGAPI-566e7f9f-de68-4912-9061-4a8f404a14cc";

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseLastMatches(string);
                requestChampByID();
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
            JSONArray array = new JSONArray(jsonString);

            for(int i=0;i<array.length();i++){
                JSONObject object = array.getJSONObject(i);
                championID = object.getString("champion");
                gameID = object.getString("gameId");
                lane = object.getString("lane");
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void requestChampByID(){
        dialog = new ProgressDialog(this);
        dialog.setMessage("Cargando...");
        dialog.show();

        String url = "https://"+main.selectedRegion+".api.riotgames.com/lol/static-data/v3/champions/"+championID+"?locale=en_US&tags=image&api_key=RGAPI-566e7f9f-de68-4912-9061-4a8f404a14cc";

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseChampID(string);
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
            champImg = object.optString("full");
            champName = object.optString("name");
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
