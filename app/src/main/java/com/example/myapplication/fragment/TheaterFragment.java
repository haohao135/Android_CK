package com.example.myapplication.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.example.myapplication.R;
import com.example.myapplication.activity.Buy_ticket;
import com.example.myapplication.activity.CommingSoon;
import com.example.myapplication.activity.List_movie;
import com.example.myapplication.activity.ViewShowtime;
import com.example.myapplication.adapter.TheaterAdapter;
import com.example.myapplication.model.Movie;
import com.example.myapplication.model.Theater;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TheaterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TheaterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TheaterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TheaterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TheaterFragment newInstance(String param1, String param2) {
        TheaterFragment fragment = new TheaterFragment();
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
    FirebaseFirestore db;
    ImageView imageView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView imageViewBell;
    List<Theater> theaterList;
    TheaterAdapter adapter;
    RecyclerView recyclerView;
    public static String theaterPosition;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theater, container, false);
        db = FirebaseFirestore.getInstance();
        drawerLayout = view.findViewById(R.id.drawerLayout1);
        navigationView = view.findViewById(R.id.navigationView1);
        imageView = view.findViewById(R.id.iconImageViewMenu1);
        imageViewBell = view.findViewById(R.id.iconImageView1);
        recyclerView = view.findViewById(R.id.recyclerViewTheater);

        theaterList = new ArrayList<>();
        loadDataFromFirestore();
        adapter = new TheaterAdapter(theaterList, getContext());
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        imageViewBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CommingSoon.class));
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    drawerLayout.openDrawer(navigationView);
                }
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.movie){
                    startActivity(new Intent(getContext(), List_movie.class));
                } else if (itemId == R.id.buy_ticket) {
                    startActivity(new Intent(getContext(), Buy_ticket.class));
                } else if (itemId == R.id.showtime) {
                    startActivity(new Intent(getContext(), ViewShowtime.class));
                }
                return true;
            }
        });
        return view;
    }

    public void loadDataFromFirestore() {
        theaterList.clear();
        CollectionReference moviesRef = db.collection("theaters");
        moviesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Theater theater = document.toObject(Theater.class);
                        theaterList.add(theater);
                        adapter.notifyDataSetChanged();
                    }
                }
            } else {

            }
        });
    }
}