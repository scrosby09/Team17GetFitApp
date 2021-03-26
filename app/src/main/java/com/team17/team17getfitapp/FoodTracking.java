package com.team17.team17getfitapp;

import com.team17.*;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FoodTracking extends AppCompatActivity implements View.OnClickListener, CaloriesAdapter.OnValueChanged {

    ArrayList<CaloriesModel> modelArrayList;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabase;
    private ListView itemListView;
    private CaloriesAdapter adapter;
    private SessionManager sessionManager;
    private TextView calories_total;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_foodtracking);

        sessionManager = new SessionManager(getApplicationContext());

        modelArrayList = new ArrayList<>();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("Calories");

        Button save_button = (Button) findViewById(R.id.save_button);
        itemListView = (ListView) findViewById(R.id.listView);
        calories_total = (TextView) findViewById(R.id.calories_total);
        save_button.setOnClickListener(this);

        // Get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        System.out.println(" getKey " + mFirebaseDatabase.getKey());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("dataSnapshot " + dataSnapshot.getValue());

                modelArrayList.clear();

                // Iterate through nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    System.out.println(" postSnapshot " + postSnapshot.getValue());
                    CaloriesModel artist = postSnapshot.getValue(CaloriesModel.class);
                    modelArrayList.add(artist);
                }

                adapter = new CaloriesAdapter(FoodTracking.this, modelArrayList);
                itemListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_button:
                logUserCalories(sessionManager.getSharedPrefItem(SessionManager.KEY_ID), Long.parseLong(calories_total.getText().toString()));
                break;
        }
    }

    private void logUserCalories(String id, long val) {

        DatabaseReference userCaloriesDatabase = mFirebaseInstance.getReference("UserCalories");
        long dateInMillisecond = Calendar.getInstance().getTimeInMillis();
        Date date = new Date(dateInMillisecond);
        SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yy", Locale.getDefault());
        String dateText = df2.format(date);


        CaloriesModel model = new CaloriesModel(dateText, val);

        userCaloriesDatabase.child(id).child(dateText).setValue(model);
    }

    @Override
    public void onValueChange() {
        long loopValue = 0;
        long[] loop = new long[caloriesModelList.size()];
        for (int i = 0; i < caloriesModelList.size(); i++) {
            System.out.println(" int " + i);
            loop[i] = caloriesModelList.get(i).getFinalCalorieValue();
            loopValue = loopValue + loop[i];
        }
        calories_total.setText(String.valueOf(loopValue));
    }

}