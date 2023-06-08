package com.example.financefit;

import static com.example.financefit.R.id.DateTime;
import static com.example.financefit.R.id.imageSend;
import static com.example.financefit.R.id.txtName;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.financefit.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class MainActivity extends AppCompatActivity {
    ImageView imgAdd, imgSend, imgReceive, imgInvest, imgProfileB, imgSummary, imgSettings, imgProfile;
    FloatingActionButton BtnFloat;
    TextView txtName, txtBudget;
    ActivityMainBinding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ArrayList<TransactionModel> transactionModelArrayList;
    ArrayList<UserName> userNameArrayList;
    ArrayList<UserProfile2> userProfile2ArrayList;
    TransactionAdapter transactionAdapter;
    private TextView TxtRecyclerText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        imgAdd = findViewById(R.id.addImg);
        imgInvest = findViewById(R.id.imageInvest);
        imgSummary = findViewById(R.id.imageSummary);
        imgProfile = findViewById(R.id.imageView3);
        imgProfile.setClipToOutline(true);
        imgProfileB = findViewById(R.id.imageProfile);
        imgReceive = findViewById(R.id.imageReceive);
        imgSend = findViewById(R.id.imageSend);
        imgSettings = findViewById(R.id.imageSettings);
        BtnFloat = findViewById(R.id.floatBtn);
        txtBudget = findViewById(R.id.monthlyBudget);
        txtName = findViewById(R.id.txtName);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        transactionModelArrayList = new ArrayList<>();
        userNameArrayList = new ArrayList<>();
        userProfile2ArrayList = new ArrayList<>();

        getUserInfo();

        binding.expenseView.setLayoutManager(new LinearLayoutManager(this));
        binding.expenseView.setHasFixedSize(true);

        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(MainActivity.this,Login.class));
                    finish();
                }
            }
        });
        binding.imageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Loan.class));
            }
        });
        binding.imageReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Income.class));
            }
        });
        binding.imageInvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Investing.class));

            }
        });
        binding.floatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Invest.class));
            }
        });

        binding.btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSignOutDialog();
            }
        });


        imgSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
            }
        });
        binding.addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTransaction.class);
                startActivity(intent);
            }
        });
        imgProfileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Profile.class);
                startActivity(intent);
            }
        });
        imgSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Summary.class);
                startActivity(intent);
            }
        });
        loadData();
        loadData1();
        loadData2();


    }

    private void getUserInfo() {
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

    private void createSignOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Logout")
                .setIcon(R.drawable.log)
                .setMessage("Are you sure you want to Logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebaseAuth.signOut();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        builder.create().show();
    }

    private void loadData2() {
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

    private void loadData1() {
        firebaseFirestore.collection("New Intro Profiles").document(firebaseAuth.getUid()).collection("New Users Intro")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot ds : task.getResult()) {
                            UserProfile2 model2 = new UserProfile2(ds.getString("Budget"));
                            userProfile2ArrayList.add(model2);

                            txtBudget.setText("₹"+ds.getString("Budget"));
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
        loadData1();
    }

    private void loadData() {
        firebaseFirestore.collection("Expenses").document(firebaseAuth.getUid()).collection("Notes")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        transactionModelArrayList.clear();
                        for (DocumentSnapshot ds : task.getResult()) {
                            TransactionModel model = new TransactionModel(ds.getString("amount"), ds.getString("desc"), ds.getString("id"), ds.getString("type"), ds.getString("date"));
                            transactionModelArrayList.add(model);

                        }
                        transactionAdapter = new TransactionAdapter(MainActivity.this, transactionModelArrayList);
                        binding.expenseView.setAdapter(transactionAdapter);
                        TxtRecyclerText = (TextView) binding.txtrecyclerText;
                        if (transactionModelArrayList.isEmpty()){
                            binding.expenseView.setVisibility(View.GONE);
                            TxtRecyclerText.setVisibility(View.VISIBLE);
                        } else{
                            binding.expenseView.setVisibility(View.VISIBLE);
                            TxtRecyclerText.setVisibility(View.GONE);
                        }

                    }
                });

    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.power)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to close the App ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

}