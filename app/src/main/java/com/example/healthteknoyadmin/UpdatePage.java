package com.example.healthteknoyadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UpdatePage extends AppCompatActivity {

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private Button buttonUpdate;
    private EditText inputUpdate;
    private TextView toolbarTitle, textDate;
    private ImageView imageView;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_page2);

        buttonUpdate = findViewById(R.id.buttonUpdate);
        inputUpdate = findViewById(R.id.inputUpdate);
        toolbarTitle = findViewById(R.id.toolbar_title2);
        imageView = findViewById(R.id.back2);
        textDate = findViewById(R.id.textDate);

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("EEE, MMMM d, yyyy");
        currentDate = simpleDateFormat.format(calendar.getTime());

        toolbarTitle.setText("Announcement");
        textDate.setText(currentDate);

        rootNode = FirebaseDatabase.getInstance("https://teknoyhealthapp-default-rtdb.asia-southeast1.firebasedatabase.app/");
        reference = rootNode.getReference("User");

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String allUserName = String.valueOf(dataSnapshot.child("username").getValue());
                            String update = inputUpdate.getText().toString();
                            reference.child(allUserName).child("update").setValue("[" +currentDate+ "]\n"+update);
                            Toast.makeText(UpdatePage.this, "POSTED AN UPDATE", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdatePage.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}