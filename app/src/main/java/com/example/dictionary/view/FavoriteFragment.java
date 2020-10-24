package com.example.dictionary.view;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dictionary.viewmodel.FavoriteViewModel;
import com.example.dictionary.R;
import com.example.dictionary.data.EngDatabaseAccess;
import com.example.dictionary.data.viet_anh.VietDatabaseAccess;
import com.example.dictionary.databinding.FavoriteFragmentBinding;
import com.example.dictionary.model.Word;
import com.example.dictionary.util.Constants;
import com.example.dictionary.view.adapter.WordAdapter;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {
    private final String TAG = FavoriteFragment.class.getSimpleName();
    private FavoriteViewModel mViewModel;
    FavoriteFragmentBinding binding;
    private List<Word> wordList;
    private WordAdapter adapter;

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FavoriteFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        mViewModel.getWords().observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                if (words.isEmpty()) {
                    loadData();
                }
                else {
                    wordList.addAll(words);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    public void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EngDatabaseAccess databaseAccess = EngDatabaseAccess.getInstance(getContext());
                databaseAccess.open();
                List<Word> words = databaseAccess.getFavorite();
                wordList.addAll(words);

                VietDatabaseAccess databaseAccess1 = VietDatabaseAccess.getInstance(getContext());
                databaseAccess1.open();
                wordList.addAll(databaseAccess1.getFavorite());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "run: " + words.size());
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    public void initRecyclerView() {
        wordList = new ArrayList<>();
        adapter = new WordAdapter(getContext(), wordList, Constants.WORD.ENG_TYPE);
        binding.rvFavorite.setAdapter(adapter);
        binding.rvFavorite.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setOnClickListener((word, position) -> {
            navigateDefinition(word);
        });
    }

    public void navigateDefinition(Word word) {
        DefinitionFragment fragment = new DefinitionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.WORD.WORD_ID, word);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        mViewModel.setWords(wordList);
    }
}