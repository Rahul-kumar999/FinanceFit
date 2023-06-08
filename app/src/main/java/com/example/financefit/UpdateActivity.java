package com.example.financefit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Toast;

import com.example.financefit.databinding.ActivityUpdateBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateActivity extends AppCompatActivity {
    ActivityUpdateBinding binding;
    String newType;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();
        int maxLength = 15;
        binding.userDescUpdate.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        String id = getIntent().getStringExtra("id");
        String amount = getIntent().getStringExtra("amount");
        String desc = getIntent().getStringExtra("desc");
        String type = getIntent().getStringExtra("type");

        binding.userAmountUpdate.setText(amount);
        binding.userDescUpdate.setText(desc);

        switch (type) {
            case "Expense":
                newType = "Expense";
                binding.expenseCheck1.setChecked(true);
                break;
            case "Income":
                newType = "Income";
                binding.incomeCheck1.setChecked(true);
                break;


        }
        binding.incomeCheck1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newType = "Income";
                binding.incomeCheck1.setChecked(true);
                binding.expenseCheck1.setChecked(false);

            }
        });
        binding.expenseCheck1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newType = "Expense";
                binding.incomeCheck1.setChecked(false);
                binding.expenseCheck1.setChecked(true);

            }
        });
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String amount = binding.userAmountUpdate.getText().toString();
                String desc = binding.userDescUpdate.getText().toString();
                firebaseFirestore.collection("Expenses").document(firebaseAuth.getUid())
                        .collection("Notes").document(id)
                        .update("amount",amount,"desc",desc,"type",newType)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                onBackPressed();
                                Toast.makeText(UpdateActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("Expenses").document(firebaseAuth.getUid())
                        .collection("Notes")
                        .document(id).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                onBackPressed();
                                Toast.makeText(UpdateActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }
}