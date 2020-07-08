package com.first.movieapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.first.movieapp.Database.MovieDBHelper;
import com.first.movieapp.Database.Movie;
import com.first.movieapp.Movie_Activity;
import com.first.movieapp.R;


import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    MovieDBHelper DBHelper;
    private Context mContext;
    private ArrayList<Movie> mMovieData;
    RequestOptions loadOption;
    ArrayList<String> TempDB;


    public MovieAdapter(Context mContext, ArrayList<Movie> mMovieData) {
        this.mContext = mContext;
        this.mMovieData = mMovieData;
        loadOption = new RequestOptions().centerCrop().placeholder(R.drawable.loading).error(R.drawable.loading);

    }

    @NonNull
    @Override
    public MovieAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        TempDB = new ArrayList<>();

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.movie_card, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MyViewHolder holder, final int position) {

        DBHelper = new MovieDBHelper(mContext);
        Movie Items = mMovieData.get(position);

        final String aTitle = Items.getTitle();
        final String aThumbnail = Items.getThumbnail();
        final String aYear = Items.getYear();
        final String aIMDB = Items.getImdbid();

        holder.tv_MovieTitle.setText(aTitle);
        Glide.with(mContext).load(aThumbnail).apply(loadOption).into(holder.iv_MovieThumbnail);

        holder.cardMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, Movie_Activity.class);

                intent.putExtra("MovieTitle", mMovieData.get(position).getTitle());
                intent.putExtra("Year", mMovieData.get(position).getYear());
                intent.putExtra("IMDB_ID", mMovieData.get(position).getImdbid());
                intent.putExtra("Thumbnail", mMovieData.get(position).getThumbnail());
                mContext.startActivity(intent);

            }
        });

        boolean validate = false;
        CursorDB();

        if (TempDB.size() > 0) {

            for (int i = 0; i < TempDB.size(); i++) {

                SQLiteDatabase DB = DBHelper.getReadableDatabase();

                if (aIMDB.equals(TempDB.get(i))) {

                    validate = true;
                    break;
                }
            }
        }

        if (!validate || TempDB.size() == 0) {

            holder.cardMovie.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    CharSequence[] save = {"Save Movie"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                    builder.setItems(save, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            SQLiteDatabase Db = DBHelper.getReadableDatabase();
                            boolean isAdded = DBHelper.onInsert(aTitle, aYear, aIMDB, aThumbnail);
                            notifyDataSetChanged();


                            if (isAdded == true) {

                                Toast.makeText(mContext, "Movie Saved", Toast.LENGTH_SHORT).show();

                            } else {

                                Toast.makeText(mContext, "Movie Saved Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.show();
                    return true;
                }
            });

        } else if (validate){

            holder.cardMovie.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    CharSequence[] save = {"Delete Movie"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                    builder.setItems(save, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            DBHelper.onDelete(aIMDB);
                            notifyDataSetChanged();
                            Toast.makeText(mContext, "Movie Deleted", Toast.LENGTH_SHORT).show();
                    }
                    });
                    builder.show();
                    return true;
                }
            });

        }
    }

    @Override
    public int getItemCount() {

        return mMovieData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_MovieTitle;
        ImageView iv_MovieThumbnail;
        CardView cardMovie;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_MovieTitle = itemView.findViewById(R.id.mv_title);
            iv_MovieThumbnail = itemView.findViewById(R.id.mv_image);
            cardMovie = itemView.findViewById(R.id.card_view);
        }
    }

    public void CursorDB() {

        SQLiteDatabase DB = DBHelper.getReadableDatabase();
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
