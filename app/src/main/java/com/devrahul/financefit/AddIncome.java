package com.devrahul.financefit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Toast;


import com.devrahul.financefit.databinding.ActivityAddIncomeBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddIncome extends AppCompatActivity {
    ActivityAddIncomeBinding binding;
    String Type = "";
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddIncomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(this);
        int maxLength = 9;
        binding.userCompanyAdd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        int maxLengthOne = 2;
        binding.userDateAdd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthOne)});
        int maxLenghtn = 8;
        binding.userAmountAdd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLenghtn)});

        binding.expenseCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Type = "Job Salary";
                binding.expenseCheck.setChecked(true);
                binding.incomeCheck.setChecked(false);
                binding.carCheck.setChecked(false);
                binding.otherCheck.setChecked(false);
            }
        });

        binding.incomeCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Type = "Business";
                binding.expenseCheck.setChecked(false);
                binding.incomeCheck.setChecked(true);
                binding.carCheck.setChecked(false);
                binding.otherCheck.setChecked(false);
            }
        });
        binding.carCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Type = "Agriculture";
                binding.expenseCheck.setChecked(false);
                binding.incomeCheck.setChecked(false);
                binding.carCheck.setChecked(true);
                binding.otherCheck.setChecked(false);

            }
        });
        binding.otherCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Type = "Other Sources";
                binding.expenseCheck.setChecked(false);
                binding.incomeCheck.setChecked(false);
                binding.carCheck.setChecked(false);
                binding.otherCheck.setChecked(true);

            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = binding.userAmountAdd.getText().toString().trim();
                String Name = binding.userCompanyAdd.getText().toString().trim();
                String StartDate = binding.userDateAdd.getText().toString().trim();
                String EndDate = binding.userEndDateAdd.getText().toString().trim();
                String type = binding.userTypeAdd.getText().toString().trim();
                if (amount.length() <= 0) {
                    return;
                }

                progressDialog.show();
                progressDialog.setMessage("Just a moment...");
                String Id = UUID.randomUUID().toString();
                Map<String, Object> transaction = new HashMap<>();
                transaction.put("Id", Id);
                transaction.put("amount", amount);
                transaction.put("Name", Name);
                transaction.put("StartDate", StartDate);
                transaction.put("EndDate", EndDate);
                transaction.put("type", type);
                transaction.put("TypeA",Type);
                firebaseFirestore.collection("Income").document(firebaseAuth.getUid()).collection("Data").document(Id)
                        .set(transaction)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.cancel();
                                Toast.makeText(AddIncome.this, "Investment Added", Toast.LENGTH_SHORT).show();
                                binding.userAmountAdd.setText("");
                                binding.userCompanyAdd.setText("");
                                binding.userDateAdd.setText("");
                                binding.userEndDateAdd.setText("");
                                binding.userTypeAdd.setText("");
                                Intent intent = new Intent(AddIncome.this, Income.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(AddIncome.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}