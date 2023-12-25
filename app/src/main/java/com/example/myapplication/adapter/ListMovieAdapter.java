package com.example.myapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.activity.List_movie;
import com.example.myapplication.activity.MovieDetails;

import com.example.myapplication.activity.UserMovieDetails;
import com.example.myapplication.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListMovieAdapter extends RecyclerView.Adapter<ListMovieAdapter.ViewHolder>{
    List<Movie> movieList;
    Context context;


    public ListMovieAdapter(List<Movie> movieList, Context context) {
        this.movieList = movieList;
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listmovie_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Picasso.with(context)
                .load(movieList.get(position).getImage())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imageView);
        holder.name.setText(movieList.get(position).getTitle());
        holder.price.setText(String.valueOf(movieList.get(position).getPrice()));
        holder.genre.setText(movieList.get(position).getGenre());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = movieList.get(position).getId();
                if(!id.equals("")){
                    List_movie.clickMoviePosition = id;
                    goToDetail(movieList.get(position));
                }
            }
        });

        holder.imageView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            }
        });
    }
    private void goToDetail(Movie movie) {
        Intent intent = new Intent(context, UserMovieDetails.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Movie1", movie);
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
        ImageView imageView;
        TextView name, price, genre;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView2);
            name = itemView.findViewById(R.id.textView20);
            price = itemView.findViewById(R.id.textView21);
            genre = itemView.findViewById(R.id.tvGenre);
        }
    }
}
