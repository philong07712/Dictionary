package com.example.dictionary.view;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dictionary.data.EngDatabaseAccess;
import com.example.dictionary.data.viet_anh.VietDatabaseAccess;
import com.example.dictionary.databinding.DefinitionFragmentBinding;
import com.example.dictionary.model.Word;
import com.example.dictionary.util.Constants;
import com.example.dictionary.viewmodel.DefinitionViewModel;

public class DefinitionFragment extends Fragment {
    private final String TAG = DefinitionFragment.class.getSimpleName();
    private DefinitionViewModel mViewModel;
    private DefinitionFragmentBinding binding;
    private Word word;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DefinitionFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListener();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DefinitionViewModel.class);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            word = (Word) bundle.getSerializable(Constants.WORD.WORD_ID);
            initDefinition(word);
        }
        // TODO: Use the ViewModel
    }

    public void initDefinition(Word word) {
        binding.tvContent.setText(word.getContent());
        SpannableString spannableString = new SpannableString(Html.fromHtml(word.getDefinition()));
        binding.tvDescription.setText(spannableString);
    }

    public void initListener() {
        binding.fabFavorite.setOnClickListener(v -> {
            Log.i(TAG, "initListener: " + word.getId());
            if (word.getType() == Constants.WORD.ENG_TYPE) {
                EngDatabaseAccess access = EngDatabaseAccess.getInstance(getContext());
                access.open();
                access.addFavorite(word.getId());
            }
            else {
                VietDatabaseAccess access = VietDatabaseAccess.getInstance(getContext());
                access.open();
                access.addFavorite(word.getId());
            }
        });
    }
}