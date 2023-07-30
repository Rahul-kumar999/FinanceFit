package com.devrahul.financefit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Toast;


import com.devrahul.financefit.databinding.ActivityUpdateProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateProfileActivity extends AppCompatActivity {
    ActivityUpdateProfileBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        int maxLength =8;
        binding.edtBudget.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                progressDialog.setMessage("Just a Moment...");


                String telephone = binding.edtText1.getText().toString();
                String address = binding.edtText2.getText().toString();
                String postal = binding.textView5.getText().toString();
                String budget = binding.edtBudget.getText().toString();
                String id = firebaseAuth.getCurrentUser().getUid();
                if (budget.isEmpty() || address.isEmpty() || postal.isEmpty() || budget.isEmpty()){
                    Toast.makeText(UpdateProfileActivity.this, "Please Fill Info in all Required Fields", Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                } else {
                    firebaseFirestore.collection("New Intro Profiles").document(firebaseAuth.getUid())
                            .collection("New Users Intro").document(id)
                            .update("Telephone",telephone,"Address",address,"Postal",postal,"Budget",budget)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog.cancel();
                                    onBackPressed();
                                    Toast.makeText(UpdateProfileActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.cancel();
                                    Toast.makeText(UpdateProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }
        });
    }
}