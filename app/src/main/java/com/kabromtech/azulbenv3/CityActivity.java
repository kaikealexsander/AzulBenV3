package com.kabromtech.azulbenv3;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CityActivity extends AppCompatActivity implements AdapterCity.OnItemClickListener {
    private RecyclerView mRecycleView;
    private AdapterCity mAdapterCity;
    private ArrayList<ItemCity> mCityList;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("Escolha a cidade");

        mRecycleView = findViewById(R.id.recycler_city);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mCityList = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(this);
        parseJSON();

    }

    private void parseJSON(){
        String url = "https://rededaeconomia.com.br/cidades?init=0&limit=70";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("cities");

                            for(int i = 0; i < jsonArray.length(); i ++){
                                JSONObject city = jsonArray.getJSONObject(i);

                                int ID = city.getInt("ID");
                                String nameCity = city.getString("name");
                                String fkState = city.getString("fkState");

                                mCityList.add(new ItemCity(ID, nameCity, fkState));
                            }

                            mAdapterCity = new AdapterCity(CityActivity.this, mCityList);
                            mRecycleView.setAdapter(mAdapterCity);

                            mAdapterCity.setOnItemClickListener(CityActivity.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
    }

    @Override
    public void onItemClick(int position) {
        //Intent detailIntent = new Intent(this, DetailActivity.class);
        ItemCity clickedItem = mCityList.get(position);

        int ID = clickedItem.getID();
        String city = clickedItem.getName();
        String fkState = clickedItem.getFkState();

        //SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("AzulBenPrefs", 0);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putInt("azulBenCityID", ID);
        editor.putString("azulBenCityName", city);
        editor.putString("azulBenCityFkState", fkState);

        editor.commit();

        startActivity(new Intent(getBaseContext(), SyndicateActivity.class));
        finish();
    }
}
