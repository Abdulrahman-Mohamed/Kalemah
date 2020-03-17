package com.Motawer.kalemah.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.Motawer.kalemah.Fragments.words_frag;
import com.Motawer.kalemah.R;
import com.Motawer.kalemah.RoomDataBase.Word;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.WordViewHolder> {
    private List<Word> wordList = new ArrayList<>();

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.words_item, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        Word currentword = wordList.get(position);
        holder.words.setText(currentword.getWord());
        holder.meaning.setText(currentword.getMeaning());
        if (currentword.getLevel() == 1) {
            holder.level.setText("A");
            holder.level.setBackgroundResource(R.drawable.level_round_a);
        } else if (currentword.getLevel() == 2) {
            holder.level.setText("B");
            holder.level.setBackgroundResource(R.drawable.level_round_b);

        } else {
            holder.level.setText("C");
            holder.level.setBackgroundResource(R.drawable.level_round_c);
        }
    }

    public void setWordList(List<Word> wordList) {
        this.wordList = wordList;
        notifyDataSetChanged();
    }

    public Word getWordAt(int position) {
        return wordList.get(position);
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    public void removeAt(int position) {
        wordList.remove(position);
       // notifyItemChanged(position);
        notifyItemRemoved(position);
          notifyItemRangeChanged(position, wordList.size());

    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        private TextView words;
        private TextView level;
        private TextView meaning;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            words = itemView.findViewById(R.id.word_text);
            level = itemView.findViewById(R.id.word_level);
            meaning = itemView.findViewById(R.id.meaning_text);

        }
    }
}
