package com.first.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.first.movieapp.Database.MovieDBHelper;

import java.util.ArrayList;

public class Movie_Activity extends AppCompatActivity {

    private TextView mvTitle, mvYear, mvIMDBID;
    private ImageView mvImg;
    MovieDBHelper DBHelper = new MovieDBHelper(this);
    ArrayList<String> TempDB;

    String M_Title;
    String M_Year;
    String M_IMDB;
    String M_Image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_);

        TempDB = new ArrayList<>();
        mvTitle = findViewById(R.id.ma_title);
        mvYear = findViewById(R.id.ma_year);
        mvIMDBID = findViewById(R.id.ma_imdbid);
        mvImg = findViewById(R.id.movie_thumbnail);
        final Button save_delete = findViewById(R.id.btn_savedelete);

        Intent intent = getIntent();
        M_Title = intent.getExtras().getString("MovieTitle");
        M_Year = intent.getExtras().getString("Year");
        M_IMDB = intent.getExtras().getString("IMDB_ID");
        M_Image = intent.getExtras().getString("Thumbnail");

        mvTitle.setText("Name: "+ M_Title);
        mvYear.setText("Year: "+ M_Year);
        mvIMDBID.setText("IMDB ID: "+ M_IMDB);
        Glide.with(Movie_Activity.this).load(M_Image).into(mvImg);

        boolean validate = false;
        CursorDB();

        if (TempDB.size() > 0) {

            for (int i = 0; i < TempDB.size(); i++) {

                SQLiteDatabase DB = DBHelper.getReadableDatabase();

                if (M_IMDB.equals(TempDB.get(i))) {

                    validate = true;
                    break;
                }
            }
        }

        if (!validate || TempDB.size() == 0) {

            save_delete.setText("Save");
            save_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SQLiteDatabase DB = DBHelper.getReadableDatabase();
//                    boolean isAdded =
                    DBHelper.onInsert(M_Title, M_Year, M_IMDB, M_Image);
//
//                    if (isAdded == true) {

                        Toast.makeText(Movie_Activity.this, "Movie Saved", Toast.LENGTH_SHORT).show();
                        finish();

                }

            });

        } else if (validate){

            save_delete.setText("Delete");
            save_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DBHelper.onDelete(M_IMDB);
                    //finish();
                    Intent intentSearch = new Intent(Movie_Activity.this,SavedM_Activity.class);
                    startActivity(intentSearch);
                    finish();
                    Toast.makeText(Movie_Activity.this, "Movie Deleted", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }


    public void CursorDB() {

        SQLiteDatabase DB = DBHelper.getReadableDatabase();
        boolean validate = true;
        Cursor cursor = DB.rawQuery("SELECT * FROM " + DBHelper.TABLE_MOVIE, null);
//        cursor.moveToFirst();
        while (cursor.moveToNext()) {
//
//            if (M_Title.equals(cursor.getString(cursor.getColumnIndex(DBHelper.FIELD_MOVIE_TITLE))) && cursor.getString(cursor.getColumnIndex(DBHelper.FIELD_MOVIE_STATUS)).equals("Saved")) {

            String tempID;
            tempID = cursor.getString(cursor.getColumnIndex(DBHelper.FIELD_MOVIE_IMDBID));
            TempDB.add(tempID);
//                validate = false;
//                break;

        }
    }

}



