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

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.activity.AddTheater;
import com.example.myapplication.activity.UpdateTheater;
import com.example.myapplication.adapter.TheaterManagerAdapter;
import com.example.myapplication.model.Theater;
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
 * Use the {@link Theater_Manager_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Theater_Manager_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Theater_Manager_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Theater_Manager_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Theater_Manager_Fragment newInstance(String param1, String param2) {
        Theater_Manager_Fragment fragment = new Theater_Manager_Fragment();
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
    RecyclerView recyclerView;
    Button addTheater;
    TheaterManagerAdapter adapter;
    List<Theater> theaterList;
    FirebaseFirestore db;
    public static String theaterPosition;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_theater__manager_, container, false);
        db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.theater_recycle);
        addTheater = view.findViewById(R.id.add_theater);
        theaterList = new ArrayList<>();
        loadDataFromFirestore();

        adapter = new TheaterManagerAdapter(theaterList);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        addTheater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), AddTheater.class), 1001);
            }
        });
        return view;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.editAccount){
            if(!theaterPosition.equals("null")){
                for(Theater theater : theaterList){
                    if(theater.getId() == theaterPosition){
                        Intent intent1 = new Intent(getContext(), UpdateTheater.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Theater", theater);
                        intent1.putExtras(bundle);
                        startActivityForResult(intent1, 1002);
                    }
                }
            }
        }
        if(item.getItemId() == R.id.deleteAccount){
            if(!theaterPosition.equals("null")){
                deleteTheaterFromFirestore(theaterPosition);
            }
        }
        return super.onContextItemSelected(item);
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

    private void deleteTheaterFromFirestore(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference theatersRef = db.collection("theaters");

        theatersRef.document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        loadDataFromFirestore();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
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