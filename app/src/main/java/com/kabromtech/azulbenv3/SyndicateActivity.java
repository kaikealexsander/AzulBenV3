package com.kabromtech.azulbenv3;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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

public class SyndicateActivity extends AppCompatActivity implements AdapterSyndicate.OnItemClickListener {
    private RecyclerView mRecycleView;
    private AdapterSyndicate mAdapterSyndicate;
    private ArrayList<ItemSyndicate> mSyndicateList;
    private RequestQueue mRequestQueue;

    public static final String EXTRA_ID_WORKING_AREA = "ID_WORKING_AREA";
    public static final String EXTRA_NAME_WORKING_AREA = "NAME_WORKING_AREA";
    public static final String EXTRA_IMAGE_WORKING_AREA = "IMAGE_WORKING_AREA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syndicate);

        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("AzulBenPrefs", 0);

        /*SharedPreferences.Editor ed = sharedPrefs.edit();
        ed.clear();
        ed.commit();*/

        ActionBar ab = getSupportActionBar();

        //SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!sharedPrefs.contains("azulBenCityID")){

            ab.setTitle(getString(R.string.app_name));
            ab.setSubtitle("Escolha sua cidade");

            Toast toast = Toast.makeText(this, "Primeiro acesso?\n\nEscolha a Cidade.", Toast.LENGTH_LONG);
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            if( v != null) v.setGravity(Gravity.CENTER);
            toast.show();

            startActivity(new Intent(getBaseContext(), CityActivity.class));
            finish();

        }else{
            String title = getString(R.string.syndicate);
            String subTitle = sharedPrefs.getString("azulBenCityName", null) + " - " + sharedPrefs.getString("azulBenCityFkState", null);

            ab.setTitle(title);
            ab.setSubtitle(subTitle);

            mRecycleView = findViewById(R.id.recycler_syndicate);
            mRecycleView.setHasFixedSize(true);
            mRecycleView.setLayoutManager(new LinearLayoutManager(this));

            mSyndicateList = new ArrayList<>();

            mRequestQueue = Volley.newRequestQueue(this);
            parseJSON();
        }
    }

    private void parseJSON(){
        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("AzulBenPrefs", 0);
        String url = "https://rededaeconomia.com.br/sindicatos?init=0&limit=70&city=" + sharedPrefs.getString("azulBenCityName", null) + "&state=" + sharedPrefs.getString("azulBenCityFkState", null);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("syndicates");

                            for(int i = 0; i < jsonArray.length(); i ++){
                                JSONObject syndicate = jsonArray.getJSONObject(i);

                                int ID = syndicate.getInt("ID");
                                String imageUrl = syndicate.getString("image");
                                String nameSyndicate = syndicate.getString("name");
                                String descriptionSyndicate = syndicate.getString("description");

                                mSyndicateList.add(new ItemSyndicate(ID, nameSyndicate, descriptionSyndicate, imageUrl));
                            }

                            mAdapterSyndicate = new AdapterSyndicate(SyndicateActivity.this, mSyndicateList);
                            mRecycleView.setAdapter(mAdapterSyndicate);

                            mAdapterSyndicate.setOnItemClickListener(SyndicateActivity.this);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    public void onItemClick(int position) {
        ItemSyndicate clickedItem = mSyndicateList.get(position);

        Intent workingAreaIntent = new Intent(getBaseContext(), WorkingAreaActivity.class);
        workingAreaIntent.putExtra(EXTRA_ID_WORKING_AREA, clickedItem.getID());
        workingAreaIntent.putExtra(EXTRA_NAME_WORKING_AREA, clickedItem.getName());
        workingAreaIntent.putExtra(EXTRA_IMAGE_WORKING_AREA, clickedItem.getPicture());

        startActivity(workingAreaIntent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.location:
                startActivity(new Intent(getBaseContext(), CityActivity.class));
                finish();
                return true;
            case R.id.syndicate:
                startActivity(new Intent(getBaseContext(), SyndicateActivity.class));
                finish();
                return true;
            case R.id.about:
                startActivity(new Intent(getBaseContext(), SplashActivity.class));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}
