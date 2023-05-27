package com.kabromtech.azulbenv3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WorkingAreaActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener , AdapterWorkingArea.OnItemClickListener  {
    private boolean isHideToolbarView = false;
    HeaderView toolbarHeaderView;
    HeaderView floatHeaderView;
    AppBarLayout appBarLayout;

    private RecyclerView mRecycleView;
    private AdapterWorkingArea mAdapter;
    private ArrayList<ItemWorkingArea> mList;
    private RequestQueue mRequestQueue;

    public static final String EXTRA_ID_WORKING_AREA = "ID_WORKING_AREA";
    public static final String EXTRA_ID_SYNDICATE = "ID_SYNDICATE";
    public static final String EXTRA_NAME_WORKING_AREA = "NAME_WORKING_AREA";
    private static int ID_SYNDICATE;

    FloatingActionMenu floatingActionMenu;
    FloatingActionButton locale, whatsapp, cellphone, phone, moreInfo;

    //float posYActionMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working_area);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        toolbarHeaderView = findViewById(R.id.toolbar_header_view);
        floatHeaderView = findViewById(R.id.float_header_view);

        collapsingToolbarLayout.setTitle(" ");

        appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(this);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        ID_SYNDICATE = extras.getInt("ID_WORKING_AREA");
        String nameWorkingArea = extras.getString("NAME_WORKING_AREA");
        String imageWorkingArea = extras.getString("IMAGE_WORKING_AREA");
        String title = getString(R.string.workingarea);

        ImageView mImageView = findViewById(R.id.image_workingarea);
        Picasso.get().load(imageWorkingArea).fit().centerInside().into(mImageView);

        toolbarHeaderView.bindTo(title, nameWorkingArea);
        floatHeaderView.bindTo(title, nameWorkingArea);

        floatingActionMenu = (FloatingActionMenu)findViewById(R.id.fab);
        //posYActionMenu = floatingActionMenu.getTranslationY();
        //posYActionMenu = floatingActionMenu.getY();
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        mRequestQueue = Volley.newRequestQueue(this);
        parseOneJSON();

        mRecycleView = findViewById(R.id.recycler_workingarea);
        mRecycleView.setHasFixedSize(true);

        //mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mRecycleView.setLayoutManager(new GridLayoutManager(this, 2));

        mList = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(this);
        parseJSON();
    }

    private void parseJSON(){
        String url = "https://rededaeconomia.com.br/ramos-atuacao?syndicate=" + ID_SYNDICATE + "&init=0&limit=70";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("workingarea");

                            for(int i = 0; i < jsonArray.length(); i ++){
                                JSONObject syndicate = jsonArray.getJSONObject(i);

                                int ID = syndicate.getInt("ID");
                                String imageUrl = syndicate.getString("image");
                                String nameSyndicate = syndicate.getString("name");

                                mList.add(new ItemWorkingArea(ID, nameSyndicate, imageUrl));
                            }

                            mAdapter = new AdapterWorkingArea(WorkingAreaActivity.this, mList);
                            mRecycleView.setAdapter(mAdapter);

                            mAdapter.setOnItemClickListener(WorkingAreaActivity.this);

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {

        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        //float position = posYActionMenu - (posYActionMenu * percentage);

        //float position = (float) maxScroll - (float) Math.abs(offset) + 90;

        float appBarLayoutHeight = appBarLayout.getHeight();
        float appBarLayoutHeightResize = (float) (appBarLayoutHeight - (appBarLayoutHeight * 0.15));


        float position =  appBarLayoutHeightResize - (float) Math.abs(offset);

        /*Log.d("OPAA",Float.toString((float) Math.abs(offset)) + " -- " +
                Float.toString((float) maxScroll) + " -- " +
                Float.toString((float) percentage) + " -- " +
                Float.toString((float) position));*/

        floatingActionMenu.setY( position );

        if (percentage == 1f && isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;

            floatingActionMenu.setVisibility(View.GONE);

        } else if (percentage < 1f && !isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;

            floatingActionMenu.setVisibility(View.VISIBLE);
        }
    }

    public void onItemClick(int position) {
        ItemWorkingArea clickedItem = mList.get(position);

        Intent companyIntent = new Intent(getBaseContext(), CompanyActivity.class);
        companyIntent.putExtra(EXTRA_ID_SYNDICATE, ID_SYNDICATE);
        companyIntent.putExtra(EXTRA_ID_WORKING_AREA, clickedItem.getID());
        companyIntent.putExtra(EXTRA_NAME_WORKING_AREA, clickedItem.getName());
        startActivity(companyIntent);

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
    public void openWhatsApp(View view, String number){
        PackageManager pm = getPackageManager();
        try {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+ number +"&text="+ ""));
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(WorkingAreaActivity.this,"Aparentemente você não tem o Whatsapp",Toast.LENGTH_LONG).show();

        }
    }

    public void openCall(View view, String number){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" +number));
        startActivity(intent);
    }

    public void openMaps(View view, String latitude, String longitude){
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr=" + latitude+ "," + longitude ));
        startActivity(intent);
    }

    private void parseOneJSON(){
        String url = "https://rededaeconomia.com.br/sindicato/" + ID_SYNDICATE;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("syndicate");

                            JSONObject syndicate = jsonArray.getJSONObject(0);

                            //floatingActionMenu = (FloatingActionMenu)findViewById(R.id.fab);
                            locale = (FloatingActionButton)findViewById(R.id.locale);
                            moreInfo = (FloatingActionButton)findViewById(R.id.about);
                            whatsapp = (FloatingActionButton)findViewById(R.id.whatsapp);
                            cellphone = (FloatingActionButton)findViewById(R.id.cellphone);
                            phone = (FloatingActionButton)findViewById(R.id.phone);


                            final String feedLatitude = syndicate.getString("latitude");
                            final String feedLongitude = syndicate.getString("longitude");
                            if (feedLatitude.trim().isEmpty() || feedLatitude == "" || feedLongitude.trim().isEmpty() || feedLongitude == "") {
                                locale.setVisibility(View.GONE);
                            }else{
                                locale.setVisibility(View.VISIBLE);
                                locale.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        openMaps(v, feedLatitude, feedLongitude);
                                    }
                                });
                            }

                            final String feedWhatsapp = syndicate.getString("whatsapp");
                            if (feedWhatsapp.trim().isEmpty() || feedWhatsapp == "") {
                                whatsapp.setVisibility(View.GONE);
                            }else{
                                whatsapp.setVisibility(View.VISIBLE);
                                whatsapp.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        openWhatsApp(v, "55" + feedWhatsapp);
                                    }
                                });
                            }

                            final String feedCellphone = syndicate.getString("cellphone");
                            if (feedCellphone.trim().isEmpty() || feedCellphone == "") {
                                cellphone.setVisibility(View.GONE);
                            }else{
                                cellphone.setVisibility(View.VISIBLE);
                                cellphone.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    openCall(v, feedCellphone);
                                    }
                                });
                            }

                            final String feedPhone = syndicate.getString("phone");
                            if (feedPhone.trim().isEmpty() || feedPhone == "") {
                                phone.setVisibility(View.GONE);
                            }else{
                                phone.setVisibility(View.VISIBLE);
                                phone.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    openCall(v, feedPhone);
                                    }
                                });
                            }

                            final String feedName = syndicate.getString("name");
                            final String feedInitial = syndicate.getString("initials");
                            moreInfo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("AzulBenPrefs", 0);
                                    SharedPreferences.Editor editor = sharedPrefs.edit();

                                    editor.putInt("azulBenSyndID", ID_SYNDICATE);
                                    editor.putString("azulBenSyndName", feedName);
                                    editor.putString("azulBenSyndInitial", feedInitial);

                                    editor.commit();

                                    startActivity(new Intent(getBaseContext(), KnowMoreSyndicateActivity.class));
                                }
                            });

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
}
