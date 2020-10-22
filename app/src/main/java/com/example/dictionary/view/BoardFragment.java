package com.example.dictionary.view;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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
import android.widget.SearchView;

import com.example.dictionary.util.Constants;
import com.example.dictionary.R;
import com.example.dictionary.model.Word;
import com.example.dictionary.view.adapter.WordAdapter;
import com.example.dictionary.data.DatabaseAccess;
import com.example.dictionary.databinding.BoardFragmentBinding;
import com.example.dictionary.viewmodel.BoardViewModel;

import java.util.ArrayList;
import java.util.List;

public class BoardFragment extends Fragment {

    private static final String TAG = BoardFragment.class.getSimpleName();
    private BoardViewModel mViewModel;
    private BoardFragmentBinding binding;
    private List<Word> wordList;
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
        initSearch();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(BoardViewModel.class);
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
        // TODO: Use the ViewModel
    }

    public void loadData() {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext());
        databaseAccess.open();
        List<Word> anhviet = new ArrayList<>();
        if (wordList.isEmpty()) {
            anhviet.addAll(databaseAccess.getWords());
        }
        else {
            anhviet.addAll(databaseAccess.getWords(wordList.size(), 20));

        }
        databaseAccess.close();
        wordList.clear();
        wordList.addAll(anhviet);
        adapter.setWords(wordList);
        adapter.notifyDataSetChanged();
    }

    public void initRecyclerView() {
        wordList = new ArrayList<>();
        adapter = new WordAdapter(getContext(), wordList);
        adapter.setOnClickListener((word, position) -> {
            navigateDefinition(word);
        });
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
                if (layoutManager.findLastVisibleItemPosition() == wordList.size() - 1
                    && !adapter.isSearching) {
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

    public void initSearch() {
        binding.svBoard.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }


    public void navigateDefinition(Word word) {
        DefinitionFragment fragment = new DefinitionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.WORD.WORD_ID, word);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        mViewModel.setWords(wordList);
    }
}