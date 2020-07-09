package com.Motawer.kalemah.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.Motawer.kalemah.Repository.WordsRepository;
import com.Motawer.kalemah.RoomDataBase.Word;

import java.util.List;


public class WordsViewModel extends AndroidViewModel {
    WordsRepository wordsRepository;
    LiveData<List<Word>> allWords;
    LiveData<List<Word>> fireWords;
    LiveData<List<Word>> allUserWords;
    LiveData<List<Word>> aWords;
    LiveData<List<Word>> bWords;
    LiveData<List<Word>> cWords;

    public WordsViewModel(@NonNull Application application) {
        super(application);
        Log.e("thread", String.valueOf(Thread.currentThread()));
        wordsRepository=new WordsRepository(application);
        allWords=wordsRepository.getAllWords();
        fireWords=wordsRepository.getFireWords();
        allUserWords=wordsRepository.getAllUserWords();
        aWords=wordsRepository.getAWords();
        bWords=wordsRepository.getBWords();
        cWords=wordsRepository.getCWords();

    }
    public void insetr(Word word)
    {


        wordsRepository.insert(word);
    }
    public void setFireBaseWords()
    {
        wordsRepository.getFireData();
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
    public LiveData<List<Word>> getFireWords()
    {
        return fireWords;
    }
    public LiveData<List<Word>> getAllUserWords()
    {
        return allUserWords;
    }
    public LiveData<List<Word>> getlevelWords(int level)
    {
        if (level==-1)
        { return aWords;}
        else if (level==-2)
        { return bWords;}
        else if (level==-3)
        { return cWords;}
        return null;
    }
}
