package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ListMovieAdapter;
import com.example.myapplication.model.Movie;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class List_movie extends AppCompatActivity {
    public static String clickMoviePosition;
    Spinner chooseGenre;
    ImageView back;
    EditText search;
    RecyclerView recyclerView;
    List<Movie> movieList;
    ListMovieAdapter adapter;

    FirebaseFirestore db;
    ArrayAdapter<String> adapterSpinner;
    List<String> genre;
    public static String moviePosition;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("TAG", "onCreate: ");
        setContentView(R.layout.activity_list_movie);
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerViewListMovie);
        back = findViewById(R.id.backListMovie);
        search = findViewById(R.id.editText);
        chooseGenre = findViewById(R.id.chooseGenre);

        movieList = new ArrayList<>();
        genre = new ArrayList<>();
        genre.add("Tất cả");
        genre.add("Tình cảm");
        genre.add("Hành động");
        genre.add("Tâm lý");
        genre.add("Kinh dị");
        genre.add("Hài hước");
        genre.add("Viễn tưởng");

        loadDataFromFirestore();
        adapter = new ListMovieAdapter(movieList, getBaseContext());

        adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genre);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        recyclerView.setAdapter(adapter);


        chooseGenre.setAdapter(adapterSpinner);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        chooseGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGenre = (String) parent.getItemAtPosition(position);
                if (selectedGenre.equals("Tất cả")) {
                    loadDataFromFirestore();
                } else {
                    getMovieFromFirestoreByGenre(selectedGenre);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString();
                if (searchText.isEmpty()) {
                    loadDataFromFirestore();
                    adapter.setMovieList(movieList);
                } else {
                    searchMovies(searchText);
                }
            }
        });
    }
    @SuppressLint("NotifyDataSetChanged")
    private void searchMovies(String searchText) {
        String convertedString =
                Normalizer
                        .normalize(searchText, Normalizer.Form.NFD)
                        .replaceAll("[^\\p{ASCII}]", "");
        List<Movie> newList = new ArrayList<>();
        for (Movie movie : movieList) {
            String convertedString1 =
                    Normalizer
                            .normalize(movie.getTitle(), Normalizer.Form.NFD)
                            .replaceAll("[^\\p{ASCII}]", "");
            if (convertedString1.toLowerCase().contains(convertedString.toLowerCase())) {
                newList.add(movie);
            }
        }
        adapter.setMovieList(newList);
        adapter.notifyDataSetChanged();
    }

    public void loadDataFromFirestore() {
        movieList.clear();
        CollectionReference moviesRef = db.collection("movies");
        moviesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Movie movie = document.toObject(Movie.class);
                        movieList.add(movie);
                        adapter.notifyDataSetChanged();
                    }
                }
            } else {
                Log.d("TAG", "Failed to retrieve data from Firstore");
            }
        });
    }

    public void getMovieFromFirestoreByGenre(String genre) {
        movieList.clear();
        CollectionReference moviesRef = db.collection("movies");
        moviesRef.whereEqualTo("genre", genre)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                Movie movie = document.toObject(Movie.class);
                                movieList.add(movie);
                            }
                        } else {
                            Log.d("TAG", "No movies found with genre: " + genre);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("TAG", "Error getting movies by genre: ", task.getException());
                    }
                });
    }
}