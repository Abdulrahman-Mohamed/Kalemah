package com.Motawer.kalemah.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.Motawer.kalemah.RoomDataBase.Word;
import com.Motawer.kalemah.RoomDataBase.WordsDao;
import com.Motawer.kalemah.RoomDataBase.WordsDataBase_Impl;

import java.util.List;

public class WordsRepository {
    private WordsDao wordsDao;
    private LiveData<List<Word>> allWords;
    private LiveData<List<Word>> AWords;
    private LiveData<List<Word>> BWords;
    private LiveData<List<Word>> CWords;

    public WordsRepository(Application application) {
        WordsDataBase_Impl wordsDataBase = (WordsDataBase_Impl) WordsDataBase_Impl.getInstance(application);
        wordsDao = wordsDataBase.wordsDao();
        allWords = wordsDao.getAllWords();
        AWords=wordsDao.getWordslevel1();
        BWords=wordsDao.getWordslevel2();
        CWords=wordsDao.getWordslevel3();

    }

    public void insert(Word word)
    {
        new InsertAsync(wordsDao).execute(word);

    }
    public void update(Word word)
    {
new updateAsync(wordsDao).execute(word);
    }
    public void delete(Word word)
    {
new deleteAsync(wordsDao).execute(word);
    }
    public void deleteAll()
    {
        new deleteAllAsync(wordsDao).execute();
    }
    public  LiveData<List<Word>> getAllWords()
    {
        return allWords;
    }
    public  LiveData<List<Word>> getAWords()
    {
        return AWords;
    }public  LiveData<List<Word>> getBWords()
    {
        return BWords;
    }public  LiveData<List<Word>> getCWords()
    {
        return CWords;
    }

   public static class InsertAsync extends AsyncTask<Word,Void,Void>
   {
       private WordsDao wordsDao;
       public InsertAsync(WordsDao wordsDao) {
           this.wordsDao = wordsDao;


       }
       @Override
       protected Void doInBackground(Word... words) {
           wordsDao.insert(words[0]);
           return null;
       }
   }
    public static class updateAsync extends AsyncTask<Word,Void,Void>
    {
        private WordsDao wordsDao;
        public updateAsync(WordsDao wordsDao) {
            this.wordsDao = wordsDao;


        }
        @Override
        protected Void doInBackground(Word... words) {
            wordsDao.update(words[0]);
            return null;
        }
    }
    public static class deleteAsync extends AsyncTask<Word,Void,Void>
    {
        private WordsDao wordsDao;
        public deleteAsync(WordsDao wordsDao) {
            this.wordsDao = wordsDao;


        }
        @Override
        protected Void doInBackground(Word... words) {
            wordsDao.delete(words[0]);
            return null;
        }
    }
    public static class deleteAllAsync extends AsyncTask<Void,Void,Void>
    {
        private WordsDao wordsDao;
        public deleteAllAsync(WordsDao wordsDao) {
            this.wordsDao = wordsDao;


        }
        @Override
        protected Void doInBackground(Void... voids) {
            wordsDao.deleteAll();
            return null;
        }
    }
}
