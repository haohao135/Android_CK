package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class SignUp extends AppCompatActivity {
    EditText Eemail, Ename, Epassword, EconfirmPassword;
    Button register, signin;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Eemail = findViewById(R.id.email);
        Ename = findViewById(R.id.name);
        Epassword = findViewById(R.id.password);
        EconfirmPassword = findViewById(R.id.confirmpassword);
        register = findViewById(R.id.button2);
        signin = findViewById(R.id.signin);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Ename.getText().toString().trim();
                String email = Eemail.getText().toString().trim();
                String password = Epassword.getText().toString().trim();
                String confirmPassword = EconfirmPassword.getText().toString().trim();
                progressDialog.show();

                if(TextUtils.isEmpty(name)){
                    Ename.setError("Tên không được để trống");
                    progressDialog.dismiss();
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    Eemail.setError("Yêu cầu email");
                    progressDialog.dismiss();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Epassword.setError("Yêu cầu password");
                    progressDialog.dismiss();
                    return;
                }
                if(!password.equals(confirmPassword)){
                    EconfirmPassword.setError("Mật khẩu không trùng khớp");
                    progressDialog.dismiss();
                    return;
                }
                if(password.length()<6){
                    Epassword.setError("Độ dài mật khẩu phải trên 6 kí tự");
                    progressDialog.dismiss();
                    return;
                }
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            String id = firebaseAuth.getCurrentUser().getUid();
                            User user = new User(id, email, name, password);
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("Users").document(user.getId()).set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(SignUp.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignUp.this, "Đăng ký không thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(SignUp.this, "Đăng ký không thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}