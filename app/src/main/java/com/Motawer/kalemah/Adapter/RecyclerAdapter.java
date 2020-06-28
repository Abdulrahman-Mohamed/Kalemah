package com.Motawer.kalemah.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Motawer.kalemah.R;
import com.Motawer.kalemah.RoomDataBase.Word;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.WordViewHolder> implements Filterable {
    private List<Word> wordList = new ArrayList<Word>();
    private List<Word> wordLisfull;



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

        if (currentword.getLevel() == -1) {
            holder.level.setText("A");
            holder.level.setBackgroundResource(R.drawable.level_round_a);
        } else if (currentword.getLevel() == -2) {
            holder.level.setText("B");
            holder.level.setBackgroundResource(R.drawable.level_round_b);

        } else if (currentword.getLevel() == -3){
            holder.level.setText("C");
            holder.level.setBackgroundResource(R.drawable.level_round_c);
        }else if (currentword.getLevel()>0)
        {
            holder.level.setText(String.valueOf(currentword.getLevel()));
            holder.level.setBackgroundResource(R.drawable.level_round_c);
        }
    }

    public void setWordList(List<Word> wordList) {
        this.wordList = wordList;
        wordLisfull = new ArrayList<>(wordList);
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

    @Override
    public Filter getFilter() {
        return wordFilter;
    }

    private Filter wordFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Word> filteredList = new ArrayList<>();
//            for(int i=0;i<=wordList.size();i++)
//                wordLisfull.add(wordList.get(i));

           if (constraint == null || constraint.length() == 0) {
              filteredList.addAll(wordLisfull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Word word : wordLisfull)
                {
                    if (word.getWord().toLowerCase().trim().contains(filterPattern))
                    {
                        filteredList.add(word);
                    }
                        if (word.getMeaning().toLowerCase().contains(filterPattern))
                        {filteredList.add(word);
                        }

                }
           }
            FilterResults results = new FilterResults();
          // if (!filteredList.isEmpty()){
            results.values = filteredList;
        //}
            //else if (filteredList.isEmpty())
       //    {results.values=wordList;}
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (!constraint.equals("")){
            wordList.clear();
            wordList.addAll((List) results.values);
            notifyDataSetChanged();}else
                {
                    wordList.clear();
                    wordList.addAll(wordLisfull);
                    notifyDataSetChanged();
                }
        }
    };

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
    public void clear() {
        int size = wordList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                wordList.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }
}
