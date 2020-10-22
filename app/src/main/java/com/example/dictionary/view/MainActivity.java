package com.example.dictionary.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.dictionary.R;
import com.example.dictionary.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private ActionBarDrawerToggle mToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mToggle = new ActionBarDrawerToggle(this, binding.drawer, R.string.Open, R.string.Close);
        binding.drawer.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContainer(binding.nvBoard);

        BoardFragment fragment = new BoardFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().add(R.id.frame_fragment, fragment);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void selectItemDrawer(MenuItem item) {
//        Fragment myFragment = null;
//        Class fragmentClass;
//        switch (item.getItemId())
//        {
//            case R.id.db:
//                fragmentClass = DashBoardFragment.class;
//                break;
//            case R.id.activities:
//                fragmentClass = ActivityFragment.class;
//                break;
//            case R.id.setting:
//                fragmentClass = SettingFragment.class;
//                break;
//            case R.id.logout:
//                fragmentClass = LogoutFragment.class;
//                break;
//            case R.id.search:
//                fragmentClass = SearchFragment.class;
//                break;
//            default:
//                fragmentClass = EventFragment.class;
//        }
//        try {
//            myFragment = (Fragment) fragmentClass.newInstance();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.frame, myFragment).commit();
        item.setChecked(true);
        binding.drawer.closeDrawers();
//        setTitle(menuItem.getTitle());
    }

    private void setupDrawerContainer(NavigationView nv) {
        nv.setNavigationItemSelectedListener((item) -> {
            selectItemDrawer(item);
            return true;
        });
    }

}