package com.team17.team17getfitapp;

import com.team17.team17getfitapp.UserProfiles;
import com.team17.team17getfitapp.R;
import com.team17.team17getfitapp.SessionManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import javax.*;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;
    private FirebaseAuth.AuthStateListener mauthstatelistner;
    private DatabaseReference mFirebaseDatabase;

    public LoginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        initViews();
        mAuth = FirebaseAuth.getInstance();
        mauthstatelistner = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            }
            if(firebaseAuth.getCurrentUser()!=null)

            {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        }
    }

    ;
        mAuth.addAuthStateListener(mauthstatelistner);
}

    private void initViews() {
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        TextView registerView = (TextView) findViewById(R.id.registerView);
        registerView.setOnClickListener(this);
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                final String email = emailEditText.getText().toString().trim();
                final String password = passwordEditText.getText().toString().trim();

                Query query = mFirebaseDatabase.child("Users").orderByChild("userEmail").equalTo(email);
                query.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            for (DataSnapshot user : dataSnapshot.getChildren()) {

                                UserProfiles userProfilesBean = user.getValue(UserProfiles.class);

                                assert userProfilesBean != null;
                                if (userProfilesBean.getUserPassword().equalsIgnoreCase(password)) {
                                    SessionManager sessionManager = new SessionManager(getApplicationContext());
                                    sessionManager.createLoginSession(userProfilesBean.getId(), email, password);
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    finish();

                                } else {
                                    Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_LONG).show();
                                }
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "User not found int the database", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Sign In Problem", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
    }
}

