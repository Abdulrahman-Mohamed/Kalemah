package com.Motawer.kalemah.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.Motawer.kalemah.Repository.WordsRepository;
import com.Motawer.kalemah.RoomDataBase.Word;

import java.util.List;


public class WordsViewModel extends AndroidViewModel {
    WordsRepository wordsRepository;
    LiveData<List<Word>> allWords;
    public WordsViewModel(@NonNull Application application) {
        super(application);
        wordsRepository=new WordsRepository(application);
        allWords=wordsRepository.getAllWords();
    }
    public void insetr(Word word)
    {
        wordsRepository.insert(word);
    }
    public void update(Word word)
    {
        wordsRepository.update(word);
    }
    public void delete(Word word)
    {
        wordsRepository.delete(word);
    }
    public void deleteAllWords()
    {
        wordsRepository.deleteAll();
    }
    public LiveData<List<Word>> getAllWords()
    {
    return allWords;
    }
}
