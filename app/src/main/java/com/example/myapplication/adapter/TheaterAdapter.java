package com.example.myapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.MovieDetails;
import com.example.myapplication.fragment.Movie_Manager_Fragment;
import com.example.myapplication.model.Movie;
import com.example.myapplication.model.Theater;

import java.util.List;

public class TheaterAdapter extends RecyclerView.Adapter<TheaterAdapter.ViewHolder>{
    List<Theater> theaterList;
    Context context;

    public TheaterAdapter(List<Theater> theaterList, Context context) {
        this.theaterList = theaterList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.theater_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Theater theater = theaterList.get(position);
        if(theater == null){
            return;
        }

        holder.theaterName.setText(theater.getName());
        holder.theaterAddress.setText(theater.getAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = theaterList.get(position).getId();
                if(!id.equals("")){
                    Movie_Manager_Fragment.clickMoviePosition = id;
                    goToDetail(theaterList.get(position));
                }
            }
        });
    }

    private void goToDetail(Theater theater) {
        Intent intent = new Intent(context, MovieDetails.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Object", theater);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if(theaterList!=null){
            return theaterList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView theaterName, theaterAddress;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            theaterName = itemView.findViewById(R.id.textView77);
            theaterAddress = itemView.findViewById(R.id.textView88);
        }
    }
}
