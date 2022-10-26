package com.example.healthteknoyadmin;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase rootNode, rootNode2;
    private DatabaseReference reference, reference2;
    private TextView toolbarTitle;
    private ImageView back;
    private Calendar calendar;
    private Date date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        back = findViewById(R.id.back);
        toolbarTitle = findViewById(R.id.toolbar_title);

        date = Calendar.getInstance().getTime();

        toolbarTitle.setText("Dashboard");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Can't go back any further", Toast.LENGTH_SHORT).show();
            }
        });

        rootNode = FirebaseDatabase.getInstance("https://teknoyhealthapp-default-rtdb.asia-southeast1.firebasedatabase.app/");
        reference = rootNode.getReference().child("User");

    }


    public void scanBarcode(View view) {
        scanBarcode();
    }

    public void scanBarcode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(BarcodeScanner.class);
        barcodeLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null){
            String barcodeScanned = result.getContents();
            System.out.println(barcodeScanned);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String scannedUser = String.valueOf(snapshot.child(barcodeScanned).child("barcode").getValue());
                    if(scannedUser.equals("YES")){
                        System.out.println(scannedUser);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("INPUT TEMPERATURE")
                                .setNegativeButton("HIGH", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        reference.child(barcodeScanned).child("temperature").setValue("HIGH");
                                        reference.child(barcodeScanned).child("timeVisit").setValue(String.valueOf(date));
                                        Toast.makeText(getApplicationContext(), "NOT ALLOWED", Toast.LENGTH_SHORT).show();
                                        scanBarcode();
                                    }
                                }).setPositiveButton("NORMAL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        reference.child(barcodeScanned).child("temperature").setValue("NORMAL");
                                        reference.child(barcodeScanned).child("timeVisit").setValue(String.valueOf(date));
                                        Toast.makeText(getApplicationContext(), "ALLOWED", Toast.LENGTH_SHORT).show();
                                        scanBarcode();
                                    }
                                }).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    });

    public void deleteBarcodes(View view) {
        deleteAllBarcode();
    }

    public void deleteAllBarcode(){

        rootNode2 = FirebaseDatabase.getInstance("https://teknoyhealthapp-default-rtdb.asia-southeast1.firebasedatabase.app/");
        reference2 = rootNode.getReference("User");

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Are you sure deleting all barcodes?")
                .setMessage("\nAfter deleting all barcodes, press OKAY.")
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        reference2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    String allUserName = String.valueOf(dataSnapshot.child("username").getValue());
                                    System.out.println(allUserName);
                                    reference2.child(allUserName).child("barcode").setValue("");
                                    reference2.child(allUserName).child("recentExposure").setValue("");
                                    reference2.child(allUserName).child("symptoms").setValue("");
                                }
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                                builder1.setTitle("CLOSE THE APP")
                                        .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                System.exit(0);
                                            }
                                        }).show();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }).show();
    }

    public void openUpdate(View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, UpdatePage.class));
                finish();
            }
        },100);
    }

    public void openUsers(View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, UserPage.class));
                finish();
            }
        },100);
    }
}