package com.devrahul.financefit;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.devrahul.financefit.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Login extends AppCompatActivity {
    ActivityLoginBinding bindingA;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;
    //GoogleSignInOptions gso;
    //GoogleSignInClient gsc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingA=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(bindingA.getRoot());
        String email = bindingA.editTextTextPersonName.getText().toString().trim();
        //String password = bindingA.editTextTextPersonName2.getText().toString();
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()!=null){
                    try {
                        startActivity(new Intent(Login.this,MainActivity.class));
                        finish();
                    }
                    catch (Exception e){

                    }
                }
            }
        });
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);

        // checking if user already signed in
        if (googleSignInAccount != null) {

            // open home activity
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                // getting signed in account after user selected an account from google accounts dialog
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                handleSignInTask(task);
            }
        });
        bindingA.btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Login.this, "Please First Register Yourself!", Toast.LENGTH_SHORT).show();
            }
        });

        bindingA.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                activityResultLauncher.launch(signInIntent);
            }
        });

        bindingA.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = bindingA.editTextTextPersonName.getText().toString().trim();
                String password = bindingA.editTextTextPersonName2.getText().toString();
                if (email.length() <= 0) {
                    Toast.makeText(Login.this, "Please Enter Your Account Email To Proceed", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() <= 0) {
                    Toast.makeText(Login.this, "Please Enter Your Account Password To Proceed", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                progressDialog.setMessage("Logging In");
                firebaseAuth.signInWithEmailAndPassword(email,password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                progressDialog.cancel();
                                Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                String id = UUID.randomUUID().toString();
                                Map<String, Object> transaction1 = new HashMap<>();
                                transaction1.put("Id", id);
                                transaction1.put("Email",email);
                                transaction1.put("Password", password);
                                firebaseFirestore.collection("Profiles").document(firebaseAuth.getUid()).collection("Users").document(id)
                                        .set(transaction1)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                bindingA.editTextTextPersonName.setText("");
                                                bindingA.editTextTextPersonName2.setText("");

                                            }
                                        });


                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        bindingA.btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = bindingA.editTextTextPersonName.getText().toString().trim();
                if (email.length() <= 0) {
                    Toast.makeText(Login.this, "Please Enter Your Account Email To Proceed", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.setTitle("Sending Mail");
                progressDialog.show();
                firebaseAuth.sendPasswordResetEmail(email)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.cancel();
                                Toast.makeText(Login.this, "Email Sent", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
        bindingA.buttonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Signup.class));
            }
        });
    }

    private void handleSignInTask(Task<GoogleSignInAccount> task) {

        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);

            // getting account data
            final String getFullName = account.getDisplayName();
            final String getEmail = account.getEmail();
            //final Uri getPhotoUrl = account.getPhotoUrl();

            // opening MainActivity
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();

        } catch (ApiException e) {
            e.printStackTrace();
            Toast.makeText(this, "Please Register Yourself First!", Toast.LENGTH_SHORT).show();
        }

        progressDialog = new ProgressDialog(this);
//        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//        gsc = GoogleSignIn.getClient(this,gso);
//        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
//        if (googleSignInAccount !=null){
//            startActivity(new Intent(Login.this,MainActivity.class));
//            finish();
//        }
//        bindingA.btnGoogle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SignIn(); //se at line 115-125
//            }
//
//        });


    }
//    private void SignIn() {
//        Intent intent = gsc.getSignInIntent();
//        startActivityForResult(intent,100);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 100){
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                task.getResult(ApiException.class);
//                HomeActivity();
//            } catch (ApiException e) {
//                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
//            }
//
//        }
//
//    }
//    private void HomeActivity() {
//        finish();
//        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(Login.this, MainActivity.class);
//        startActivity(intent);
//
//    }
}