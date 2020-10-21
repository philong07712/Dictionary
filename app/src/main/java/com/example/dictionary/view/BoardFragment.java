package com.example.dictionary.view;

import androidx.fragment.app.FragmentTransaction;
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

import com.example.dictionary.util.Constants;
import com.example.dictionary.R;
import com.example.dictionary.model.Word;
import com.example.dictionary.view.adapter.WordAdapter;
import com.example.dictionary.data.DatabaseAccess;
import com.example.dictionary.databinding.BoardFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class BoardFragment extends Fragment {

    private static final String TAG = BoardFragment.class.getSimpleName();
    private BoardViewModel mViewModel;
    private BoardFragmentBinding binding;
    private List<Word> words;
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
        List<Word> anhviet = new ArrayList<>();
        if (words.isEmpty()) {
            anhviet.addAll(databaseAccess.getWords());
        }
        else {
            anhviet.addAll(databaseAccess.getWords(words.size(), 20));

        }
        databaseAccess.close();
        words.clear();
        words.addAll(anhviet);
        adapter.notifyDataSetChanged();
    }

    public void initRecyclerView() {
        words = new ArrayList<>();
        adapter = new WordAdapter(getContext(), words);
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

}