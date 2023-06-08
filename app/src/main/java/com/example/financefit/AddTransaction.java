package com.example.financefit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.financefit.databinding.ActivityAddTransactionBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AddTransaction extends AppCompatActivity {

    ActivityAddTransactionBinding binding;
    String type = "";
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(this);
        int maxLength = 15;
        binding.userDescAdd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        binding.expenseCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "Expense";
                binding.expenseCheck.setChecked(true);
                binding.incomeCheck.setChecked(false);
            }
        });

        binding.incomeCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "Income";
                binding.expenseCheck.setChecked(false);
                binding.incomeCheck.setChecked(true);
            }
        });
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = binding.userAmountAdd.getText().toString().trim();
                String desc = binding.userDescAdd.getText().toString().trim();
                if (amount.length() <= 0) {
                    return;
                }
                if (type.length() <= 0) {
                    Toast.makeText(AddTransaction.this, "Please Select Transaction Type", Toast.LENGTH_SHORT).show();
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy_HH:mm", Locale.getDefault());
                String currentDateTime = sdf.format(new Date());
                progressDialog.show();
                progressDialog.setMessage("Just a moment...");
                String id = UUID.randomUUID().toString();
                Map<String, Object> transaction = new HashMap<>();
                transaction.put("id", id);
                transaction.put("amount", amount);
                transaction.put("desc", desc);
                transaction.put("type", type);
                transaction.put("date", currentDateTime);
                firebaseFirestore.collection("Expenses").document(firebaseAuth.getUid()).collection("Notes").document(id)
                        .set(transaction)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.cancel();
                                Toast.makeText(AddTransaction.this, "Transaction Added", Toast.LENGTH_SHORT).show();
                                binding.userAmountAdd.setText("");
                                binding.userDescAdd.setText("");
                                Intent intent = new Intent(AddTransaction.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(AddTransaction.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }
}



