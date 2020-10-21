package com.example.dictionary;

import android.content.Context;
import android.renderscript.ScriptGroup;
import android.text.Html;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary.databinding.ItemBoardBinding;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {
    public final String TAG = WordAdapter.class.getSimpleName();
    Context context;
    List<Word> words;

    public WordAdapter(Context context, List<Word> words) {
        this.context = context;
        this.words = words;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBoardBinding binding = ItemBoardBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new WordViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        holder.binding.tvWord.setText(words.get(position).getContent());
        String definitionString = words.get(position).getDefinition();
        int start = definitionString.indexOf("<li>");
        int end = definitionString.indexOf("</li>");
        if (start != -1 || end != -1) {
            String subString = definitionString.substring(start + 4, end);
            SpannableString spannableString = new SpannableString(Html.fromHtml(subString));
            holder.binding.tvDefinition.setText(spannableString);

        }
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        ItemBoardBinding binding;
        public WordViewHolder(ItemBoardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
