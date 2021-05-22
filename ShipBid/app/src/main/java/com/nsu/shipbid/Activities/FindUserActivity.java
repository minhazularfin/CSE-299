package com.nsu.shipbid.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nsu.shipbid.Fragments.DriverInboxFragment;
import com.nsu.shipbid.Fragments.FindUserFragment;
import com.nsu.shipbid.Fragments.ProfileFragment;
import com.nsu.shipbid.Fragments.UserFragment;
import com.nsu.shipbid.R;

import java.util.Objects;

public class FindUserActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_goods);
        Objects.requireNonNull(getSupportActionBar()).hide();
        loadFragment(new FindUserFragment());
        bindWidgets();
        bindListeners();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment;
            switch (menuItem.getItemId()) {
                case R.id.rides:
                    fragment = new UserFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.profile:
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.inbox:
                    fragment = new DriverInboxFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.create:
                    fragment = new FindUserFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.exit_title)
                .setMessage(R.string.exit_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    private void bindWidgets() {
        bottomNavigationView = findViewById(R.id.navigation);
    }

    private void bindListeners() {
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.create);
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
