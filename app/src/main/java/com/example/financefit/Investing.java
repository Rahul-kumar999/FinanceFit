package com.example.financefit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.financefit.databinding.ActivityAddPortfolioBinding;
import com.example.financefit.databinding.ActivityInvestBinding;
import com.example.financefit.databinding.ActivityInvestingBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.StringValue;

import java.util.ArrayList;

public class Investing extends AppCompatActivity {
    ActivityInvestingBinding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    int sumPortfolio = 0;
    int sumSavings = 0;
    ProgressDialog progressDialog;
    ArrayList<PortfolioModel> portfolioModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInvestingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        portfolioModelArrayList= new ArrayList<>();
        loadData3();
        loadData4();
        binding.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Investing.this, AddPortfolio.class);
                startActivity(intent);
                finish();
            }
        });
        binding.imgSavings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Investing.this, AddSavings.class);
                startActivity(intent);
                finish();
            }
        });
        binding.btnInvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Investing.this, Invest.class);
                //intent.putExtra("TotalVal",TotalVal );
                startActivity(intent);
                finish();
            }
        });


    }

    private void loadData4() {
        //progressDialog.show();
        //progressDialog.setMessage("Loading...");
        firebaseFirestore.collection("Savings").document(firebaseAuth.getUid()).collection("Data")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot ds : task.getResult()) {
                            PortfolioModel model = new PortfolioModel(ds.getString("amount"), ds.getString("Name"), ds.getString("StartDate"), ds.getString("EndDate"), ds.getString("type"), ds.getString("Id"),ds.getString("TypeA"));
                            int amount = Integer.parseInt(ds.getString("amount"));
                            if (ds.getString("TypeA").equals("Deposits") || ds.getString("TypeA").equals("Securities")) {
                                sumSavings = sumSavings + amount;
                            }
                            binding.txtSavVal.setText("₹"+String.valueOf(sumSavings));
                            //progressDialog.cancel();
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
                                sumPortfolio = sumPortfolio + amount;
                            }
                            binding.txtPortVal.setText("₹"+String.valueOf(sumPortfolio));
                        }
                    }
                });
    }


}