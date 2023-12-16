package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ShowtimeAdapter;
import com.example.myapplication.model.Showtime;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewShowtime extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseFirestore db;
    List<Showtime> showtimeList;
    ShowtimeAdapter adapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_showtime);
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerViewShowtime1);

        showtimeList = new ArrayList<>();
        loadDataFromFirestore();
        adapter = new ShowtimeAdapter(showtimeList, getBaseContext());
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
    }

    public void loadDataFromFirestore() {
        showtimeList.clear();
        CollectionReference moviesRef = db.collection("showtimes");
        moviesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Showtime showtime = document.toObject(Showtime.class);
                        showtimeList.add(showtime);
                        adapter.notifyDataSetChanged();
                    }
                }
            } else {

            }
        });
    }
}