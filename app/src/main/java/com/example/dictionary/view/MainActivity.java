package com.example.dictionary.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.dictionary.FavoriteFragment;
import com.example.dictionary.R;
import com.example.dictionary.databinding.ActivityMainBinding;
import com.example.dictionary.util.Constants;
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
        Fragment myFragment = null;
        Bundle bundle = new Bundle();
        Class fragmentClass;
        switch (item.getItemId())
        {
            case R.id.favorite_menu:
                fragmentClass = FavoriteFragment.class;
                break;
            case R.id.eng_viet_menu:
                fragmentClass = BoardFragment.class;
                bundle.putInt(Constants.WORD.TYPE_ID, Constants.WORD.ENG_TYPE);
                break;
            case R.id.viet_eng_menu:
                fragmentClass = BoardFragment.class;
                bundle.putInt(Constants.WORD.TYPE_ID, Constants.WORD.VIET_TYPE);
                break;
//            case R.id.logout:
//                fragmentClass = LogoutFragment.class;
//                break;
//            case R.id.search:
//                fragmentClass = SearchFragment.class;
//                break;
            default:
                fragmentClass = FavoriteFragment.class;
        }
        try {
            myFragment = (Fragment) fragmentClass.newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        myFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_fragment, myFragment).commit();
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