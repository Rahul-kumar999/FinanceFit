package com.devrahul.financefit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.MyViewHolder>{
    Context context;
    ArrayList<PortfolioModel> portfolioModelArrayList;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    public LoanAdapter(Context context, ArrayList<PortfolioModel> portfolioModelArrayList) {
        this.context = context;
        this.portfolioModelArrayList  = portfolioModelArrayList;
    }

    @NonNull
    @Override
    public LoanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_loan, parent,false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull LoanAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        PortfolioModel model = portfolioModelArrayList.get(position);
        String priority = model.getTypeA();
        if (priority.equals("Home Loan")){
            holder.Type.setText("Home Loan");
        } else if (priority.equals("Education Loan")) {
            holder.Type.setText("Education Loan");

        } else if (priority.equals("Vehicle Loan")) {
            holder.Type.setText("Vehicle Loan");

        } else {
            holder.Type.setText("Other Loans");

        }

        holder.amount.setText(model.getAmount());
        holder.StartDate.setText(model.getStartDate());
        holder.EndDate.setText(model.getEndDate());
        holder.Name.setText(model.getName());
        holder.subType.setText("EMI: ₹"+model.getType());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context)
                        .setIcon(R.drawable.alertnoti)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to Delete the Loan Details ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                firebaseAuth = FirebaseAuth.getInstance();
                                firebaseFirestore= FirebaseFirestore.getInstance();
                                String id = portfolioModelArrayList.get(position).getId();
                                firebaseFirestore.collection("Loan").document(firebaseAuth.getUid())
                                        .collection("Data")
                                        .document(id).delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(context, MainActivity.class);
                                                context.startActivity(intent);


                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                return false;

            }
        });

    }

    @Override
    public int getItemCount() {
        return portfolioModelArrayList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView amount, Name,StartDate,EndDate, Type, subType;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            amount =itemView.findViewById(R.id.amount);
            Name =itemView.findViewById(R.id.IncomeLoss);
            StartDate =itemView.findViewById(R.id.StartDate);
            EndDate = itemView.findViewById(R.id.EndDate);
            Type =itemView.findViewById(R.id.priority_one);
            subType = itemView.findViewById(R.id.Type);
        }
    }


}

