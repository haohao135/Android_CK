package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.model.Showtime;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class AddShowtime extends AppCompatActivity {
    EditText Edate, Estartime, Eendtime;
    AutoCompleteTextView nameMovie, nameTheater;
    Button back, add;
    List<String> listMovieName;
    List<String> listTheaterName;
    FirebaseFirestore db;
    ArrayAdapter<String> adapter1;
    ArrayAdapter<String> adapter2;
    private String movieId;
    private String theaterId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_showtime);

        Edate = findViewById(R.id.editText2);
        Estartime = findViewById(R.id.editText3);
        Eendtime = findViewById(R.id.editText4);
        nameMovie = findViewById(R.id.autoCompleteTextView2);
        nameTheater = findViewById(R.id.autoCompleteTextView);
        back = findViewById(R.id.backToManagerShowtime);
        add = findViewById(R.id.button5);
        db = FirebaseFirestore.getInstance();

        listMovieName = new ArrayList<>();
        listTheaterName = new ArrayList<>();
        loadMovieNameFromFirestore();
        loadTheateNameFromFirestore();

        adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listMovieName);
        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listTheaterName);
        nameMovie.setAdapter(adapter1);
        nameTheater.setAdapter(adapter2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = String.valueOf(UUID.randomUUID());
                String date = Edate.getText().toString().trim();
                String startime = Estartime.getText().toString().trim();
                String endtime = Eendtime.getText().toString().trim();
                getMovieIdByName(nameMovie.getText().toString());
                getTheaterIdByName(nameTheater.getText().toString());
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    Showtime showtime = new Showtime(id, date, startime, endtime, movieId, theaterId);

                    db.collection("showtimes").document(showtime.getId()).set(showtime)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(AddShowtime.this, "Thêm lịch chiếu thành công", Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddShowtime.this, "Thêm lịch chiếu thất bại", Toast.LENGTH_SHORT).show();
                                }
                            });
                }, 500);
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
}