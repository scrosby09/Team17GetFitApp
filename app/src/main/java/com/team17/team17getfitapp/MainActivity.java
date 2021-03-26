package com.team17.team17getfitapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(getApplicationContext());
        System.out.println(" session " + sessionManager.isLoggedIn());
        if (!sessionManager.isLoggedIn()) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        TextView nameNav = header.findViewById(R.id.nameTextView);
        TextView emailNav = header.findViewById(R.id.emailTextView);
        nameNav.setText("User Name");
        emailNav.setText(sessionManager.getUserData().get(SessionManager.KEY_EMAIL));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {

            startActivity(new Intent(this, FoodTrakingFragment.class));

        } else if (id == R.id.nav_gallery) {

            startActivity(new Intent(this, ExerciseActivity.class));

        } else if (id == R.id.nav_history) {
            startActivity(new Intent(this, UserHistoryActivity.class));

        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));

        } else if (id == R.id.nav_send) {
            startActivity(new Intent(MainActivity.this, UserProfileActivity.class));

        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DrawerLayout drawer = findViewById(R.id.drawer_layout);
                    drawer.openDrawer(Gravity.LEFT);
                }
            });
            alertDialogBuilder.setMessage("Confirm Logout ?");
            alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sessionManager.logoutUser();
                    finish();
                }
            });
            alertDialogBuilder.create().show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}