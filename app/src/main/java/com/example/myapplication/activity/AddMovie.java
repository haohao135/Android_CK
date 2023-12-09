package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.model.Movie;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class AddMovie extends AppCompatActivity {
    Button back, save;
    EditText movieName, movieImage, moviePrice, movieDuration, movieDirector, movieGenre, movieTrailer, movieDescription;
    FirebaseFirestore db;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        back = findViewById(R.id.back);
        save = findViewById(R.id.save);

        movieName = findViewById(R.id.movieName);
        movieImage = findViewById(R.id.movieImage);
        moviePrice = findViewById(R.id.moviePrice);
        movieDuration = findViewById(R.id.movieDuration);
        movieDirector = findViewById(R.id.movieDirector);
        movieGenre = findViewById(R.id.movieGenre);
        movieTrailer = findViewById(R.id.movieTrailer);
        movieDescription = findViewById(R.id.movieDescription);
        db = FirebaseFirestore.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = String.valueOf(UUID.randomUUID());
                String title = movieName.getText().toString().trim();
                String image = movieImage.getText().toString().trim();
                double price = Double.parseDouble(moviePrice.getText().toString().trim());
                String duration = movieDuration.getText().toString().trim();
                String director = movieDirector.getText().toString().trim();
                String genre = movieGenre.getText().toString().trim();
                String trailer = movieTrailer.getText().toString().trim();
                String description = movieDescription.getText().toString().trim();
                Movie movie = new Movie(id, title, image, price, duration, director, genre, trailer, description);
                db.collection("movies").document(movie.getId()).set(movie)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AddMovie.this, "Thêm phim mới thành công", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddMovie.this, "Thêm phim mới thất bại", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }
}