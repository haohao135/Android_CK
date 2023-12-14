package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ShowtimeManagerAdapter;
import com.example.myapplication.model.Showtime;
import com.example.myapplication.model.Theater;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UpdateShowtime extends AppCompatActivity {
    EditText dateshow, startime, endtime;
    String id;
    AutoCompleteTextView nameMovie, nameTheater;
    Button back, save;
    String movieName, theaterName, movieId, theaterId;
    FirebaseFirestore db;
    ArrayAdapter<String> adapter1, adapter2;
    List<String> listMovieName, listTheaterName;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_showtime);
        db = FirebaseFirestore.getInstance();
        Showtime showtime = (Showtime) getIntent().getExtras().get("Showtime");
        id = showtime.getId();
        getMovieNameById(showtime.getMovie_id());
        getTheaterNameById(showtime.getTheater_id());
        dateshow = findViewById(R.id.editText6);
        startime = findViewById(R.id.editText5);
        endtime = findViewById(R.id.editText8);
        nameMovie = findViewById(R.id.autoCompleteTextView3);
        nameTheater = findViewById(R.id.autoCompleteTextView4);
        back = findViewById(R.id.saveShowtime);
        save = findViewById(R.id.button6);
        listMovieName = new ArrayList<>();
        listTheaterName = new ArrayList<>();
        loadMovieNameFromFirestore();
        loadTheateNameFromFirestore();
        adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listMovieName);
        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listTheaterName);
        nameMovie.setAdapter(adapter1);
        nameTheater.setAdapter(adapter2);

        Handler handler1 = new Handler();
        handler1.postDelayed(() -> {
            dateshow.setText(showtime.getShowDate());
            startime.setText(showtime.getStarTime());
            endtime.setText(showtime.getEndTime());
            nameMovie.setText(movieName);
            nameTheater.setText(theaterName);
        }, 500);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedShowDate = dateshow.getText().toString().trim();
                String updatedStartTime = startime.getText().toString().trim();
                String updatedEndTime = endtime.getText().toString().trim();
                String updatedMovieName = nameMovie.getText().toString().trim();
                String updatedTheaterName = nameTheater.getText().toString().trim();
                getTheaterIdByName(updatedTheaterName);
                getMovieIdByName(updatedMovieName);
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    Showtime newShowtime = new Showtime(id, updatedShowDate, updatedStartTime, updatedEndTime, movieId, theaterId);
                    updateShowtime(newShowtime);
                }, 500);
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

    public void getTheaterNameById(String id) {
        CollectionReference moviesCollectionRef = db.collection("theaters");
        Query query = moviesCollectionRef.whereEqualTo("id", id);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                theaterName = documentSnapshot.getString("name");
            }
        });
    }

    public void getMovieIdByName(String name) {
        CollectionReference moviesCollectionRef = db.collection("movies");
        Query query = moviesCollectionRef.whereEqualTo("title", name);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                movieId = documentSnapshot.getId();
            }
        });
    }

    public void getTheaterIdByName(String name) {
        CollectionReference moviesCollectionRef = db.collection("theaters");
        Query query = moviesCollectionRef.whereEqualTo("name", name);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                runOnUiThread(() -> theaterId = documentSnapshot.getId());
            }
        });
    }

    private void updateShowtime(Showtime showtime) {
        DocumentReference theaterRef = db.collection("showtimes").document(showtime.getId());
        theaterRef.set(showtime)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UpdateShowtime.this, "Cập nhật thông tin lịch chiếu thành công", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(UpdateShowtime.this, "Cập nhật thông tin lịch chiếu thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void loadMovieNameFromFirestore() {
        CollectionReference moviesCollectionRef = db.collection("movies");
        moviesCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String title = documentSnapshot.getString("title");
                    listMovieName.add(title);
                }
            }
        });
    }

    public void loadTheateNameFromFirestore() {
        CollectionReference moviesCollectionRef = db.collection("theaters");
        moviesCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String title = documentSnapshot.getString("name");
                    listTheaterName.add(title);
                }
            }
        });
    }
}