package com.first.movieapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.first.movieapp.Movie_Activity;

import java.sql.Blob;

public class MovieDBHelper extends SQLiteOpenHelper {

    SQLiteDatabase Db;
    private static final String DB_NAME = "MovieApp";
    private static final int DB_VERSION = 4;
    public static final String TABLE_MOVIE = "MsMovie";
    public static final String FIELD_MOVIE_TITLE = "movieTitle";
    public static final String FIELD_MOVIE_YEAR = "movieYear";
    public static final String FIELD_MOVIE_IMDBID = "movieImdbid";
    public static final String FIELD_MOVIE_THUMBNAIL = "movieThumbnail";
    public static final String FIELD_MOVIE_STATUS = "movieStatus";

    private static final String CREATE_TABLE_MOVIE = "CREATE TABLE IF NOT EXISTS " + TABLE_MOVIE + "(" +
            FIELD_MOVIE_TITLE + " TEXT, " +
            FIELD_MOVIE_YEAR + " TEXT, " +
            FIELD_MOVIE_IMDBID + " TEXT PRIMARY KEY, " +
            FIELD_MOVIE_THUMBNAIL + " NUMERIC, " +
            FIELD_MOVIE_STATUS + " TEXT );";

    private static final String DROP_TABLE_KOS = "DROP TABLE IF EXISTS " + TABLE_MOVIE + ";";


     public MovieDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_MOVIE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_TABLE_KOS);
        onCreate(db);
    }

//    public boolean onDelete(){
//
//         SQLiteDatabase DB = this.getWritableDatabase();
//         String query = "DELETE FROM" + C
//
//    }
    public boolean onInsert(String M_Title, String M_Year, String M_IMDB, String M_Image) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(this.FIELD_MOVIE_TITLE, M_Title);
        values.put(this.FIELD_MOVIE_YEAR, M_Year);
        values.put(this.FIELD_MOVIE_IMDBID, M_IMDB);
        values.put(this.FIELD_MOVIE_THUMBNAIL, M_Image);
        values.put(this.FIELD_MOVIE_STATUS, "Saved");

        long result = DB.insert(this.TABLE_MOVIE, null, values);

        if(result ==-1){  return  false; }
        else{ return true;}

    }

    public Integer onDelete(String IMDBID) {

        SQLiteDatabase DB = this.getWritableDatabase();

        int result = DB.delete(TABLE_MOVIE, FIELD_MOVIE_IMDBID + "=?", new String[]{IMDBID});
        return result;

    }

    public Cursor retrieveData(){

         SQLiteDatabase db = this.getWritableDatabase();
         Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MOVIE , null);
         return cursor;
    }
}
