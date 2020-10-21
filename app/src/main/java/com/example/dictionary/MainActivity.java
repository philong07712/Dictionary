package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BoardFragment fragment = new BoardFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().add(R.id.container_fragment, fragment);
        transaction.commit();
    }
}