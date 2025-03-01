package com.example.myapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.MovieDetails;
import com.example.myapplication.fragment.Movie_Manager_Fragment;
import com.example.myapplication.model.Movie;

import java.util.List;

public class MovieManagerAdapter extends RecyclerView.Adapter<MovieManagerAdapter.ViewHolder> {
    List<Movie> movieList;
    Context context;

    public MovieManagerAdapter(Context context,  List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_manager, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Movie movie = movieList.get(position);
        if(movie == null){
            return;
        }
        holder.name.setText(movie.getTitle());
        holder.price.setText(String.valueOf(movie.getPrice()));
        holder.director.setText(movie.getDirector());
        holder.duration.setText(movie.getDuration());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = movieList.get(position).getId();
                if(!id.equals("")){
                    Movie_Manager_Fragment.clickMoviePosition = id;
                    goToDetail(movieList.get(position));
                }
            }
        });
        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                Movie_Manager_Fragment.clickMoviePosition = movieList.get(position).getId();
                MenuInflater inflater = new MenuInflater(v.getContext());
                inflater.inflate(R.menu.menu_context_account, menu);
            }
        });
    }

    private void goToDetail(Movie movie) {
        Intent intent = new Intent(context, MovieDetails.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Object", movie);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if(movieList!=null){
            return movieList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, price, director, duration;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView4);
            price = itemView.findViewById(R.id.textView5);
            director =itemView.findViewById(R.id.textView6);
            duration = itemView.findViewById(R.id.tvduration);
        }
    }
}
