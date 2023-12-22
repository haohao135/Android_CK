package com.example.myapplication.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Account_Manager_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Account_Manager_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Account_Manager_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Account_Manager_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Account_Manager_Fragment newInstance(String param1, String param2) {
        Account_Manager_Fragment fragment = new Account_Manager_Fragment();
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
    EditText edEmail, edUsername,edPass;
    Button btnBack,btnSave;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_manager, container, false);

        db = FirebaseFirestore.getInstance();
        edEmail= view.findViewById(R.id.edEmail);
        edUsername= view.findViewById(R.id.edUsername);
        edPass= view.findViewById(R.id.edPass);
        btnBack= view.findViewById(R.id.btnBack);
        btnSave= view.findViewById(R.id.btnSave);

        if (MovieFragment.login_status == true) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            getUserNameById(user.getUid());
        }

        return view;
    }

    public void getUserNameById(String id) {
        CollectionReference moviesCollectionRef = db.collection("Users");
        Query query = moviesCollectionRef.whereEqualTo("id", id);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String email = documentSnapshot.getString("email");
                String username = documentSnapshot.getString("username");
                String password = documentSnapshot.getString("password");
                loadUserFromFirestore(email,username,password);
            }
        });
    }

    private void loadUserFromFirestore(String email,String username, String password) {
        edEmail.setText(email);
        edUsername.setText(username);
        edPass.setText(password);
    }
}