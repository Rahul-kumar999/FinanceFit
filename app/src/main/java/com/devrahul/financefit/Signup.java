package com.devrahul.financefit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.devrahul.financefit.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Signup extends AppCompatActivity {
    //Button button;
    ActivitySignupBinding binding;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    FirebaseFirestore firebaseFirestore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        progressDialog= new ProgressDialog(this);


        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String name = binding.editTextTextPersonName3.getText().toString();
               String email = binding.editTextTextPersonName.getText().toString().trim();
               String password = binding.editTextTextPersonName2.getText().toString();
               String mobileNo = binding.editTextTextPersonName4.getText().toString();
                if ((email.length() <= 0) || name.length()<=0 || password.length()<=0 || mobileNo.length()<=0) {
                    Toast.makeText(Signup.this, "Please Enter The Required Details To Proceed", Toast.LENGTH_SHORT).show();
                    return;
                }
               progressDialog.show();
               progressDialog.setMessage("Just a moment");

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                startActivity(new Intent(Signup.this, Login.class));
                                progressDialog.cancel();
                                String id = UUID.randomUUID().toString();
                                Map<String, Object> transaction2 = new HashMap<>();
                                transaction2.put("Id", id);
                                transaction2.put("Name", name);
                                transaction2.put("Email",email);
                                transaction2.put("Password", password);
                                transaction2.put("Mobile No" , mobileNo);
                                firebaseFirestore.collection("New SignUp Profiles").document(firebaseAuth.getUid()).collection("New Users").document(id)
                                        .set(transaction2)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                binding.editTextTextPersonName3.setText("");
                                                binding.editTextTextPersonName.setText("");
                                                binding.editTextTextPersonName2.setText("");
                                                binding.editTextTextPersonName4.setText("");

                                            }
                                        });
//
                                Intent intent = new Intent(Signup.this, Introprofile.class);
                                startActivity(intent);
                                finish();

                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(Signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });
        binding.btnRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Signup.this, "Combination of Uppercase, \nLowercase & Numeric Values", Toast.LENGTH_LONG).show();
            }
        });

//

    }
}