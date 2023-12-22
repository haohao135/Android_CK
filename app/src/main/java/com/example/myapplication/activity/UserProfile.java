package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.myapplication.R;
import com.example.myapplication.fragment.MovieFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class UserProfile extends AppCompatActivity {
 EditText edEmail, edUsername,edPassword;
 ImageView imgBack;
 Button btnBack, btnSave;
 FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        db = FirebaseFirestore.getInstance();

        edEmail= findViewById(R.id.edEmail);
        edUsername= findViewById(R.id.edUsername);
        edPassword= findViewById(R.id.edPass);
        btnBack= findViewById(R.id.btnBack);
        btnSave= findViewById(R.id.btnSave);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (MovieFragment.login_status == true) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            getUserNameById(user.getUid());
        }
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
        edPassword.setText(password);
    }
}