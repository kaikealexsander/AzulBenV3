package com.kabromtech.azulbenv3;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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

public class CompanyActivity extends AppCompatActivity implements AdapterCompany.OnItemClickListener  {
    private RecyclerView mRecycleView;
    private AdapterCompany mAdapter;
    private ArrayList<ItemCompany> mList;
    private RequestQueue mRequestQueue;

    private static int ID_SYNDICATE;
    private static int ID_WORKING_AREA;

    public static final String EXTRA_ID_COMPANY = "ID_COMPANY";
    public static final String EXTRA_NAME_COMPANY = "NAME_COMPANY";
    public static final String EXTRA_IMAGE_COMPANY = "IMAGE_COMPANY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        ID_SYNDICATE = extras.getInt("ID_SYNDICATE");
        ID_WORKING_AREA = extras.getInt("ID_WORKING_AREA");

        String nameWorkingArea = extras.getString("NAME_WORKING_AREA");

        ActionBar ab = getSupportActionBar();
        ab.setTitle("Empresas");
        ab.setSubtitle(nameWorkingArea);

        ab.setDisplayHomeAsUpEnabled(true);

        mRecycleView = findViewById(R.id.recycler_company);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mList = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(this);
        parseJSON();
    }

    private void parseJSON(){
        String url = "https://rededaeconomia.com.br/empresas?init=0&limit=70&syndicate=" + ID_SYNDICATE + "&workingArea=" + ID_WORKING_AREA;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("company");

                            for(int i = 0; i < jsonArray.length(); i ++){
                                JSONObject company = jsonArray.getJSONObject(i);

                                int ID = company.getInt("ID");
                                String imageUrl = company.getString("image");
                                String name = company.getString("name");
                                String description = company.getString("description");

                                mList.add(new ItemCompany(ID, name, description, imageUrl));
                            }

                            mAdapter = new AdapterCompany(CompanyActivity.this, mList);
                            mRecycleView.setAdapter(mAdapter);

                            mAdapter.setOnItemClickListener(CompanyActivity.this);

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

    public void onItemClick(int position) {
        ItemCompany clickedItem = mList.get(position);

        Intent promotionIntent = new Intent(getBaseContext(), PromotionActivity.class);
        promotionIntent.putExtra(EXTRA_ID_COMPANY, clickedItem.getID());
        promotionIntent.putExtra(EXTRA_NAME_COMPANY, clickedItem.getName());
        promotionIntent.putExtra(EXTRA_IMAGE_COMPANY, clickedItem.getPicture());

        startActivity(promotionIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
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
            case android.R.id.home:
                finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}
