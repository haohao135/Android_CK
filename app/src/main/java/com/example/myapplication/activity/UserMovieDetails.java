package com.example.myapplication.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ActorAdapter;
import com.example.myapplication.model.Actor;
import com.example.myapplication.model.Movie;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserMovieDetails extends AppCompatActivity {
    TextView movieName, movieDuration, movieDirector, movieGenre, moviePrice, movieDescription, movieActor;
    ImageView movieImage;
    VideoView movieTrailer;
    Button back, buy;
    Movie movie;
    FirebaseFirestore db;
    static ActorAdapter adapter;
    List<Movie> movieList;
    List<Actor> actorList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_movie_detail);
        db = FirebaseFirestore.getInstance();

        movieName =  findViewById(R.id.movieName);
        movieImage = findViewById(R.id.movieImage);
        movieDuration=  findViewById(R.id.movieDuration);
        movieDirector=  findViewById(R.id.movieDirector);
        movieGenre=  findViewById(R.id.movieGenre);
        moviePrice=  findViewById(R.id.moviePrice);
        movieTrailer = findViewById(R.id.movieTrailer);
        movieActor = findViewById(R.id.movieActor);
        movieDescription =  findViewById(R.id.movieDescription);
        back = findViewById(R.id.backToListMovie);
        buy= findViewById(R.id.btnTicket);

        movie = (Movie) getIntent().getExtras().get("Movie1");
        movieName.setText(movie.getTitle());
//        movieImage.setText(movie.getImage());
        moviePrice.setText(String.valueOf(movie.getPrice()));
        movieDuration.setText(movie.getDuration());
        movieDirector.setText(movie.getDirector());
        movieGenre.setText(movie.getGenre());
//        movieTrailer.setText(movie.getTrailer());
        movieDescription.setText(movie.getDescription());

//        movie = (Movie) getIntent().getExtras().get("Movie1");
        actorList = movie.getActorList();
        // Iterate over the actor list and build the actor names string
        StringBuilder actorNamesBuilder = new StringBuilder();
        for (int i = 0; i < actorList.size(); i++) {
            Actor actor = actorList.get(i);
            actorNamesBuilder.append(actor.getActorName());
            if (i != actorList.size() - 1) {
                actorNamesBuilder.append(", ");
            }
        }

        movieActor.setText(actorNamesBuilder.toString());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Buy_ticket.class);
                intent.putExtra("ID", movie.getId());
                startActivityForResult(intent, 101);
            }
        });
    }

}

