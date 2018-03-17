package com.android.nanodegree.moviesapp.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.nanodegree.moviesapp.R;
import com.android.nanodegree.moviesapp.ui.fragment.HomeFragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new HomeFragment()).commit();
    }
}
