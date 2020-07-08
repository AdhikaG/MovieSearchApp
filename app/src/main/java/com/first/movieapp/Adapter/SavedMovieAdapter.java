package com.first.movieapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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

public class SavedMovieAdapter extends RecyclerView.Adapter<SavedMovieAdapter.MyViewHolder> {


    private Context dContext;
    private ArrayList<Movie> MovieList;
    RequestOptions loadOption;
    MovieDBHelper DBHelper;
    Cursor cursorSV;


    public SavedMovieAdapter(Context dContext ) {

        this.dContext = dContext;
        loadOption = new RequestOptions().centerCrop().placeholder(R.drawable.loading).error(R.drawable.loading);

    }

    @NonNull
    @Override
    public SavedMovieAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater dInflater = LayoutInflater.from(dContext);
        view = dInflater.inflate(R.layout.movie_card,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull SavedMovieAdapter.MyViewHolder holder, final int position) {

        DBHelper = new MovieDBHelper(dContext);
//        if(!cursorSV.moveToPosition(position)){
//
//            return;
//        }
        Movie movie = RefreshDel.TempDel.get(position);

        final String IMDB = movie.getImdbid() ;
        final String Title = movie.getTitle() ;
        final String Year = movie.getYear() ;
        final String Image = movie.getThumbnail() ;

        holder.tv_savedTitle.setText(Title);
        Glide.with(dContext).load(Image).apply(loadOption).into(holder.iv_SavedThumbnail);


        holder.cardSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MovieList = new ArrayList<>();
                MovieList.clear();
                Movie movie = new Movie(Title,Year,IMDB,Image);
                MovieList.add(movie);

                Intent intent = new Intent(dContext, Movie_Activity.class);
                intent.putExtra("MovieTitle", MovieList.get(0).getTitle());
                intent.putExtra("Year", MovieList.get(0).getYear());
                intent.putExtra("IMDB_ID", MovieList.get(0).getImdbid());
                intent.putExtra("Thumbnail", MovieList.get(0).getThumbnail());
                dContext.startActivity(intent);

            }
        });


        holder.cardSave.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                CharSequence[] save = {"Delete Movie"};
                AlertDialog.Builder builder = new AlertDialog.Builder(dContext);

                builder.setItems(save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DBHelper.onDelete(IMDB);
                        RefreshDel.TempDel.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(dContext, "Movie Deleted", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(dContext, SavedM_Activity.class);
//                        dContext.startActivity(intent);

                    }
                });
                builder.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {

        return RefreshDel.TempDel.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_savedTitle;
        ImageView iv_SavedThumbnail;
        CardView cardSave;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_savedTitle = itemView.findViewById(R.id.mv_title);
            iv_SavedThumbnail = itemView.findViewById (R.id.mv_image);
            cardSave = itemView.findViewById(R.id.card_view);
//            cardSave.setOnCreateContextMenuListener(this);
        }

//        @Override
//        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//
//            menu.add(this.getAdapterPosition(),1 ,0 , "Delete");
//
//        }
    }
}
