package com.example.myapplication.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.fragment.Movie_Manager_Fragment;
import com.example.myapplication.model.Seat;
import com.example.myapplication.model.Showtime;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChooseSeats extends AppCompatActivity {
    Button btnBack, btnPayment;
    ImageView imgBack;
    FirebaseFirestore db;
    ArrayList<Seat> seatList;
    RecyclerView recyclerView;

    SeatAdapter adapter;
    Showtime showtime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_seats);
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.gridSeats);
        imgBack= findViewById(R.id.imgBack);
        btnBack= findViewById(R.id.btnBack);
        btnPayment= findViewById(R.id.btnPayment);
        showtime = (Showtime) getIntent().getExtras().get("Showtime");
        seatList = new ArrayList<>();
        String TTID = getIntent().getStringExtra("theaterID");
        SharedPreferences sharedPref = getSharedPreferences("MyTheaterID", Context.MODE_PRIVATE);
        String theaterID = sharedPref.getString("theaterID", "1");
        if(TTID == null){
            getSeatFromFirestoreByTheaterID(theaterID);
        } else {
            getSeatFromFirestoreByTheaterID(TTID);
        }
        adapter = new SeatAdapter(seatList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), 5));


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Seat> seats = countSeat(seatList);
                Intent intent = new Intent(getBaseContext(), PaymentDetail.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Showtime", showtime);
                bundle.putSerializable("list", (Serializable) seats);
                bundle.putSerializable("listFull", (Serializable) seatList);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }
    public List<Seat> countSeat(List<Seat> seatList){
        List<Seat> seats = new ArrayList<>();
        for (Seat seat : seatList){
            if(seat.getStatus() == 2){
                seats.add(seat);
            }
        }
        return seats;
    }

    public void getSeatFromFirestoreByTheaterID(String theaterID){
        seatList.clear();
        db.collection("theaters")
                .document(theaterID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ArrayList<Map<String, Object>> seatArray = (ArrayList<Map<String, Object>>) documentSnapshot.get("seatList");
                        for(Map<String, Object> seat: seatArray){
                            Seat newSeat = new Seat();
                            newSeat.setId((String) seat.get("id"));
                            newSeat.setSeat_number(Integer.parseInt(seat.get("seat_number").toString()));
                            newSeat.setStatus(Integer.parseInt(seat.get("status").toString()));
                            newSeat.setTheater_id((String) seat.get("theater_id"));
                            seatList.add(newSeat);
                        }
                        runOnUiThread(() -> adapter.notifyDataSetChanged());
                    }
                });

    }
}

class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.ViewHolder> {
    ArrayList<Seat> seatList;
    Context context;

    public SeatAdapter(ArrayList<Seat> seatList, Context context) {
        this.seatList = seatList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_seat_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.so.setText(String.valueOf(seatList.get(position).getSeat_number()));
        if (seatList.get(position).getStatus() == 1) {
            holder.background.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.color.green));
        } else if (seatList.get(position).getStatus() == 3) {
            holder.background.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.color.red));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seatList.get(position).getStatus()==1){
                    holder.background.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.color.yellow));
                    seatList.get(position).setStatus(2);
                }else if(seatList.get(position).getStatus()==2){
                    holder.background.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.color.green));
                    seatList.get(position).setStatus(1);
                }

                String id = seatList.get(position).getId();
            }
        });
    }

    @Override
    public int getItemCount() {
        return seatList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView so;
        LinearLayoutCompat background;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            so = itemView.findViewById(R.id.tvSeatNumber);
            background = itemView.findViewById(R.id.background);
        }
    }
}
