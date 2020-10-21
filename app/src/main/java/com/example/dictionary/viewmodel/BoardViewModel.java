package com.example.dictionary.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dictionary.model.Word;

import java.util.ArrayList;
import java.util.List;

public class BoardViewModel extends ViewModel {
    public MutableLiveData<List<Word>> wordLiveData;
    public BoardViewModel() {
        wordLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Word>> getWords() {
        if (wordLiveData.getValue() == null) {
            wordLiveData.setValue(new ArrayList<>());
        }
        return wordLiveData;
    }

    public void setWords(List<Word> words) {
        if (wordLiveData.getValue() == null) {
            wordLiveData.setValue(new ArrayList<>());
        }
        wordLiveData.getValue().clear();
        wordLiveData.getValue().addAll(words);
    }
}