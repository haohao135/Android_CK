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
import com.example.myapplication.activity.Buy_ticket;
import com.example.myapplication.activity.ListShowtime;
import com.example.myapplication.activity.MovieDetails;
import com.example.myapplication.fragment.Movie_Manager_Fragment;
import com.example.myapplication.model.Theater;

import java.util.List;

public class ListTheaterAdapter extends RecyclerView.Adapter<ListTheaterAdapter.ViewHolder>{
    List<Theater> theaterList;
    Context context;

    public ListTheaterAdapter(List<Theater> theaterList, Context context) {
        this.theaterList = theaterList;
        this.context = context;
    }

    @NonNull
    @Override
    public ListTheaterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.theater_item, parent,false);
        return new ListTheaterAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListTheaterAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
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
        Intent intent = new Intent(context, ListShowtime.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("theater_id", theater.getId());
//        intent.putExtra("movie_id", theater.getId());

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
