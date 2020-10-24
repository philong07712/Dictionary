package com.example.dictionary.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.dictionary.R;
import com.example.dictionary.YourWordFragment;
import com.example.dictionary.databinding.ActivityMainBinding;
import com.example.dictionary.util.Constants;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private final int RecordAudioRequestCode = 255;
    ActivityMainBinding binding;
    private ActionBarDrawerToggle mToggle;
    private String TAG = MainActivity.class.getSimpleName();

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

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }
        BoardFragment fragment = new BoardFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().add(R.id.frame_fragment, fragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.tool_menu, menu);
        return true;
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
                setTitle("            FAVORITE");
                break;
            case R.id.eng_viet_menu:
                fragmentClass = BoardFragment.class;
                bundle.putInt(Constants.WORD.TYPE_ID, Constants.WORD.ENG_TYPE);
                setTitle("            ENG-VIET");
                break;
            case R.id.viet_eng_menu:
                fragmentClass = BoardFragment.class;
                bundle.putInt(Constants.WORD.TYPE_ID, Constants.WORD.VIET_TYPE);
                setTitle("            VIET-ENG");
                break;
            case R.id.your_menu:
                fragmentClass = YourWordFragment.class;
                setTitle("            YOUR WORDS");
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
    }

    private void setupDrawerContainer(NavigationView nv) {
        nv.setNavigationItemSelectedListener((item) -> {
            selectItemDrawer(item);
            return true;
        });
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Log.i(TAG, "onRequestPermissionsResult: ");
        }
    }
}