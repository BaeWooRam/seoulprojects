package com.example.myapplication;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.myapplication.fragment.MainFragment;


public class MainActivity extends AppCompatActivity {
    private String api_key = "724f4c6a55766f6138304476465169";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this,Splash.class);
        startActivity(intent);

        MainFragment.NavigateToFragment(new MainFragment(),getSupportFragmentManager(), R.id.container,false);

    }



}
