package com.example.myapplication.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ShowtimeAdapter;
import com.example.myapplication.model.Showtime;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListShowtime extends AppCompatActivity {
    RecyclerView recyclerViewListShowtime;
    FirebaseFirestore db;
    List<Showtime> showtimeList;
    ShowtimeAdapter adapter;
    ImageView back;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_showtime);
        db = FirebaseFirestore.getInstance();
        recyclerViewListShowtime = findViewById(R.id.recyclerViewListShowtime);
        back = findViewById(R.id.imgBack);

        Intent intent = getIntent();
        String movieID= intent.getStringExtra("movieID");
        String theaterID= intent.getStringExtra("theaterID");

        showtimeList = new ArrayList<>();
        loadCorrectDataFromFirestore(movieID,theaterID);
        adapter = new ShowtimeAdapter(showtimeList, getBaseContext());
        recyclerViewListShowtime.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL);
        recyclerViewListShowtime.addItemDecoration(dividerItemDecoration);
        recyclerViewListShowtime.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void loadCorrectDataFromFirestore(String movieID, String theaterID) {
        showtimeList.clear();
        CollectionReference moviesRef = db.collection("showtimes");
        moviesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Showtime showtime = document.toObject(Showtime.class);
                        if(showtime.getMovie_id().equals(movieID) && showtime.getTheater_id().equals(theaterID)){
                            showtimeList.add(showtime);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            } else {
                Log.e("Choose showtime", "Lỗi khi lấy danh sách lịch chiếu trên Firestore");
            }
        });
    }
}
