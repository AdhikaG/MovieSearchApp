package com.first.movieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.first.movieapp.Adapter.MovieAdapter;
import com.first.movieapp.Database.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText et_SearchTitle;
    Button btn_SearchBar;
    public String JSON_URL;
    CardView cardView;


    private JsonArrayRequest request;
    public ArrayList<Movie> MovieList;
    private RequestQueue reqQueue;
    private RecyclerView rvMovie;
    private MovieAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        rvMovie = findViewById(R.id.rv_recyclerview);
        cardView = findViewById(R.id.card_view);

        MovieList = new ArrayList<>();
        reqQueue = Volley.newRequestQueue(this);

        Button SearchMov = findViewById(R.id.btn_search);
        Button SavedMov = findViewById(R.id.btn_saved);
        et_SearchTitle = findViewById(R.id.et_search);
        btn_SearchBar = findViewById(R.id.btn_searchBar);


        SavedMov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, SavedM_Activity.class);
                startActivity(intent);
            }
        });

        btn_SearchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MovieList.clear();
                parseJSON();

            }
        });

        rvMovie.setHasFixedSize(true);
        rvMovie.setLayoutManager(new GridLayoutManager(this, 3));

    }


    private void parseJSON() {

        String searchMov = et_SearchTitle.getText().toString().trim();
        JSON_URL = "https://www.omdbapi.com/?s=" + searchMov + "&apikey=44399691&";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray jsonArray = response.getJSONArray("Search");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject search = jsonArray.getJSONObject(i);
                        String Title = search.getString("Title");
                        String Year = search.getString("Year");
                        String IMDBID = search.getString("imdbID");
                        String Thumbnail = search.getString("Poster");

                        MovieList.add(new Movie(Title, Year, IMDBID, Thumbnail));
                    }

                    myAdapter = new MovieAdapter(MainActivity.this, MovieList);
                    myAdapter.notifyDataSetChanged();
                    rvMovie.setAdapter(myAdapter);

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

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        reqQueue = new RequestQueue(cache, network);
        reqQueue.start();
        reqQueue.add(request);
    }

    @Override
    protected void onResume() {
        super.onResume();

        rvMovie.setLayoutManager(new GridLayoutManager(this, 3));
        myAdapter = new MovieAdapter(MainActivity.this, MovieList);
        myAdapter.notifyDataSetChanged();
        rvMovie.setAdapter(myAdapter);

    }

}


