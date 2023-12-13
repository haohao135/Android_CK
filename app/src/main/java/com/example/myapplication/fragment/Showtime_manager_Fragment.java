package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.activity.AddShowtime;
import com.example.myapplication.model.Theater;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Showtime_manager_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Showtime_manager_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Showtime_manager_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Showtime_manager_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Showtime_manager_Fragment newInstance(String param1, String param2) {
        Showtime_manager_Fragment fragment = new Showtime_manager_Fragment();
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
    Button addShowtime;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_showtime_manager, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewShowtime);
        addShowtime = view.findViewById(R.id.btnAddShowtime);

        addShowtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddShowtime.class);
                startActivityForResult(intent, 1001);
            }
        });
        return view;
    }

//    public void loadDataFromFirestore() {
//        theaterList.clear();
//        CollectionReference moviesRef = db.collection("theaters");
//
//        moviesRef.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                QuerySnapshot querySnapshot = task.getResult();
//                if (querySnapshot != null) {
//                    for (QueryDocumentSnapshot document : querySnapshot) {
//                        Theater theater = document.toObject(Theater.class);
//                        theaterList.add(theater);
//                        adapter.notifyDataSetChanged();
//                    }
//                }
//            } else {
//
//            }
//        });
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1001 && resultCode == -1){

        }
        if(requestCode == 1002 && resultCode == -1){

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}