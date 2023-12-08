package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.admin.Admin_home;
import com.example.myapplication.fragment.MovieFragment;
import com.example.myapplication.model.Notifications;
import com.example.myapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.List;

public class Login extends AppCompatActivity {
    EditText Ename, Epassword;
    ImageView back;
    TextView forgotPassword;
    Button register, signIn;
    FirebaseAuth auth;
    User user;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        back = findViewById(R.id.back);
        Ename = findViewById(R.id.editText);
        Epassword = findViewById(R.id.editText1);
        forgotPassword = findViewById(R.id.misspassword);
        signIn = findViewById(R.id.button2);
        register = findViewById(R.id.btnRegister);
        auth = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), SignUp.class));
            }
        });
        Log.e("TAG", "onCreate2: " + user);
        signIn.setOnClickListener(v -> {
            String email = Ename.getText().toString().trim();
            String password = Epassword.getText().toString().trim();
            if(TextUtils.isEmpty(email)){
                Ename.setError("Email không được để trống");
                return;
            }
            if(TextUtils.isEmpty(password)){
                Epassword.setError("Yêu cầu password");
                return;
            }
            if(password.length()<6){
                Epassword.setError("Độ dài mật khẩu phải trên 6 kí tự");
                return;
            }
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                if(auth.getInstance().getCurrentUser()!=null){
                    String userID = auth.getInstance().getCurrentUser().getUid();
                    db.collection("Users").document(userID).get().addOnSuccessListener(documentSnapshot -> {
                        String getId = documentSnapshot.getString("id");
                        String getEmail = documentSnapshot.getString("email");
                        String getUsername = documentSnapshot.getString("username");
                        String getPassword = documentSnapshot.getString("password");
                        long role = (long) documentSnapshot.get("role");
                        if(role == 1){
                            startActivity(new Intent(Login.this, Admin_home.class));
                            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show());
                        }else if (role == 2){
                            MovieFragment.login_status = true;
                            startActivity(new Intent(Login.this, MainActivity.class));
                            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show());
                        }
                    });
                }
            }).addOnFailureListener(e ->
                    Toast.makeText(getApplicationContext(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show());
        });
    }

}