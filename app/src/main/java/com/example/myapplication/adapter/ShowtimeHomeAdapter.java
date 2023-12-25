package com.example.myapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.ChooseSeats;
import com.example.myapplication.activity.RequestLogin;
import com.example.myapplication.fragment.MovieFragment;
import com.example.myapplication.model.Showtime;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShowtimeHomeAdapter extends RecyclerView.Adapter<ShowtimeHomeAdapter.ViewHolder>{
    List<Showtime> showtimeList;
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ShowtimeHomeAdapter(List<Showtime> showtimeList, Context context) {
        this.showtimeList = showtimeList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Showtime showtime = showtimeList.get(position);
        getMovieNameById(showtime.getMovie_id(), holder);
        getMovieImageById(showtime.getMovie_id(), holder);
        getMoviePriceById(showtime.getMovie_id(), holder);
        holder.time.setText(showtime.getStarTime());
        holder.date.setText(showtime.getShowDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MovieFragment.login_status==false){
                    Intent intent = new Intent(context, RequestLogin.class);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, ChooseSeats.class);
                    intent.putExtra("theaterID", showtimeList.get(position).getTheater_id());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Showtime", showtimeList.get(position));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }

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
                        .into(holder.imageView);
            }
        });
    }

    private void getMoviePriceById(String id, ViewHolder holder) {
        CollectionReference moviesCollectionRef = db.collection("movies");
        Query query = moviesCollectionRef.whereEqualTo("id", id);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                double movieName = documentSnapshot.getDouble("price");
                holder.price.setText(String.valueOf(movieName));
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
        ImageView imageView;
        TextView name, price, time, date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_movie);
            name = itemView.findViewById(R.id.name_movie);
            price = itemView.findViewById(R.id.price_movie);
            time = itemView.findViewById(R.id.time_movie);
            date = itemView.findViewById(R.id.date_movie);
        }
    }
}
