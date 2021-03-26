package com.team17.team17getfitapp;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText email, password, userName, weight, height;
    private Button register_button;
    private EditText register_name;
    private EditText register_email;
    private EditText register_password;
    private EditText register_height;
    private EditText register_weight;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        initViews();
    }

    private void initViews() {
        register_name = (EditText) findViewById(R.id.register_name);
        register_email = (EditText) findViewById(R.id.register_email);
        register_password = (EditText) findViewById(R.id.register_password);
        register_weight = (EditText) findViewById(R.id.register_weight);
        register_height = (EditText) findViewById(R.id.register_height);
        TextView registerView = (TextView) findViewById(R.id.registerView);
        registerView.setOnClickListener(this);
        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.register_button) {
            final String email = register_email.getText().toString();
            final String password = register_password.getText().toString();
            final String name = register_name.getText().toString();
            final String weight = register_weight.getText().toString();
            final String height = register_height.getText().toString();


            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    System.out.println(" task " + task);
                    System.out.println(" task successful " + task.isSuccessful());
                    System.out.println(" task complete " + task.isComplete());
                    if (task.isSuccessful()) {
                        String key_id = databaseReference.push().getKey();
                        UserProfiles ufo = new UserProfiles(key_id, email, password, name, weight, height);
                        assert key_id != null;
                        databaseReference.child(key_id).setValue(ufo);


                        SessionManager sessionManager = new SessionManager(getApplicationContext());
                        sessionManager.createLoginSession(key_id, email, password);

                        Toast.makeText(RegisterActivity.this, "Registered Successfully.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this, email + " \n " + password, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}