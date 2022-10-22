package com.example.healthteknoyadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SummaryPage extends AppCompatActivity {

    private TextView approvedBarcode, declinedBarcode, noBarcode, date;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_page);


        rootNode = FirebaseDatabase.getInstance("https://teknoyhealthapp-default-rtdb.asia-southeast1.firebasedatabase.app/");
        reference = rootNode.getReference("User");

        approvedBarcode = findViewById(R.id.approvedBarcode);
        declinedBarcode = findViewById(R.id.declinedBarcode);
        noBarcode = findViewById(R.id.noBarcode);
        date = findViewById(R.id.date);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> exposure = new ArrayList<>();
                List<String> symptoms = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String allExposure = String.valueOf(dataSnapshot.child("recentExposure").getValue());
                    String allSymptoms = String.valueOf(dataSnapshot.child("symptoms").getValue());

                    exposure.add(allExposure);
                    symptoms.add(allSymptoms);

                    int countYes = Collections.frequency(exposure, "YES");
                    int countNo = Collections.frequency(exposure, "NO");
                    int countNull = Collections.frequency(exposure, "");

                    approvedBarcode.setText(String.valueOf(countYes));
                    declinedBarcode.setText(String.valueOf(countNo));
                    noBarcode.setText(String.valueOf(countNull));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}