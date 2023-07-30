package com.devrahul.financefit;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Introprofile extends AppCompatActivity {
    ImageView SettingsImg, uploadImage;
    Button btnChange, button;
    EditText edtTelephone, edtAddress, edtPostal, edtBudget;
    TextView textName, textView2, textView15;
    String imageURL;
    Uri uri;
    FirebaseFirestore firebaseFirestore;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    ArrayList<UserModel> userModelArrayList;
    ProgressDialog progressDialog;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introprofile);
        uploadImage = findViewById(R.id.imageProfile);
        uploadImage.setClipToOutline(true);
        textName = findViewById(R.id.textName);
        textView2 = findViewById(R.id.textView2);
        btnChange = findViewById(R.id.btnChange);
        button = findViewById(R.id.button);
        edtAddress = findViewById(R.id.edtText2);
        edtPostal = findViewById(R.id.textView5);
        edtTelephone = findViewById(R.id.edtText1);
        SettingsImg = findViewById(R.id.SettingsImg);
        edtBudget = findViewById(R.id.edtBudget);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        firebaseFirestore = FirebaseFirestore.getInstance();
        userModelArrayList = new ArrayList<>();
        textView15 = findViewById(R.id.textView15);
        int maxLength =8;
        edtBudget.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        loadData2();

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Introprofile.this, "Set Up Your Profile First!", Toast.LENGTH_SHORT).show();
            }
        });



        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            uploadImage.setImageURI(uri);
                        }
                    }
                }
        );
        SettingsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Introprofile.this, "Please Set Up Your Profile First!", Toast.LENGTH_SHORT).show();
            }
        });
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uri!=null){
                    saveData();
                } else {
                    Toast.makeText(Introprofile.this, "Please Insert Your Profile Image", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
    public void saveData(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Android Image")
                .child(uri.getLastPathSegment());
        AlertDialog.Builder builder= new AlertDialog.Builder(Introprofile.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                Uri uriImage = uriTask.getResult();
                imageURL = uriImage.toString();
                uploadData();
                uploadimg();
                dialog.dismiss();
                Intent intent = new Intent(Introprofile.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });

    }

    private void uploadimg() {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("image",imageURL);
        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).updateChildren(userMap);
    }

    private void uploadData() {
        String Telephone =edtTelephone.getText().toString();
        String Address = edtAddress.getText().toString();
        String Postal = edtPostal.getText().toString();
        String Budget = edtBudget.getText().toString();
        //String id = UUID.randomUUID().toString();
        String id = firebaseAuth.getCurrentUser().getUid();
        Map<String, Object> transaction3 = new HashMap<>();
        transaction3.put("Id", id);
        transaction3.put("Telephone", Telephone);
        transaction3.put("Address",Address);
        transaction3.put("Postal", Postal);
        transaction3.put("Budget" ,Budget);
        firebaseFirestore.collection("New Intro Profiles").document(firebaseAuth.getUid()).collection("New Users Intro").document(id)
                .set(transaction3)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        edtTelephone.setText("");
                        edtAddress.setText("");
                        edtPostal.setText("");
                        edtBudget.setText("");
                        Toast.makeText(Introprofile.this, "Profile Saved", Toast.LENGTH_SHORT).show();

                    }
                });

        Intent intent = new Intent(Introprofile.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void loadData2() {
        firebaseFirestore.collection("New SignUp Profiles").document(firebaseAuth.getUid()).collection("New Users")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot ds : task.getResult()) {
                            UserModel model = new UserModel(ds.getString("Email"), ds.getString("Name"), ds.getString("Mobile No"));
                            userModelArrayList.add(model);

                            textName.setText(ds.getString("Name"));
                            textView2.setText(ds.getString("Email"));
                            textView15.setText(ds.getString("Mobile No"));

                        }


                    }
                });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(Introprofile.this, "Please Set Up Your Profile First!", Toast.LENGTH_SHORT).show();
        return;
    }
}