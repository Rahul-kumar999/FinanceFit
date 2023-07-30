package com.devrahul.financefit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.devrahul.financefit.databinding.ActivityPortfolioBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Portfolio extends AppCompatActivity {
    ActivityPortfolioBinding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ArrayList<PortfolioModel> portfolioModelArrayList;
    PortfolioAdapter portfolioAdapter;
    private TextView TxtRecyclerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPortfolioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        portfolioModelArrayList = new ArrayList<>();
        binding.portfolioView.setLayoutManager(new LinearLayoutManager(this));
        binding.portfolioView.setHasFixedSize(true);
        loadData();
        binding.btnadda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Portfolio.this, AddPortfolio.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private void loadData() {
        String Id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore.collection("Portfolio").document(Id).collection("Data")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        portfolioModelArrayList.clear();
                        for (DocumentSnapshot ds : task.getResult()) {
                            PortfolioModel model = new PortfolioModel(ds.getString("amount"), ds.getString("Name"), ds.getString("StartDate"), ds.getString("EndDate"), ds.getString("type"), ds.getString("Id"),ds.getString("TypeA"));
                            portfolioModelArrayList.add(model);

                        }
                        portfolioAdapter = new PortfolioAdapter(Portfolio.this, portfolioModelArrayList);
                        binding.portfolioView.setAdapter(portfolioAdapter);
                        TxtRecyclerText = (TextView) binding.txtrecyclerText;
                        if (portfolioModelArrayList.isEmpty()){
                            binding.portfolioView.setVisibility(View.GONE);
                            TxtRecyclerText.setVisibility(View.VISIBLE);
                        } else{
                            binding.portfolioView.setVisibility(View.VISIBLE);
                            TxtRecyclerText.setVisibility(View.GONE);
                        }

                    }
                });

    }
}