package com.example.financefit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.example.financefit.databinding.ActivityMainBinding;
import com.example.financefit.databinding.ActivitySummaryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Summary extends AppCompatActivity {
    ActivitySummaryBinding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    int sumExpense = 0;
    int sumIncome = 0;
    int sumIncomeTotal = 0;
    int sumLoan = 0;
    ArrayList<UserProfile2> userProfile2ArrayList;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        binding = ActivitySummaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userProfile2ArrayList = new ArrayList<>();
        loadData1();
        loadData3();
        loadData4();
    }

    private void loadData3() {
        firebaseFirestore.collection("Income").document(firebaseAuth.getUid()).collection("Data")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot ds : task.getResult()) {
                            PortfolioModel model = new PortfolioModel(ds.getString("amount"), ds.getString("Name"), ds.getString("StartDate"), ds.getString("EndDate"), ds.getString("type"), ds.getString("Id"),ds.getString("TypeA"));
                            int amount = Integer.parseInt(ds.getString("amount"));
                            if (ds.getString("TypeA").equals("Job Salary") || ds.getString("TypeA").equals("Business") || ds.getString("TypeA").equals("Agriculture") || ds.getString("TypeA").equals("Other Sources")) {
                                sumIncomeTotal = sumIncomeTotal + amount;
                            }
                            binding.txtComInc.setText("₹"+String.valueOf(sumIncomeTotal));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Summary.this, "No Data Added", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void loadData4() {
        firebaseFirestore.collection("Loan").document(firebaseAuth.getUid()).collection("Data")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot ds : task.getResult()) {
                            PortfolioModel model = new PortfolioModel(ds.getString("amount"), ds.getString("Name"), ds.getString("StartDate"), ds.getString("EndDate"), ds.getString("type"), ds.getString("Id"),ds.getString("TypeA"));
                            int type = Integer.parseInt(ds.getString("type"));
                            if (ds.getString("TypeA").equals("Home Loan") || ds.getString("TypeA").equals("Education Loan") || ds.getString("TypeA").equals("Vehicle Loan") || ds.getString("TypeA").equals("Other Loans")) {
                                sumLoan = sumLoan + type;
                            }
                            binding.txtComLoan.setText("₹"+String.valueOf(sumLoan));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    private void loadData2() {
        progressDialog.show();
        progressDialog.setMessage("Loading...");
        double[] num1 = new double[1];
        double[] num2 = new double[1];
        double[] numBal = new double[1];
        firebaseFirestore.collection("New Intro Profiles").document(firebaseAuth.getUid()).collection("New Users Intro")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot ds : task.getResult()) {
                            UserProfile2 model2 = new UserProfile2(ds.getString("Budget"));
                            userProfile2ArrayList.add(model2);
                            num1[0] = Double.parseDouble(ds.getString("Budget"));
                            num2[0] = Double.parseDouble(binding.txtBalance.getText().toString());
                            numBal[0] = num1[0]+num2[0];
                            if (numBal[0]<0){
                                binding.txtComBal.setText(String.valueOf(numBal[0]));
                                binding.txtComBal.setTextColor(Color.RED);
                                progressDialog.cancel();
                            } else {
                                binding.txtComBal.setText(String.valueOf(numBal[0]));
                                binding.txtComBal.setTextColor(Color.GREEN);
                                progressDialog.cancel();

                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.cancel();
                    }
                });
    }

    private void loadData1() {
        progressDialog.show();
        progressDialog.setMessage("Loading...");
        firebaseFirestore.collection("Expenses").document(firebaseAuth.getUid()).collection("Notes")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot ds : task.getResult()) {
                            TransactionModel model = new TransactionModel(ds.getString("amount"), ds.getString("desc"), ds.getString("id"), ds.getString("type"), ds.getString("date"));
                            int amount = Integer.parseInt(ds.getString("amount"));
                            if (ds.getString("type").equals("Expense")){
                                sumExpense= sumExpense+amount;
                            } else{
                                sumIncome=sumIncome+amount;
                            }
                            binding.txtIncome.setText(String.valueOf(sumIncome));
                            binding.txtExpense.setText(String.valueOf(sumExpense));
                            binding.txtBalance.setText(String.valueOf(sumIncome-sumExpense));
                            loadData2();
                            progressDialog.cancel();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.cancel();

                    }
                });

    }
}