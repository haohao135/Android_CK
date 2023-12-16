package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Movie;
import com.example.myapplication.model.User;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
    List<Movie> userList;
    public void setUserList(List<Movie> newList){
        this.userList = newList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = userList.get(position);
        if(movie == null){
            return;
        }
        //holder.imageView.set
        holder.name.setText(movie.getTitle());
        holder.price.setText(String.valueOf(movie.getPrice()));
        holder.duration.setText(movie.getDuration());
    }

    @Override
    public int getItemCount() {
        if(userList!=null){
            return userList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name;
        TextView price;
        TextView duration;
        TextView time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_movie);
            name = itemView.findViewById(R.id.name_movie);
            price = itemView.findViewById(R.id.price_movie);
            duration = itemView.findViewById(R.id.time_movie);
            time = itemView.findViewById(R.id.date_movie);
        }
    }
}
