package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.model.Movie;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateMovie extends AppCompatActivity {
    EditText movieName, movieImage, moviePrice, movieDuration, movieDirector, movieGenre, movieTrailer, movieDescription;
    Button back, save;
    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_movie);

        movieName = findViewById(R.id.movieName);
        movieImage = findViewById(R.id.movieImage);
        moviePrice = findViewById(R.id.moviePrice);
        movieDuration = findViewById(R.id.movieDuration);
        movieDirector = findViewById(R.id.movieDirector);
        movieGenre = findViewById(R.id.movieGenre);
        movieTrailer = findViewById(R.id.movieTrailer);
        movieDescription = findViewById(R.id.movieDescription);
        back = findViewById(R.id.back);
        save = findViewById(R.id.save);

        Intent intent = getIntent();
        Movie movie = (Movie) intent.getExtras().get("Movie");

        movieName.setText(movie.getTitle());
        movieImage.setText(movie.getImage());
        moviePrice.setText(String.valueOf(movie.getPrice()));
        movieDuration.setText(movie.getDuration());
        movieDirector.setText(movie.getDirector());
        movieGenre.setText(movie.getGenre());
        movieTrailer.setText(movie.getTrailer());
        movieDescription.setText(movie.getDescription());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = movieName.getText().toString().trim();
                String image = movieImage.getText().toString().trim();
                double price = Double.parseDouble(moviePrice.getText().toString().trim());
                String duration = movieDuration.getText().toString().trim();
                String director = movieDirector.getText().toString().trim();
                String genre = movieGenre.getText().toString().trim();
                String trailer = movieTrailer.getText().toString().trim();
                String description = movieDescription.getText().toString().trim();

                Movie movie1 = new Movie(movie.getId(), name, image, price, duration, director, genre, description, trailer);
                updateMovieInFirestore(movie1);
            }
        });
    }

    private void updateMovieInFirestore(Movie updatedMovie) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference moviesRef = db.collection("movies");

        moviesRef.document(updatedMovie.getId())
                .set(updatedMovie)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UpdateMovie.this, "Sửa phim thành công", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateMovie.this, "Sửa phim thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}