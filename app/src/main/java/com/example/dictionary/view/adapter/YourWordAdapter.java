package com.example.dictionary.view.adapter;

import android.content.Context;
import android.graphics.Point;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary.databinding.YourWordItemBinding;
import com.example.dictionary.model.Word;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class YourWordAdapter extends RecyclerView.Adapter<YourWordAdapter.YourWordViewHolder> {

    private final TextToSpeech t1;
    Context context;
    List<Word> wordList;

    public YourWordAdapter(Context context, List<Word> words) {
        this.context = context;
        this.wordList = words;
        t1 = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        t1.setSpeechRate(1.0f);
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
        holder.binding.btnAudio.setOnClickListener(v -> {
            t1.speak(word.getContent(), TextToSpeech.QUEUE_FLUSH, null);
        });
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        t1.shutdown();
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    class YourWordViewHolder extends RecyclerView.ViewHolder {
        YourWordItemBinding binding;
        Point point;
        public YourWordViewHolder(YourWordItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            int point = new Random().nextInt(10);
            binding.tvPoint.setText("" + point);
        }
    }
}
