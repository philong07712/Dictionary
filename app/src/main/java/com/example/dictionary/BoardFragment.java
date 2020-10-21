package com.example.dictionary;

import androidx.lifecycle.ViewModelProviders;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dictionary.databinding.BoardFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class BoardFragment extends Fragment {

    private static final String TAG = BoardFragment.class.getSimpleName();
    private BoardViewModel mViewModel;
    private BoardFragmentBinding binding;
    private List<String> words;
    private WordAdapter adapter;
    private LinearLayoutManager layoutManager;

    public static BoardFragment newInstance() {
        return new BoardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BoardFragmentBinding.inflate(inflater, container, false);
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
        mViewModel = ViewModelProviders.of(this).get(BoardViewModel.class);
        loadData();
        // TODO: Use the ViewModel
    }

    public void loadData() {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext());
        databaseAccess.open();
        List<String> anhviet = new ArrayList<>();
        if (words.isEmpty()) {
            anhviet.addAll(databaseAccess.getWords());
        }
        else {
            anhviet.addAll(databaseAccess.getWords(words.size(), 20));

        }
        Log.i(TAG, "loadData: anhviet " + anhviet.size());
        databaseAccess.close();
        words.clear();
        words.addAll(anhviet);
        Log.i(TAG, "loadData: words " + words.size());
        adapter.notifyDataSetChanged();
    }

    public void initRecyclerView() {
        words = new ArrayList<>();
        adapter = new WordAdapter(getContext(), words);
        binding.rvBoard.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getContext());
        binding.rvBoard.setLayoutManager(layoutManager);
        binding.rvBoard.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i(TAG, "onScrolled: " + layoutManager.findLastVisibleItemPosition());
                if (layoutManager.findLastVisibleItemPosition() == words.size() - 1) {
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "run: ");
                            loadData();
                        }
                    });
                }
            }
        });
    }

}