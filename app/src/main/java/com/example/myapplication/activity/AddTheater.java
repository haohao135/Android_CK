package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.model.Seat;
import com.example.myapplication.model.Theater;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddTheater extends AppCompatActivity {
    EditText name, address;
    Button back, save;
    FirebaseFirestore db;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_theater);
        name = findViewById(R.id.addNameTheater);
        address = findViewById(R.id.addAddressTheater);
        back = findViewById(R.id.button4);
        save = findViewById(R.id.btnSaveAddTheater);
        db = FirebaseFirestore.getInstance();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tid = String.valueOf(UUID.randomUUID());
                String ename = name.getText().toString().trim();
                String eaddress = address.getText().toString().trim();
                List<Seat> seatList = new ArrayList<>();
                for (int i = 1; i <= 40; i++){
                    String id = String.valueOf(UUID.randomUUID());
                    Seat seat = new Seat(id, i, false, tid);
                    seatList.add(seat);
                }
                Theater theater = new Theater(tid, ename, eaddress, seatList);
                db.collection("theaters").document(theater.getId()).set(theater).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddTheater.this, "Thêm rạp mới thành công", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddTheater.this, "Thêm rạp mới thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}