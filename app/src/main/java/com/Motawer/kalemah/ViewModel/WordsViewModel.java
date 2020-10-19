package com.Motawer.kalemah.ViewModel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.Motawer.kalemah.Models.BarGraphModel;
import com.Motawer.kalemah.Repository.WordsRepository;
import com.Motawer.kalemah.RoomDataBase.Word;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class WordsViewModel extends AndroidViewModel {
    WordsRepository wordsRepository;
    LiveData<List<Word>> allWords;
    LiveData<List<Word>> fireWords;
    LiveData<List<Word>> allUserWords;
    LiveData<List<Word>> aWords;
    LiveData<List<Word>> bWords;
    LiveData<List<Word>> cWords;
    ArrayList<BarGraphModel> barGraphModels;
    FirebaseAuth firebaseAuth;


    public WordsViewModel(@NonNull Application application) {
        super(application);
        //Log.e("thread", String.valueOf(Thread.currentThread()));
        wordsRepository = new WordsRepository(application);
        allWords = wordsRepository.getAllWords();
        fireWords = wordsRepository.getFireWords();
        allUserWords = wordsRepository.getAllUserWords();
        aWords = wordsRepository.getAWords();
        bWords = wordsRepository.getBWords();
        cWords = wordsRepository.getCWords();
        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void insetr(Word word) {
        loadData();
        saveData();
        wordsRepository.insert(word);
    }

    private void saveData() {
        String uid = firebaseAuth.getUid();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String date1 = dateFormat.format(date);
        String[] words = date1.split("/");//splits the string based on whitespace
        int year = Integer.parseInt(words[0]);
        int month = Integer.parseInt(words[1]);
        BarGraphModel graphModel = new BarGraphModel(month);
        barGraphModels.add(graphModel);
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(uid, getApplication().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(barGraphModels);
        editor.putString(String.valueOf(year), json);
        editor.apply();
    }

    private void loadData() {
        String uid = firebaseAuth.getUid();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String date1 = dateFormat.format(date);
        String[] words = date1.split("/");//splits the string based on whitespace
        int year = Integer.parseInt(words[0]);
        int month = Integer.parseInt(words[1]);
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(uid, getApplication().MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(String.valueOf(year), null);
        Type type = new TypeToken<ArrayList<BarGraphModel>>() {
        }.getType();
        barGraphModels = gson.fromJson(json, type);
        if (barGraphModels == null) {
            barGraphModels = new ArrayList<>();
        }
    }

    public void setFireBaseWords() {
        wordsRepository.getFireData();
    }

    public void update(Word word) {
        wordsRepository.update(word);
    }

    public void delete(Word word) {
        wordsRepository.delete(word);
    }


    public void deleteAllWords() {
        wordsRepository.deleteAll();
    }

    public void deleteAllWordsLocalGlobal() {
        wordsRepository.deleteAllLocalGlobal();
    }

    public LiveData<List<Word>> getAllWords() {
        return allWords;
    }

    public LiveData<List<Word>> getFireWords() {
        return fireWords;
    }

    public LiveData<List<Word>> getAllUserWords() {
        return allUserWords;
    }

    public LiveData<List<Word>> getlevelWords(int level) {
        if (level == -1) {
            return aWords;
        } else if (level == -2) {
            return bWords;
        } else if (level == -3) {
            return cWords;
        }else
        return null;
    }
}
