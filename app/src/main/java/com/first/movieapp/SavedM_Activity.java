package com.first.movieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.first.movieapp.Adapter.RefreshDel;
import com.first.movieapp.Adapter.SavedMovieAdapter;
import com.first.movieapp.Database.Movie;
import com.first.movieapp.Database.MovieDBHelper;

public class SavedM_Activity extends AppCompatActivity {

    SavedMovieAdapter svAdapter;
    RecyclerView svRecycler;
    TextView svTitle;
    ImageView svThumbnail;
    MovieDBHelper DBHelper = new MovieDBHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_movies);

        Button btn_searchMovies2 = findViewById(R.id.btn_searchSA);
        svRecycler = findViewById(R.id.rv_recyclerviewSV);


        RefreshDel.TempDel.clear();
        getMovieRefresh();

        svRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        svAdapter = new SavedMovieAdapter(this);
        svAdapter.notifyDataSetChanged();
        svRecycler.setAdapter(svAdapter);


        btn_searchMovies2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentSearchA = new Intent(SavedM_Activity.this,MainActivity.class);
                startActivity(intentSearchA);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        svRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        svAdapter = new SavedMovieAdapter(this);
        svAdapter.notifyDataSetChanged();
        svRecycler.setAdapter(svAdapter);

    }

    public void getMovieRefresh(){

        Cursor cursorDel = DBHelper.retrieveData();
        while (cursorDel.moveToNext()) {

            String bTitle = cursorDel.getString(cursorDel.getColumnIndex(DBHelper.FIELD_MOVIE_TITLE));
            String bYear = cursorDel.getString(cursorDel.getColumnIndex(DBHelper.FIELD_MOVIE_YEAR));
            String bImdb = cursorDel.getString(cursorDel.getColumnIndex(DBHelper.FIELD_MOVIE_IMDBID));
            String bThumbnail = cursorDel.getString(cursorDel.getColumnIndex(DBHelper.FIELD_MOVIE_THUMBNAIL));

            Movie movie = new Movie(bTitle,bYear,bImdb,bThumbnail);
            RefreshDel.TempDel.add(movie);

        }
    }
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//
//        switch (item.getItemId()){
//
//            case 1:
//                DBHelper.onDelete(item.getGroupId() );
//                svAdapter.notifyDataSetChanged();
//                Toast.makeText(this, "Data Deleted", Toast.LENGTH_SHORT).show();
//
//                default:
//                return super.onContextItemSelected(item);
//        }
//
//    }



}