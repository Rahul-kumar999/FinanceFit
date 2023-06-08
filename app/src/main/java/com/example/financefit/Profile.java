package com.example.financefit;

import static com.google.android.material.color.utilities.MaterialDynamicColors.error;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {
    ImageView uploadImage , SettingsImg, imageView3, imageView11;
    Button btnUpdate, button;
    TextView edtTelephone, edtAddress, edtPostal,edtBudget,textView, textView2, textView15, txtPassword;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ArrayList<UserProfileModel> userProfileModels;
    ArrayList<UserDataClass> userDataClassArrayList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        uploadImage = findViewById(R.id.imageProfile);
        uploadImage.setClipToOutline(true);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        btnUpdate = findViewById(R.id.buttonUpdate);
        button = findViewById(R.id.button);
        edtAddress = findViewById(R.id.edtText2);
        imageView11 = findViewById(R.id.imageView11);
        imageView3 = findViewById(R.id.imageView3);
        edtPostal = findViewById(R.id.textView5);
        edtBudget = findViewById(R.id.textView6);
        edtTelephone = findViewById(R.id.edtText1);
        textView15 = findViewById(R.id.textView15);
        SettingsImg = findViewById(R.id.SettingsImg);
        txtPassword = findViewById(R.id.txtPassword);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        firebaseFirestore = FirebaseFirestore.getInstance();
        userDataClassArrayList = new ArrayList<>();
        userProfileModels = new ArrayList<>();
        getUserInfo();
        loadData3();
        loadData4();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, UpdateProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Summary.class);
                startActivity(intent);
                finish();
            }
        });
        imageView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Notification.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void getUserInfo() {
        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount()>0){
                    if (snapshot.hasChild("image")){
                        String image = snapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(uploadImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadData4() {
        firebaseFirestore.collection("New Intro Profiles").document(firebaseAuth.getUid()).collection("New Users Intro")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot ds : task.getResult()) {
                            UserDataClass model1 = new UserDataClass(ds.getString("Telephone"),ds.getString("Address"),ds.getString("Postal"),ds.getString("Budget"));
                            userDataClassArrayList.add(model1);

                            edtTelephone.setText(ds.getString("Telephone"));
                            edtAddress.setText(ds.getString("Address"));
                            edtPostal.setText(ds.getString("Postal"));
                            edtBudget.setText(ds.getString("Budget"));
                        }


                    }
                });

    }

    //Load data using Firestore Database
    private void loadData3() {
        firebaseFirestore.collection("New SignUp Profiles").document(firebaseAuth.getUid()).collection("New Users")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot ds : task.getResult()) {
                            UserProfileModel model = new UserProfileModel(ds.getString("Email"), ds.getString("Name"), ds.getString("Mobile No"), ds.getString("Password"));
                            userProfileModels.add(model);

                            textView.setText(ds.getString("Name"));
                            textView2.setText(ds.getString("Email"));
                            textView15.setText(ds.getString("Mobile No"));
                            txtPassword.setText(ds.getString("Password"));
                        }
                    }
                });
    }

}