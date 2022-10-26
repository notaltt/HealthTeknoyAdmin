package com.example.healthteknoyadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class SummaryPage extends AppCompatActivity {

    private TextView approvedBarcode, declinedBarcode, totalBarcode, date, toolbarTitle, withSymptoms, withExposure, totalStatistics;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private ImageView imageView;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_page);


        rootNode = FirebaseDatabase.getInstance("https://teknoyhealthapp-default-rtdb.asia-southeast1.firebasedatabase.app/");
        reference = rootNode.getReference("User");

        approvedBarcode = findViewById(R.id.approvedBarcode);
        declinedBarcode = findViewById(R.id.declinedBarcode);
        totalBarcode = findViewById(R.id.totalBarcode);
        withSymptoms = findViewById(R.id.withSymptoms);
        withExposure = findViewById(R.id.withRecent);
        totalStatistics = findViewById(R.id.totalStatistics);
        toolbarTitle = findViewById(R.id.toolbar_title2);
        date = findViewById(R.id.textDate2);
        imageView = findViewById(R.id.back2);

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("EEE, MMMM d, yyyy");
        currentDate = simpleDateFormat.format(calendar.getTime());

        toolbarTitle.setText("Summary");
        date.setText(currentDate);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> exposure = new ArrayList<>();
                List<String> symptoms = new ArrayList<>();
                List<String> barcode = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String allExposure = String.valueOf(dataSnapshot.child("recentExposure").getValue());
                    String allSymptoms = String.valueOf(dataSnapshot.child("symptoms").getValue());
                    String allBarcodes = String.valueOf(dataSnapshot.child("barcode").getValue());

                    exposure.add(allExposure);
                    symptoms.add(allSymptoms);
                    barcode.add(allBarcodes);

                    int countExposure = Collections.frequency(exposure, "YES");
                    int countSymptoms = Collections.frequency(symptoms, "YES");
                    int totalStats = countExposure + countSymptoms;
                    int countNo = Collections.frequency(barcode, "NO");
                    int countYes = Collections.frequency(barcode, "YES");
                    int totalBarcode1 = countNo + countYes;

                    withExposure.setText(String.valueOf(countExposure));
                    withSymptoms.setText(String.valueOf(countSymptoms));
                    approvedBarcode.setText(String.valueOf(countYes));
                    declinedBarcode.setText(String.valueOf(countNo));
                    totalStatistics.setText(String.valueOf(totalStats));
                    totalBarcode.setText(String.valueOf(totalBarcode1));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SummaryPage.this, UserPage.class);
                startActivity(intent);
            }
        });

    }
}