package com.example.dictionary.view.adapter;

import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary.data.EngDatabaseAccess;
import com.example.dictionary.data.viet_anh.VietDatabaseAccess;
import com.example.dictionary.model.Word;
import com.example.dictionary.databinding.ItemBoardBinding;
import com.example.dictionary.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> implements Filterable {
    public final String TAG = WordAdapter.class.getSimpleName();
    Context context;
    List<Word> words;
    List<Word> wordFiltered;
    public boolean isSearching;
    OnClickListener listener;
    private int mType;

    public WordAdapter(Context context, List<Word> words, int type) {
        this.context = context;
        this.wordFiltered = words;
        this.words = new ArrayList<>(wordFiltered);
        mType = type;
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
        holder.binding.tvWord.setText(wordFiltered.get(position).getContent());
        if (listener != null) {
            holder.binding.containerWord.setOnClickListener(v -> {
                listener.onClick(wordFiltered.get(position), position);
            });
        }
        String definitionString = wordFiltered.get(position).getDefinition();
        int start = definitionString.indexOf("<li>");
        int end = definitionString.indexOf("</li>");
        if (start != -1 || end != -1) {
            String subString = definitionString.substring(start + 4, end);
            SpannableString spannableString = new SpannableString(Html.fromHtml(subString));
            holder.binding.tvDefinition.setText(spannableString);

        }
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return wordFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filter = constraint.toString();
            List<Word> filteredList = new ArrayList<>();
            Log.i(TAG, "performFiltering: " + filter);
            if (filter.isEmpty() || filter == null) {
                isSearching = false;
                filteredList.addAll(words);
            } else {
                Log.i(TAG, "performFiltering: " + mType);
                isSearching = true;
                if (mType == Constants.WORD.ENG_TYPE) {
                    EngDatabaseAccess access = EngDatabaseAccess.getInstance(context);
                    access.open();
                    filteredList.addAll(access.getWords(filter));
                }
                else {
                    VietDatabaseAccess access = VietDatabaseAccess.getInstance(context);
                    access.open();
                    filteredList.addAll(access.getWords(filter));
                }

            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            wordFiltered.clear();
            wordFiltered.addAll((List<Word>) results.values);
            notifyDataSetChanged();
            Log.i(TAG, "publishResults: ");
        }
    };

    public void setWords(List<Word> words) {
        this.words.clear();
        this.words.addAll(words);
    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        ItemBoardBinding binding;
        public WordViewHolder(ItemBoardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    public interface OnClickListener {
        public void onClick(Word word, int position);
    }
}
