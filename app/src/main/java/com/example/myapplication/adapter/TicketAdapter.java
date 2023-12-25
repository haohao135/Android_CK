package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Booking;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.logging.Handler;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder>{
    List<Booking> bookingList;
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public TicketAdapter(List<Booking> bookingList, Context context) {
        this.bookingList = bookingList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        getMovieIDByShowtimeId(bookingList.get(position).getShowtime_id(), holder);
        getTotolPriceByPaymentID(bookingList.get(position).getPayment_id(), holder);
        String v = String.valueOf(bookingList.get(position).getDate_booking());
        holder.date.setText(v);
    }

    private void getMovieIDByShowtimeId(String id, ViewHolder holder) {
        CollectionReference moviesCollectionRef = db.collection("showtimes");
        Query query = moviesCollectionRef.whereEqualTo("id", id);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String movieID = documentSnapshot.getString("movie_id");
                getMovieImageByMovieID(movieID, holder);
                getMovienameByMovieID(movieID, holder);
            }
        });
    }

    private void getMovieImageByMovieID(String id, ViewHolder holder) {
        CollectionReference moviesCollectionRef = db.collection("movies");
        Query query = moviesCollectionRef.whereEqualTo("id", id);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String img = documentSnapshot.getString("image");
                Picasso.with(context)
                        .load(img)
                        .placeholder(R.mipmap.ic_launcher)
                        .into(holder.imageView);
            }
        });
    }

    private void getMovienameByMovieID(String id, ViewHolder holder) {
        CollectionReference moviesCollectionRef = db.collection("movies");
        Query query = moviesCollectionRef.whereEqualTo("id", id);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String n = documentSnapshot.getString("title");
                holder.name.setText(n);
            }
        });
    }

    private void getTotolPriceByPaymentID(String id, ViewHolder holder) {
        CollectionReference moviesCollectionRef = db.collection("payments");
        Query query = moviesCollectionRef.whereEqualTo("id", id);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Double pr = documentSnapshot.getDouble("price_total");
                String t = String.valueOf(pr);
                holder.price.setText(t);
            }
        });
    }



    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name, price, date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView99);
            name = itemView.findViewById(R.id.textView24);
            price = itemView.findViewById(R.id.textView25);
            date = itemView.findViewById(R.id.textView26);
        }
    }
}
