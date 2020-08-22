package com.Motawer.kalemah.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Motawer.kalemah.R;
import com.Motawer.kalemah.RoomDataBase.Word;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.WordViewHolder> implements Filterable {
    private List<Word> wordList = new ArrayList<Word>();
    private List<Word> wordLisfull;
    final String KEY = "FAVORIT_WORDS";
    final String WORD_FAVORIT = "MY_FAV_WORDS";
    ArrayList<Word> wordArrayList = new ArrayList<>();
    View view;
    TextToSpeech textToSpeech ;







    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.words_item, parent, false);





        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WordViewHolder holder, int position) {
        //viewModel = new ViewModelProvider( this).get(WordsViewModel.class);
        final Word currentword = wordList.get(position);
        loadData(view, holder, currentword);



        holder.words.setText(currentword.getWord());
        holder.meaning.setText(currentword.getMeaning());
        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i;
                final String KEY = "FAVORIT_WORDS";
                final String WORD_FAVORIT = "MY_FAV_WORDS";
                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences(KEY, Context.MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPreferences.getString(WORD_FAVORIT, null);
                Type type = new TypeToken<ArrayList<Word>>() {
                }.getType();
                wordArrayList = gson.fromJson(json, type);

                if (wordArrayList == null || wordArrayList.size() == 0) {
                    wordArrayList = new ArrayList<>();
                    SaveFavorit(v, currentword);
                    holder.favorite.setImageResource(R.drawable.favorite_word_red);
                } else {
                    for (i = 0; i < wordArrayList.size(); i++) {
                        if (wordArrayList.get(i).getWord().equals(currentword.getWord())) {
                            doubleRemove(currentword, view.getContext(), wordArrayList);
                            holder.favorite.setImageResource(R.drawable.favorite_word);
                            return;
                        }
                    }
                    // if (!wordArrayList.get(i).getWord().equals(currentword.getWord())){
                    SaveFavorit(v, currentword);
                    holder.favorite.setImageResource(R.drawable.favorite_word_red);
                }
                //   }

            }
        });

        textToSpeech =new TextToSpeech(view.getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status)
            {
                if (status ==TextToSpeech.SUCCESS)
                {
                    int lang = textToSpeech.setLanguage(Locale.GERMANY);
                }

            }
        });
        holder.speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s= holder.words.getText().toString();
                int speech = textToSpeech.speak(s,TextToSpeech.QUEUE_FLUSH,null);
            }
        });






        if (currentword.getLevel() == -1) {
            holder.level.setText("A");
            holder.level.setBackgroundResource(R.drawable.level_round_a);
        } else if (currentword.getLevel() == -2) {
            holder.level.setText("B");
            holder.level.setBackgroundResource(R.drawable.level_round_b);

        } else if (currentword.getLevel() == -3) {
            holder.level.setText("C");
            holder.level.setBackgroundResource(R.drawable.level_round_c);
        } else if (currentword.getLevel() > 0) {
            holder.level.setText(String.valueOf(currentword.getLevel()));
            holder.level.setBackgroundResource(R.drawable.level_round_c);
        }
    }

    private void SaveFavorit(View view, Word currentword) {
        SharedPreferences sharedPref = view.getContext().getSharedPreferences(KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
       // wordArrayList = new ArrayList<>();
        wordArrayList.add(currentword);
        Gson gson = new Gson();
        String json = gson.toJson(wordArrayList);
        editor.putString(WORD_FAVORIT, json);
        editor.commit();
    }

    private void loadData(View view, WordViewHolder holder, Word currentword) {
        ArrayList<Word> checkList;
        final String KEY = "FAVORIT_WORDS";
        final String WORD_FAVORIT = "MY_FAV_WORDS";
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(KEY, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(WORD_FAVORIT, null);
        Type type = new TypeToken<ArrayList<Word>>() {
        }.getType();
        checkList = gson.fromJson(json, type);

        if (checkList == null) {
            checkList = new ArrayList<>();
        } else {
            for (int i = 0; i < checkList.size(); i++)
                if (checkList.get(i).getWord().equals(currentword.getWord())) {
                    holder.favorite.setImageResource(R.drawable.favorite_word_red);
                }
        }
    }

    public void setWordList(List<Word> wordList) {
        this.wordList = wordList;
        if (wordList != null)
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

    public void doubleRemove(Word current, Context context, ArrayList<Word> wordList) {
        for (int i = 0; i < wordList.size(); i++)
            if (wordList.get(i).getWord().equals(current.getWord())) {
                wordList.remove(i);

                saveShared(wordList, context);
            }
    }

    public void removeAt(int position, Context context) {
        wordList.remove(position);
        // notifyItemChanged(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, wordList.size());
        saveShared(wordList, context);

    }

    public void removeshared(Word position, Context context, ArrayList<Word> wordList) {
        for (int i = 0; i < wordList.size(); i++)
            if (wordList.get(i).getWord().equals(position.getWord())) {
                wordList.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, wordList.size());
                saveShared(wordList, context);
            }

    }

    private void saveShared(List<Word> wordList, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(wordList);
        editor.putString(WORD_FAVORIT, json);
        editor.commit();
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
                for (Word word : wordLisfull) {
                    if (word.getWord().toLowerCase().trim().contains(filterPattern)) {
                        filteredList.add(word);
                    }
                    if (word.getMeaning().toLowerCase().contains(filterPattern)) {
                        filteredList.add(word);
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
            if (!constraint.equals("")) {
                wordList.clear();
                wordList.addAll((List) results.values);
                notifyDataSetChanged();
            } else {
                wordList.clear();
                wordList.addAll(wordLisfull);
                notifyDataSetChanged();
            }
        }
    };

    public void restoreItem(Word item, int position) {
        wordList.add(position, item);
        //viewModel.insetr(item);

        notifyItemInserted(position);
    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        private TextView words;
        private TextView level;
        private TextView meaning;
        private ImageButton favorite;
        //
        private  ImageButton speaker;


        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            words = itemView.findViewById(R.id.word_text);
            level = itemView.findViewById(R.id.word_level);
            meaning = itemView.findViewById(R.id.meaning_text);
            favorite = itemView.findViewById(R.id.favorite_Button);
            speaker= itemView.findViewById(R.id.speaker);

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
