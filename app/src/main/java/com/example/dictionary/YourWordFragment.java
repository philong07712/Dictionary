package com.example.dictionary;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dictionary.data.EngDatabaseAccess;
import com.example.dictionary.databinding.YourWordFragmentBinding;
import com.example.dictionary.model.Word;
import com.example.dictionary.util.ViewPagerSnapHelper;
import com.example.dictionary.view.adapter.YourWordAdapter;

import java.util.ArrayList;
import java.util.List;

public class YourWordFragment extends Fragment {

    private static final String TAG = YourWordFragment.class.getSimpleName();
    private YourWordViewModel mViewModel;
    private List<Word> wordList;
    private YourWordAdapter adapter;

    public static YourWordFragment newInstance() {
        return new YourWordFragment();
    }
    YourWordFragmentBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wordList = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = YourWordFragmentBinding.inflate(inflater, container, false);
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
        mViewModel = new ViewModelProvider(this).get(YourWordViewModel.class);
        // TODO: Use the ViewModel
        loadData();
    }

    public void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EngDatabaseAccess access = EngDatabaseAccess.getInstance(getContext());
                access.open();
                for (int i = 0; i < 5; i++) {
                    synchronized (wordList) {
                        wordList.add(access.getYourWord());
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    public void initRecyclerView() {
        adapter = new YourWordAdapter(getContext(), wordList);
        binding.rvYourWord.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.rvYourWord.setLayoutManager(layoutManager);
        PagerSnapHelper linearSnapHelper = new ViewPagerSnapHelper();
        linearSnapHelper.attachToRecyclerView(binding.rvYourWord);
        binding.rvYourWord.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i(TAG, "onScrolled: " + layoutManager.findLastVisibleItemPosition());
                if (layoutManager.findLastVisibleItemPosition() == wordList.size() - 1) {
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