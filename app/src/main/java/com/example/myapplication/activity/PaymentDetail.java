package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.model.Booking;
import com.example.myapplication.model.Payment;
import com.example.myapplication.model.Seat;
import com.example.myapplication.model.Showtime;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PaymentDetail extends AppCompatActivity {
    TextInputEditText nameUser, nameMovie, startDate, startime, thoiluong;
    String username, movieName, duration;
    Double priceM;
    TextView price, numSeat, totalPrice;
    FirebaseFirestore db;
    Showtime showtime;
    Button back, buy;
    ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);
        db = FirebaseFirestore.getInstance();
        nameUser = findViewById(R.id.BnameUser);
        nameMovie = findViewById(R.id.BnameMovie);
        startDate = findViewById(R.id.BdateShowtime);
        startime = findViewById(R.id.BtimeShowtime);
        thoiluong = findViewById(R.id.Bduration);
        price = findViewById(R.id.Bprice);
        numSeat = findViewById(R.id.BnumberSeat);
        totalPrice = findViewById(R.id.BtotalPrice);
        back = findViewById(R.id.Bback);
        buy = findViewById(R.id.button7);

        showtime = (Showtime) getIntent().getExtras().get("Showtime");
        Log.e("TAG", showtime.toString());
        FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
        getUserNameById(auth.getUid());
        getMovieNameById(showtime.getMovie_id());
        getDurationById(showtime.getMovie_id());
        getPriceById(showtime.getMovie_id());
        progressDialog = new ProgressDialog(this);
        Handler handler = new Handler();
        startDate.setText(showtime.getShowDate());
        startime.setText(showtime.getStarTime());
        List<Seat> seats = (List<Seat>) getIntent().getExtras().get("list");

        int countseat = CountSeat(seats);
        numSeat.setText(String.valueOf(countseat));

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nameUser.setText(username);
                nameMovie.setText(movieName);
                thoiluong.setText(duration);
                price.setText(String.valueOf(priceM));
                totalPrice.setText(String.valueOf(priceM*countseat));
            }
        }, 400);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String paymentID = String.valueOf(UUID.randomUUID());
                Date date = new Date();
                Double gia = Double.parseDouble(totalPrice.getText().toString());
                Payment payment = new Payment(paymentID, gia, "Online", date);
                String bookingID = String.valueOf(UUID.randomUUID());
                Booking booking = new Booking(bookingID, date, countseat, auth.getUid(), paymentID, showtime.getId());

                db.collection("payments").document(payment.getId()).set(payment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        finish();
                    }
                });

                db.collection("bookings").document(booking.getId()).set(booking).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        List<Seat> s1 = (List<Seat>) getIntent().getExtras().get("listFull");
                        updateSeatStatus(s1);
                        progressDialog.dismiss();
                        startActivity(new Intent(getBaseContext(), PaymentSuccess.class));
                        finish();
                    }
                });
            }
        });
    }

    public int CountSeat(List<Seat> seats){
        int count = 0;
        for (Seat seat : seats){
            count++;
        }
        return count;
    }

    public void getUserNameById(String id) {
        CollectionReference moviesCollectionRef = db.collection("Users");
        Query query = moviesCollectionRef.whereEqualTo("id", id);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                username = documentSnapshot.getString("username");
            }
        });
    }

    public void getMovieNameById(String id) {
        CollectionReference moviesCollectionRef = db.collection("movies");
        Query query = moviesCollectionRef.whereEqualTo("id", id);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                movieName = documentSnapshot.getString("title");
            }
        });
    }

    public void getDurationById(String id) {
        CollectionReference moviesCollectionRef = db.collection("movies");
        Query query = moviesCollectionRef.whereEqualTo("id", id);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                duration = documentSnapshot.getString("duration");
            }
        });
    }

    public void getPriceById(String id) {
        CollectionReference moviesCollectionRef = db.collection("movies");
        Query query = moviesCollectionRef.whereEqualTo("id", id);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                priceM = documentSnapshot.getDouble("price");
            }
        });
    }

    public void updateSeatStatus(List<Seat> ss) {
        for (Seat seat : ss) {
            if (seat.getStatus()==2) {
                seat.setStatus(3);
                break;
            }
        }
        db.collection("theaters").document(showtime.getTheater_id())
                .update("seatList", ss)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
    }
}