package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ActorAdapter;
import com.example.myapplication.model.Actor;
import com.example.myapplication.model.Movie;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** @noinspection deprecation*/
public class MovieDetails extends AppCompatActivity {
    TextView name;
    public static String pos;
    RecyclerView recyclerView;
    Button back, add;
    Movie movie;
    FirebaseFirestore db;
    static ActorAdapter adapter;
    List<Actor> actorList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        db = FirebaseFirestore.getInstance();

        name =  findViewById(R.id.textView11);
        recyclerView = findViewById(R.id.recyclerViewActor);
        back = findViewById(R.id.backToListMovie);
        add = findViewById(R.id.addActor);
        movie = (Movie) getIntent().getExtras().get("Object");
        name.setText(movie.getTitle());

        actorList = new ArrayList<>();
        adapter = new ActorAdapter(movie.getActorList());
        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        loadActorFromFirestore();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AddActor.class);
                intent.putExtra("ID", movie.getId());
                startActivityForResult(intent, 101);
            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.editAccount){
            if(!pos.equals("null")){
                for (Actor c : movie.getActorList()){
                    if(c.getId().equals(pos)){
                        Intent myIntent = new Intent(this, UpdateActor.class);
                        myIntent.putExtra("Name", c.getActorName());
                        myIntent.putExtra("Id", c.getId());
                        startActivityForResult(myIntent, 702);
                    }
                }
            }
        }
        if(item.getItemId() == R.id.deleteAccount){
            if(!pos.equals("null")){
                deleteActor(pos);
            }
        }
        return super.onContextItemSelected(item);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 101 && resultCode == -1){
            String id = String.valueOf(UUID.randomUUID());
            String nameActor = data.getStringExtra("Name");
            Actor actor = new Actor(id, nameActor, movie.getId());
            movie.getActorList().add(actor);
            adapter.notifyDataSetChanged();

            CollectionReference studentsCollection = db.collection("movies");
            studentsCollection.document(movie.getId()).update("actorList", movie.getActorList());
            loadActorFromFirestore();
        }

        if(requestCode == 702 && resultCode == -1){
            String id = data.getStringExtra("ID");
            String name = data.getStringExtra("NAME");
            for (int i = 0; i <movie.getActorList().size(); i++){
                Actor certificate1 = movie.getActorList().get(i);
                if(certificate1.getId().equals(id)){
                    certificate1.setActorName(name);
                    adapter.notifyDataSetChanged();
                    editActor(certificate1);
                    break;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void loadActorFromFirestore() {
        CollectionReference studentsCollectionRef = db.collection("movies");
        studentsCollectionRef.get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        if (document.getId().equals(movie.getId())) {
                            if (document.contains("actorList")) {
                                List<Object> certificateRefs = (List<Object>) document.get("actorList");
                                if(certificateRefs!=null){
                                    movie.getActorList().clear();
                                    for (Object certificateObj : certificateRefs) {
                                        if (certificateObj instanceof Map) {
                                            Map<String, Object> certificateData = (Map<String, Object>) certificateObj;
                                            String certificateName = (String) certificateData.get("actorName");
                                            String id = (String) certificateData.get("id");
                                            Actor c = new Actor(id, certificateName, movie.getId());
                                            movie.getActorList().add(c);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Lỗi khi lấy dữ liệu từ Firestore", e);
                });
    }

    private void deleteActor(String id) {
        DocumentReference studentRef = db.collection("movies").document(movie.getId());
        List<Actor> certificatesToDelete = new ArrayList<>();
        for (Actor certificate : movie.getActorList()) {
            if (certificate.getId().equals(id)) {
                certificatesToDelete.add(certificate);
            }
        }
        movie.getActorList().removeAll(certificatesToDelete);
        studentRef.update("actorList", movie.getActorList())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(MovieDetails.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MovieDetails.this, "Lỗi khi xóa", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Lỗi khi xóa Certificate từ Firestore", e);
                    movie.getActorList().addAll(certificatesToDelete);
                });
        adapter.notifyDataSetChanged();
    }

    private void editActor(Actor certificate) {
        DocumentReference studentRef = db.collection("movies").document(movie.getId());

        studentRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                List<Actor> certificates = documentSnapshot.toObject(Movie.class).getActorList();
                if (certificates != null) {
                    int certificateIndex = -1;
                    for (int i = 0; i < certificates.size(); i++) {
                        Actor c = certificates.get(i);
                        if (c.getId().equals(certificate.getId())) {
                            certificateIndex = i;
                            break;
                        }
                    }
                    if (certificateIndex != -1) {
                        certificates.set(certificateIndex, certificate);

                        Map<String, Object> updates = new HashMap<>();
                        updates.put("actorList", certificates);
                        studentRef.update(updates)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(MovieDetails.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(MovieDetails.this, "Lỗi khi cập nhật", Toast.LENGTH_SHORT).show();
                                    Log.e("Firestore", "Lỗi khi cập nhật Certificate trong Firestore", e);
                                });
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}