package com.example.smaproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class History extends AppCompatActivity {

    RecyclerView recylerHistory;
    DatabaseReference rootRef,ref;
    FirebaseUser currentUser;
    DayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        recylerHistory= findViewById(R.id.recylerHistory);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        final String user_id = currentUser.getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();

        ref= rootRef.child("Users").child(user_id).child("History");
        recylerHistory.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<UserNeeds> options = new FirebaseRecyclerOptions.Builder<UserNeeds>()
                .setQuery(ref, UserNeeds.class)
                .build();


        adapter = new DayAdapter(options);
        recylerHistory.setAdapter(adapter);
    }

    @Override protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stoping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }
}