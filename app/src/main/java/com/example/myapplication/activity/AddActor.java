package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;
import com.example.myapplication.model.Actor;
import com.example.myapplication.model.Theater;

import org.checkerframework.checker.units.qual.A;

import java.util.UUID;

public class AddActor extends AppCompatActivity {
    EditText nameActor;
    Button back, save;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_actor);
        nameActor = findViewById(R.id.editTextAddActor);
        back = findViewById(R.id.backToActiViewAvctor);
        save = findViewById(R.id.saveActor);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameActor.getText().toString().trim();
                Intent intent = new Intent();
                intent.putExtra("Name", name);
                setResult(RESULT_OK, intent);
                finish();
                runOnUiThread(() -> MovieDetails.adapter.notifyDataSetChanged());
            }
        });
    }
}