package com.example.healthteknoyadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserPage extends AppCompatActivity {

    private SearchView search_bar;
    private RecyclerView listviewUser;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private Adapter adapter;
    private Button summaryButton;
    private TextView toolbarTitle;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        search_bar = findViewById(R.id.search_bar);
        listviewUser = findViewById(R.id.listviewUsers);
        summaryButton = findViewById(R.id.summaryButton);
        toolbarTitle = findViewById(R.id.toolbar_title2);
        imageView = findViewById(R.id.back2);

        toolbarTitle.setText("Users");

        rootNode = FirebaseDatabase.getInstance("https://teknoyhealthapp-default-rtdb.asia-southeast1.firebasedatabase.app/");
        reference = rootNode.getReference().child("User");

        listviewUser.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(reference, User.class)
                        .build();

        adapter = new Adapter(options);
        listviewUser.setAdapter(adapter);


        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
            }
        });

        summaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(UserPage.this, SummaryPage.class));
                        finish();
                    }
                },100);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserPage.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    private void search(String s){
        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(reference.orderByChild("username").startAt(s).endAt(s+"\uf8ff"), User.class)
                .build();
        adapter = new Adapter(options);
        adapter.startListening();
        listviewUser.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}