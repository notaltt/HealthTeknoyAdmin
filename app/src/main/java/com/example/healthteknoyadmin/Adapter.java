package com.example.healthteknoyadmin;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Adapter extends FirebaseRecyclerAdapter<User, Adapter.ViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     *
     */

    public Adapter(@NonNull FirebaseRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull User model) {
        holder.name.setText(model.getFullName());
        holder.email.setText(model.getEmail());
        holder.phone.setText(model.getPhoneNumber());
        holder.username.setText(model.getUsername());
        holder.recent.setText(model.getRecentExposure());
        holder.symptoms.setText(model.getSymptoms());

        if(model.getRecentExposure().equals("YES") && model.getSymptoms().equals("YES")){
            holder.cardView.setCardBackgroundColor(Color.GREEN);
        }if(model.getRecentExposure().equals("NO") && model.getSymptoms().equals("NO")){
            holder.cardView.setCardBackgroundColor(Color.RED);
        }if(model.getRecentExposure().equals("") && model.getSymptoms().equals("")) {
            holder.recent.setText("DIDN'T MAKE A BARCODE");
            holder.symptoms.setText("DIDN'T MAKE A BARCODE");
       }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_layout,parent,false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, email, phone, username, recent, symptoms;
        CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            phone = itemView.findViewById(R.id.phone);
            username = itemView.findViewById(R.id.username);
            recent = itemView.findViewById(R.id.recent);
            symptoms = itemView.findViewById(R.id.symptoms);
            cardView = itemView.findViewById(R.id.cardView);
        }


    }
}
