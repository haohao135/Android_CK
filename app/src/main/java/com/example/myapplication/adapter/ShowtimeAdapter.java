package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.ChooseSeats;
import com.example.myapplication.activity.UserMovieDetails;
import com.example.myapplication.model.Showtime;
import com.example.myapplication.model.Theater;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShowtimeAdapter extends RecyclerView.Adapter<ShowtimeAdapter.ViewHolder>{
    List<Showtime> showtimeList;
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ShowtimeAdapter(List<Showtime> showtimeList, Context context) {
        this.showtimeList = showtimeList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.showtime_item2, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Showtime showtime = showtimeList.get(position);
        getMovieImageById(showtime.getMovie_id(), holder);
        holder.name.setText(showtime.getMovie_id());
        holder.time.setText(showtime.getStarTime());
        holder.date.setText(showtime.getShowDate());
        getMovieNameById(showtime.getMovie_id(), holder);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChooseSeats.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    private void getMovieNameById(String id, ViewHolder holder) {
        CollectionReference moviesCollectionRef = db.collection("movies");
        Query query = moviesCollectionRef.whereEqualTo("id", id);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String movieName = documentSnapshot.getString("title");
                holder.name.setText(movieName);
            }
        });
    }

    private void getMovieImageById(String id, ViewHolder holder) {
        CollectionReference moviesCollectionRef = db.collection("movies");
        Query query = moviesCollectionRef.whereEqualTo("id", id);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String movieName = documentSnapshot.getString("image");
                Picasso.with(context)
                        .load(movieName)
                        .placeholder(R.mipmap.ic_launcher)
                        .into(holder.image);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(showtimeList!=null){
            return showtimeList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name, time, date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView4);
            name = itemView.findViewById(R.id.textView144);
            time = itemView.findViewById(R.id.textView166);
            date = itemView.findViewById(R.id.tvStardate);
        }
    }

}
