package com.example.myapplication.adapter;

import android.annotation.SuppressLint;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.fragment.Showtime_manager_Fragment;
import com.example.myapplication.model.Showtime;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class ShowtimeManagerAdapter extends RecyclerView.Adapter<ShowtimeManagerAdapter.ViewHolder>{
    List<Showtime> showtimeList;
    String movieName;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public ShowtimeManagerAdapter(List<Showtime> showtimeList) {
        this.showtimeList = showtimeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.showtime_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Showtime showtime = showtimeList.get(position);
        holder.name.setText(showtime.getMovie_id());
        holder.time.setText(showtime.getStarTime());
        holder.date.setText(showtime.getShowDate());
        getMovieNameById(showtime.getMovie_id(), holder);
        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                Showtime_manager_Fragment.showtimePosition = showtimeList.get(position).getId();
                MenuInflater inflater = new MenuInflater(v.getContext());
                inflater.inflate(R.menu.menu_context_account, menu);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(!showtimeList.isEmpty()){
            return showtimeList.size();
        }
        return 0;
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, time, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView14);
            time = itemView.findViewById(R.id.textView16);
            date = itemView.findViewById(R.id.textView15);
        }
    }
}
