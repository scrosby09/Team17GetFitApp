package com.team17.team17getfitapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserHistoryActivity extends AppCompatActivity {
    ArrayList<CaloriesModel> modelArrayList;
    private DatabaseReference mFirebaseDatabase;
    private CalorieHistoryAdapter adapter;
    private ListView listView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        SessionManager sessionManager = new SessionManager(getApplicationContext());


        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("UserCalories").child(sessionManager.getSharedPrefItem(SessionManager.KEY_ID));
        listView = (ListView) findViewById(R.id.history_listView);
        modelArrayList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("Initial Outlook " + dataSnapshot.getValue());

                modelArrayList.clear();

                // Iterating through the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    System.out.println(" Post Outlook " + postSnapshot.getValue());

                    // Get the item
                    CaloriesModel artist = postSnapshot.getValue(CaloriesModel.class);

                    // Add item to the list
                    modelArrayList.add(artist);
                }
                adapter = new CalorieHistoryAdapter(UserHistoryActivity.this, modelArrayList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}