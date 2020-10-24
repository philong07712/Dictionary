package com.example.dictionary.view.adapter;

import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary.databinding.YourWordItemBinding;
import com.example.dictionary.model.Word;

import java.util.List;
import java.util.Random;

public class YourWordAdapter extends RecyclerView.Adapter<YourWordAdapter.YourWordViewHolder> {

    Context context;
    List<Word> wordList;

    public YourWordAdapter(Context context, List<Word> words) {
        this.context = context;
        this.wordList = words;
    }

    @NonNull
    @Override
    public YourWordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        YourWordItemBinding binding = YourWordItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new YourWordViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull YourWordViewHolder holder, int position) {
        Word word = wordList.get(position);
        holder.binding.tvContent.setText(word.getContent());
        SpannableString spannableString = new SpannableString(Html.fromHtml(word.getDefinition()));
        holder.binding.tvDescription.setText(spannableString);
        int point = new Random().nextInt(10);
        holder.binding.tvPoint.setText("" + point);
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    class YourWordViewHolder extends RecyclerView.ViewHolder {
        YourWordItemBinding binding;
        public YourWordViewHolder(YourWordItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
