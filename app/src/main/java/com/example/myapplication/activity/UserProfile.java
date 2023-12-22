package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.fragment.MovieFragment;
import com.example.myapplication.model.Movie;
import com.example.myapplication.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
        imgBack = findViewById(R.id.imgBack);
        btnBack= findViewById(R.id.btnBack);
        btnSave= findViewById(R.id.btnSave);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        getUserNameById(user.getUid());
        String userID = user.getUid();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edEmail.getText().toString().trim();
                String username = edUsername.getText().toString().trim();
                String password = edPassword.getText().toString().trim();
                User user1 = new User(userID,email,username,password);
                updateUserInFirestore(user1);
                Toast.makeText(UserProfile.this, "Sửa thông tin tài khoản thành công", Toast.LENGTH_SHORT).show();
                logout();
            }

        });
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

    private void updateUserInFirestore(User updatedUser) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("Users");
        usersRef.document(updatedUser.getId())
                .set(updatedUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UserProfile.this, "Sửa thông tin tài khoản thành công", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserProfile.this, "Sửa thông tin tài khoản thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void logout() {
        MovieFragment.login_status = false;
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getBaseContext(), RequestLogin.class));
        finish();
    }

}