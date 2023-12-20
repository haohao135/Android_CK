package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ListTheaterAdapter;
import com.example.myapplication.model.Movie;
import com.example.myapplication.model.Theater;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListTheater extends AppCompatActivity {
    ImageView imgBack;
    List<Theater> theaterList;
    FirebaseFirestore db;
    ListTheaterAdapter adapter;
    RecyclerView recyclerView;
    Movie movie;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_theater);
        imgBack = findViewById(R.id.imgBack);
        db = FirebaseFirestore.getInstance();
        theaterList = new ArrayList<>();
        loadDataFromFirestore();

        movie = (Movie) getIntent().getExtras().get("Movie1");

        adapter = new ListTheaterAdapter(theaterList, getBaseContext());
        recyclerView = findViewById(R.id.recyclerViewListTheater);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void loadDataFromFirestore() {
        theaterList.clear();
        CollectionReference moviesRef = db.collection("theaters");
        moviesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Theater theater = document.toObject(Theater.class);
                        theaterList.add(theater);
                        adapter.notifyDataSetChanged();
                    }
                }
            } else {
                Log.e("Choose theater", "Lỗi khi lấy danh sách rạp trên Firestore");
            }
        });
    }
}