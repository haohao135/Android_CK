package com.example.myapplication.activity;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UpdateTheater extends AppCompatActivity {
    EditText name, address, seat;
    Button back, save;
    FirebaseFirestore db;
    String id;
    List<Seat> seatList;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        Theater theater = (Theater) getIntent().getExtras().get("Theater");
        setContentView(R.layout.activity_updatetheater);
        name = findViewById(R.id.updateTheaterName);
        address = findViewById(R.id.updateTheaterAddress);
        seat = findViewById(R.id.updateTheaterSeats);
        back = findViewById(R.id.buttonBack);
        save  = findViewById(R.id.btnSaveUpdateTheater);
        id = theater.getId();
        seatList = theater.getSeatList();
        name.setText(theater.getName());
        address.setText(theater.getAddress());
        int countSeat = 0;
        for (int i = 0; i < theater.getSeatList().size(); i++) {
            countSeat++;
        }
        seat.setText(String.valueOf(countSeat));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Ename = name.getText().toString().trim();
                String Eaddress = address.getText().toString().trim();
                String Eseat = seat.getText().toString().trim();
                int num = Integer.parseInt(Eseat);
                List<Seat> seatList = new ArrayList<>();
                for (int i = 1; i <= num; i++){
                    String id = String.valueOf(UUID.randomUUID());
                    Seat seat = new Seat(id, i, 1, theater.getId());
                    seatList.add(seat);
                }
                Theater theater1 = new Theater(id, Ename, Eaddress, seatList);
                updateTheater(theater1);
            }
        });
    }

    private void updateTheater(Theater theater) {
        DocumentReference theaterRef = db.collection("theaters").document(theater.getId());
        theaterRef.set(theater)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UpdateTheater.this, "Cập nhật thông tin rạp thành công", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(UpdateTheater.this, "Cập nhật thông tin rạp thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}