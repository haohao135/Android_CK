package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.TicketAdapter;
import com.example.myapplication.model.Booking;
import com.example.myapplication.model.Theater;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListTicket extends AppCompatActivity {
    ImageView back;
    RecyclerView recyclerView;
    TicketAdapter adapter;
    List<Booking> bookingList;
    FirebaseFirestore db;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ticket);
        db = FirebaseFirestore.getInstance();
        back = findViewById(R.id.imgBackToProfile);
        recyclerView = findViewById(R.id.recyclerViewTicket);
        bookingList = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        loadDataFromFirestore(user.getUid());
        adapter = new TicketAdapter(bookingList, getApplicationContext());
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);
    }

    public void loadDataFromFirestore(String id) {
        bookingList.clear();
        CollectionReference moviesRef = db.collection("bookings");
        moviesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Booking booking = document.toObject(Booking.class);
                        if(booking.getUser_id().equals(id)){
                            bookingList.add(booking);
                            adapter.notifyDataSetChanged();
                        }

                    }
                }
            } else {
                Log.e("Choose theater", "Lỗi khi lấy danh sách rạp trên Firestore");
            }
        });
    }
}