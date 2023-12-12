package com.example.myapplication.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.activity.AddMovie;
import com.example.myapplication.activity.UpdateMovie;
import com.example.myapplication.adapter.MovieManagerAdapter;
import com.example.myapplication.model.Movie;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Movie_Manager_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Movie_Manager_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Movie_Manager_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Movie_Manager_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Movie_Manager_Fragment newInstance(String param1, String param2) {
        Movie_Manager_Fragment fragment = new Movie_Manager_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    Button addMovie;
    public static String clickMoviePosition;
    FirebaseFirestore db;
    List<Movie> movieList;
    MovieManagerAdapter adapter;
    RecyclerView recyclerView;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie__manager_, container, false);
        addMovie = view.findViewById(R.id.button3);
        db = FirebaseFirestore.getInstance();
        movieList = new ArrayList<>();
        loadDataFromFirestore();

        adapter = new MovieManagerAdapter(getContext(), movieList);

        recyclerView = view.findViewById(R.id.rcvMovieManager);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        addMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), AddMovie.class), 1001);
            }
        });
        return view;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.editAccount){
            if(!clickMoviePosition.equals("null")){
                for(Movie movie : movieList){
                    if(movie.getId() == clickMoviePosition){
                        Intent intent1 = new Intent(getContext(), UpdateMovie.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Movie", movie);
                        intent1.putExtras(bundle);
                        startActivityForResult(intent1, 1002);
                    }
                }
            }
        }
        if(item.getItemId() == R.id.deleteAccount){
            if(!clickMoviePosition.equals("null")){
                deleteMovieFromFirestore(clickMoviePosition);
            }
        }
        return super.onContextItemSelected(item);
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

            }
        });
    }

    public void deleteMovieFromFirestore(String movieId) {
        CollectionReference moviesRef = db.collection("movies");
        moviesRef.document(movieId)
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        loadDataFromFirestore();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1001 && resultCode == -1){
            loadDataFromFirestore();
        }
        if(requestCode==1002 && resultCode == -1){
            loadDataFromFirestore();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}