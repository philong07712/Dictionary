package com.example.dictionary.view;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.dictionary.data.viet_anh.VietDatabaseAccess;
import com.example.dictionary.util.Constants;
import com.example.dictionary.R;
import com.example.dictionary.model.Word;
import com.example.dictionary.view.adapter.WordAdapter;
import com.example.dictionary.data.EngDatabaseAccess;
import com.example.dictionary.databinding.BoardFragmentBinding;
import com.example.dictionary.viewmodel.BoardViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BoardFragment extends Fragment {

    private static final String TAG = BoardFragment.class.getSimpleName();
    private BoardViewModel mViewModel;
    private BoardFragmentBinding binding;
    private List<Word> wordList;
    private WordAdapter adapter;
    private LinearLayoutManager layoutManager;
    private int mType = 0;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;

    public static BoardFragment newInstance() {
        return new BoardFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mType = bundle.getInt(Constants.WORD.TYPE_ID);
        }
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
        initSpeechRecognize();
        initListener();
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Word> anhviet = new ArrayList<>();
                if (mType == Constants.WORD.VIET_TYPE) {
                    VietDatabaseAccess databaseAccess = VietDatabaseAccess.getInstance(getContext());
                    databaseAccess.open();
                    if (wordList.isEmpty()) {
                        anhviet.addAll(databaseAccess.getWords());
                    }
                    else {
                        anhviet.addAll(databaseAccess.getWords(wordList.size(), 20));
                    }
                }
                else {
                    EngDatabaseAccess databaseAccess = EngDatabaseAccess.getInstance(getContext());
                    databaseAccess.open();
                    if (wordList.isEmpty()) {
                        anhviet.addAll(databaseAccess.getWords());
                    }
                    else {
                        anhviet.addAll(databaseAccess.getWords(wordList.size(), 20));
                    }
                }
                wordList.clear();
                wordList.addAll(anhviet);
                adapter.setWords(wordList);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();

    }

    public void initListener() {
        binding.btnMic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP){
                    Log.i(TAG, "onTouch: stop");
                    binding.btnMic.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_mic_black_24));
                    speechRecognizer.stopListening();
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    Log.i(TAG, "onTouch: start");
                    binding.btnMic.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_mic_red_24));
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                return false;
            }
        });
    }

    public void initRecyclerView() {
        wordList = new ArrayList<>();
        adapter = new WordAdapter(getContext(), wordList, mType);
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
                .replace(R.id.frame_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void initSpeechRecognize() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getActivity());

        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS,true);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.i(TAG, "onBeginningOfSpeech: ");
            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {
                Log.i(TAG, "onError: " + error);
            }

            @Override
            public void onResults(Bundle results) {
                Log.i(TAG, "onResults: ");
                ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                binding.svBoard.setQuery(data.get(0), false);
                updateSearchView(data.get(0));
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                ArrayList<String> data =
                        partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                ArrayList<String> unstableData =
                        partialResults.getStringArrayList("android.speech.extra.UNSTABLE_TEXT");
                String mResult = data.get(0) + unstableData.get(0);
                updateSearchView(mResult);
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
                Log.i(TAG, "onEvent: ");
            }
        });
    }

    public void updateSearchView(String searchText) {
        binding.svBoard.setIconified(false);
        binding.svBoard.setQuery(searchText, false);
        binding.svBoard.clearFocus();
    }

    @Override
    public void onPause() {
        super.onPause();
        mViewModel.setWords(wordList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }
}