package com.devrahul.financefit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.devrahul.financefit.databinding.ActivityLoanBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Loan extends AppCompatActivity {
    ActivityLoanBinding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ArrayList<PortfolioModel> portfolioModelArrayList;
    LoanAdapter loanAdapter;
    private TextView TxtRecyclerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        portfolioModelArrayList = new ArrayList<>();
        binding.loanView.setLayoutManager(new LinearLayoutManager(this));
        binding.loanView.setHasFixedSize(true);
        loadData();
        binding.btnadda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Loan.this, AddLoan.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private void loadData() {
        String Id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore.collection("Loan").document(Id).collection("Data")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        portfolioModelArrayList.clear();
                        for (DocumentSnapshot ds : task.getResult()) {
                            PortfolioModel model = new PortfolioModel(ds.getString("amount"), ds.getString("Name"), ds.getString("StartDate"), ds.getString("EndDate"), ds.getString("type"), ds.getString("Id"),ds.getString("TypeA"));
                            portfolioModelArrayList.add(model);

                        }
                        loanAdapter = new LoanAdapter(Loan.this, portfolioModelArrayList);
                        binding.loanView.setAdapter(loanAdapter);
                        TxtRecyclerText = (TextView) binding.txtrecyclerText;
                        if (portfolioModelArrayList.isEmpty()){
                            binding.loanView.setVisibility(View.GONE);
                            TxtRecyclerText.setVisibility(View.VISIBLE);
                        } else{
                            binding.loanView.setVisibility(View.VISIBLE);
                            TxtRecyclerText.setVisibility(View.GONE);
                        }

                    }
                });

    }
}