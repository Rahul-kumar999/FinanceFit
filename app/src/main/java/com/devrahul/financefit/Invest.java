package com.devrahul.financefit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.devrahul.financefit.databinding.ActivityInvestBinding;
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

public class Invest extends AppCompatActivity {
    ActivityInvestBinding binding;
    ArrayList<UserName> userNameArrayList;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ImageView imgProfile;
    int SumInvestment = 0;
    TextView txtName, value;
    ProgressDialog progressDialog;
    ArrayList<PortfolioModel> portfolioModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInvestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        imgProfile = findViewById(R.id.imageView3);
        progressDialog = new ProgressDialog(this);
        imgProfile.setClipToOutline(true);
        txtName = findViewById(R.id.txtName);
        value = findViewById(R.id.monthlyBudget);
        userNameArrayList = new ArrayList<>();
        portfolioModelArrayList= new ArrayList<>();
        loadData1();
        loadData2();
        loadData3();
        loadData4();
        binding.imgPort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Invest.this, Portfolio.class);
                startActivity(intent);
            }
        });
        binding.imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Invest.this, Savings.class);
                startActivity(intent);
            }
        });
        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });




    }

    private void loadData4() {
        firebaseFirestore.collection("Savings").document(firebaseAuth.getUid()).collection("Data")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot ds : task.getResult()) {
                            PortfolioModel model = new PortfolioModel(ds.getString("amount"), ds.getString("Name"), ds.getString("StartDate"), ds.getString("EndDate"), ds.getString("type"), ds.getString("Id"),ds.getString("TypeA"));
                            int amount = Integer.parseInt(ds.getString("amount"));
                            if (ds.getString("TypeA").equals("Deposits") || ds.getString("TypeA").equals("Securities")) {
                                SumInvestment = SumInvestment + amount;
                                binding.monthlyBudget.setText("₹"+String.valueOf(SumInvestment));

                            }
                        }
                    }
                });
    }

    private void loadData3() {
        firebaseFirestore.collection("Portfolio").document(firebaseAuth.getUid()).collection("Data")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot ds : task.getResult()) {
                            PortfolioModel model = new PortfolioModel(ds.getString("amount"), ds.getString("Name"), ds.getString("StartDate"), ds.getString("EndDate"), ds.getString("type"), ds.getString("Id"),ds.getString("TypeA"));
                            int amount = Integer.parseInt(ds.getString("amount"));
                            if (ds.getString("TypeA").equals("Stocks") || ds.getString("TypeA").equals("Mutual Funds")) {
                                SumInvestment = SumInvestment + amount;
                            }
                            binding.monthlyBudget.setText("₹"+String.valueOf(SumInvestment));

                        }
                    }

                });

    }

    private void loadData2() {
        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount()>0){
                    if (snapshot.hasChild("image")){
                        String image = snapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(imgProfile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadData1() {
        firebaseFirestore.collection("New SignUp Profiles").document(firebaseAuth.getUid()).collection("New Users")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot ds : task.getResult()) {
                            UserName model3 = new UserName(ds.getString("Name"));
                            userNameArrayList.add(model3);

                            txtName.setText("Hello "+ds.getString("Name"));
                        }
                    }
                });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Invest.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}