package com.kabromtech.azulbenv3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.io.InputStream;

public class KnowMoreSyndicateActivity extends AppCompatActivity {
    private static int ID_SYNDICATE;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_more_syndicate);

        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("AzulBenPrefs", 0);
        String title = sharedPrefs.getString("azulBenSyndName", null);
        String subTitle = sharedPrefs.getString("azulBenSyndInitial", null);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(title);
        ab.setSubtitle(subTitle);
        ab.setDisplayHomeAsUpEnabled(true);

        ID_SYNDICATE = sharedPrefs.getInt("azulBenSyndID", 0);

        mRequestQueue = Volley.newRequestQueue(this);
        parseOneJSON();

        /*Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        ID_SYNDICATE = extras.getInt("ID_SYNDICATE");*/
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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
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

                            final String feedImageUrl = syndicate.getString("image");
                            ProportionalImageView imgSynd = (ProportionalImageView)findViewById(R.id.image_view_synd);
                            if (feedImageUrl.trim().isEmpty() || feedImageUrl == "") {
                                imgSynd.setVisibility(View.GONE);
                            }else{
                                //Picasso.get().load(feedImageUrl).fit().centerInside().into(imgSynd);
                                //imgSynd.setImageResource(imgSynd);
                                new DownloadImageTask(imgSynd).execute(feedImageUrl);
                            }

                            final String feedShortDesc = syndicate.getString("shortDescription");
                            TextView shortDesc = (TextView)findViewById(R.id.text_view_short_desc);
                            if (feedShortDesc.trim().isEmpty() || feedShortDesc == "") {
                                shortDesc.setVisibility(View.GONE);
                            }else{
                                shortDesc.setText(feedShortDesc);
                            }

                            final String feedDesc = syndicate.getString("description");
                            TextView desc = (TextView)findViewById(R.id.text_view_desc);
                            /*if (feedDesc.trim().isEmpty() || feedDesc == "") {
                                desc.setVisibility(View.GONE);
                            }else{*/
                                desc.setText(feedDesc);
                            //}

                            ImageButton facebook = (ImageButton)findViewById(R.id.btn_face);
                            Button whatsapp = (Button)findViewById(R.id.btn_whats);
                            ImageButton instagram = (ImageButton)findViewById(R.id.btn_insta);
                            ImageButton youtube = (ImageButton)findViewById(R.id.btn_yout);
                            ImageButton twitter = (ImageButton)findViewById(R.id.btn_twit);
                            Button cellphone = (Button)findViewById(R.id.btn_cellphone);
                            Button phone = (Button)findViewById(R.id.btn_phone);
                            Button locale = (Button)findViewById(R.id.btn_locale);

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

                            final String feedFacebook = syndicate.getString("facebook");
                            if (feedFacebook.trim().isEmpty() || feedFacebook == "") {
                                facebook.setVisibility(View.GONE);
                            }else{
                                facebook.setVisibility(View.VISIBLE);
                                facebook.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        openFacebook(feedFacebook);
                                    }
                                });
                            }

                            final String feedYoutube = syndicate.getString("youtube");
                            if (feedYoutube.trim().isEmpty() || feedYoutube == "") {
                                youtube.setVisibility(View.GONE);
                            }else{
                                youtube.setVisibility(View.VISIBLE);
                                youtube.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        openYoutube(feedYoutube);
                                    }
                                });
                            }

                            final String feedInstagram = syndicate.getString("instagram");
                            if (feedInstagram.trim().isEmpty() || feedInstagram == "") {
                                instagram.setVisibility(View.GONE);
                            }else{
                                instagram.setVisibility(View.VISIBLE);
                                instagram.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        openInstagram(feedInstagram);
                                    }
                                });
                            }

                            final String feedTwitter = syndicate.getString("twitter");
                            if (feedTwitter.trim().isEmpty() || feedTwitter == "") {
                                twitter.setVisibility(View.GONE);
                            }else{
                                twitter.setVisibility(View.VISIBLE);
                                twitter.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        openTwitter(feedTwitter);
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

    public void openInstagram(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this,"Falha ao abrir o instagram",Toast.LENGTH_LONG).show();
        }
    }

    public void openFacebook(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this,"Falha ao abrir o facebook",Toast.LENGTH_LONG).show();
        }
    }

    public void openYoutube(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this,"Falha ao abrir o youtube",Toast.LENGTH_LONG).show();
        }
    }

    public void openTwitter(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this,"Falha ao abrir o twitter",Toast.LENGTH_LONG).show();
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
            Toast.makeText(KnowMoreSyndicateActivity.this,"Aparentemente você não tem o Whatsapp",Toast.LENGTH_LONG).show();

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
}
