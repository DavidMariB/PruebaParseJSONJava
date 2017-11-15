package com.dmb.pruebapi;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    private TextView tv1,tv2,tv3,tv4;
    private EditText et1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tv1 = (TextView)findViewById(R.id.tv1);
        tv2 = (TextView)findViewById(R.id.tv2);
        tv3 = (TextView)findViewById(R.id.tv3);
        tv4 = (TextView)findViewById(R.id.tv4);
        et1 = (EditText)findViewById(R.id.et1);

    }

    public void requestJsonData(View v){

        dialog = new ProgressDialog(this);
        dialog.setMessage("Cargando...");
        dialog.show();

        String url = "https://euw1.api.riotgames.com/lol/summoner/v3/summoners/by-name/"+et1.getText().toString()+"?api_key=RGAPI-e6227e38-5d2d-4897-a068-b3d0ebf86018";


        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseJsonData(string);
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

    public void parseJsonData(String jsonString) {

        try {
            JSONObject object = new JSONObject(jsonString);
            String name = object.optString("name");
            String level = object.optString("summonerLevel");
            tv3.setText(name);
            tv4.setText(level);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.dismiss();
    }
}