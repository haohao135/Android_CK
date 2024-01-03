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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListTicket extends AppCompatActivity {
    public static String clickTicketPosition;
    ImageView back;
    RecyclerView recyclerView;
    TicketAdapter adapter;
    List<Booking> bookingList;
    FirebaseFirestore db;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public static String ticketPosition;
    String ticketDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ticket);
        db = FirebaseFirestore.getInstance();

        back = findViewById(R.id.imgBackToProfile);
        recyclerView = findViewById(R.id.recyclerViewTicket);
        bookingList = new ArrayList<>();

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
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.cancelTicket){
            if(!ticketPosition.equals("null")){
                for(Booking booking : bookingList){
                    if(booking.getId() == ticketPosition){
                        Date currentdate = new Date();

                          deleteTicketFromFirestore(ticketPosition);
//                        Intent intent1 = new Intent(getContext(), UpdateShowtime.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("Showtime", showtime);
//                        intent1.putExtras(bundle);
//                        startActivityForResult(intent1, 1002);
                    }
                }
            }
        }
        return super.onContextItemSelected(item);
    }
    public void deleteTicketFromFirestore(String ticketId) {
        CollectionReference ticketRef = db.collection("bookings");
        ticketRef.document(ticketId)
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        loadDataFromFirestore(user.getUid());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void getDateByShowtimeId(String id) {
        CollectionReference dateCollectionRef = db.collection("showtimes");
        Query query = dateCollectionRef.whereEqualTo("id", id);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                ticketDate = documentSnapshot.getString("showDate");
            }
        });
    }
}
