package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;

public class UpdateActor extends AppCompatActivity {
    EditText Ename;
    Button back, save;
    String id;
    String actorName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_actor);

        Ename = findViewById(R.id.editTextAddActor);
        back = findViewById(R.id.UbackToActiViewAvctor);
        save = findViewById(R.id.UsaveActor);

        Intent newIntent = getIntent();
        String name = newIntent.getStringExtra("Name");
        id = newIntent.getStringExtra("Id");
        Ename.setText(name);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameReference = Ename.getText().toString().toString();
                Intent myIntent = new Intent();
                myIntent.putExtra("NAME", nameReference);
                myIntent.putExtra("ID", id);
                setResult(RESULT_OK, myIntent);
                finish();
                runOnUiThread(() -> MovieDetails.adapter.notifyDataSetChanged());
            }
        });
    }
}