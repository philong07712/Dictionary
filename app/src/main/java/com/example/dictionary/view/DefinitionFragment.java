package com.example.dictionary.view;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dictionary.R;
import com.example.dictionary.model.Word;
import com.example.dictionary.util.Constants;

public class DefinitionFragment extends Fragment {
    private final String TAG = DefinitionFragment.class.getSimpleName();
    private DefinitionViewModel mViewModel;

    public static DefinitionFragment newInstance() {
        return new DefinitionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.definition_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DefinitionViewModel.class);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Word word = (Word) bundle.getSerializable(Constants.WORD.WORD_ID);
            Log.i(TAG, "onActivityCreated: " + word.getContent());
        }
        // TODO: Use the ViewModel
    }

}