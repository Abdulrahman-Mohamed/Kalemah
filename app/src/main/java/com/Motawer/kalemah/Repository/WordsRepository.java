package com.Motawer.kalemah.Repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.Motawer.kalemah.RoomDataBase.Word;
import com.Motawer.kalemah.RoomDataBase.WordsDao;
import com.Motawer.kalemah.RoomDataBase.WordsDataBase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class WordsRepository {
    private final WordsDao wordsDao;
    private final LiveData<List<Word>> allWords;
    private final MutableLiveData<List<Word>> fireWords;
    private LiveData<List<Word>> fireLevelsWords;
    private final LiveData<List<Word>> allUserWords;
    private final LiveData<List<Word>> AWords;
    private final LiveData<List<Word>> BWords;
    private final LiveData<List<Word>> CWords;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String Uid = firebaseAuth.getCurrentUser().getUid();
    int size;

    public WordsRepository(Application application) {
        WordsDataBase wordsDataBase = WordsDataBase.getInstance(application);
        wordsDao = wordsDataBase.wordsDao();
        allWords = wordsDao.getAllWords();
        allUserWords = wordsDao.getAllUserWords();
        fireWords = new MutableLiveData<>();
        FireBase();

        AWords = wordsDao.getWordslevel1();
        BWords = wordsDao.getWordslevel2();
        CWords = wordsDao.getWordslevel3();


    }

    private void FireBase() {
        myRef.child(Uid).child("UserWords").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Word> list = new ArrayList<>();

                if (dataSnapshot.exists())
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        list.add(snapshot.getValue(Word.class));
                    }
                fireWords.setValue(list);
                getFireWords();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void insert(Word word) {
        new InsertAsync(wordsDao).execute(word);
        //   Log.d(TAG,Uid);
        myRef.child(Uid).child("UserWords").child(word.getWord()).setValue(word);

    }

    public void insert2(Word word) {
        new InsertAsync(wordsDao).execute(word);
        //   Log.d(TAG,Uid);
        //  myRef.child(Uid).child("UserWords").child(word.getWord()).setValue(word);

    }


    public void update(Word word) {
        new updateAsync(wordsDao).execute(word);
        myRef.child(Uid).child("UserWords").child(word.getWord()).setValue(word);
    }

    public void delete(Word word) {
        new deleteAsync(wordsDao).execute(word);
        myRef.child(Uid).child("UserWords").child(word.getWord()).removeValue();
    }

    // use it to return firebase words;
    public void getFireData() {
//       final ArrayList<Word> FireList;
//       FireList=new ArrayList<>();
        myRef.child(Uid).child("UserWords").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Word> fireword = new ArrayList<>();
                //       allWords = wordsDao.getAllWords();
                //if (allWords.getValue() != null)
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Word word = dataSnapshot1.getValue(Word.class);
                        fireword.add(word);

                        // FireList.add(word);

                    }
                    Set<Word> set = new LinkedHashSet<>();
                    set.addAll(fireword);
                    fireword.clear();
                    fireword.addAll(set);
                    for (int i = 0; i < fireword.size(); i++) {
                        insert2(fireword.get(i));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void deleteAll() {
        new deleteAllAsync(wordsDao).execute();
    }

    /*
      todo
      this fun is to delete all user words form firebase and roomdatabase
       */
    public void deleteAllLocalGlobal() {
        new deleteAllAsync(wordsDao).execute();
        myRef.child(Uid).child("UserWords").removeValue();

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

    public LiveData<List<Word>> getAWords() {
        return AWords;
    }

    public LiveData<List<Word>> getBWords() {
        return BWords;
    }

    public LiveData<List<Word>> getCWords() {
        return CWords;
    }


    public static class InsertAsync extends AsyncTask<Word, Void, Void> {
        private final WordsDao wordsDao;

        public InsertAsync(WordsDao wordsDao) {
            this.wordsDao = wordsDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordsDao.insert(words[0]);
            Log.e("TAG", words[0].getWord());

            return null;
        }
    }

//    public static class InsertlistAsync extends AsyncTask<Word, Void, Void> {
//        private WordsDao wordsDao;
//
//        public InsertlistAsync(WordsDao wordsDao) {
//            this.wordsDao = wordsDao;
//        }

//        @Override
//        protected Void doInBackground(Word... words) {
//            for (int i = 0; i < words.length; i++) {
//                wordsDao.insert(words[i]);
//                Log.e("TAG", words[i].getWord());
//            }
//
//            return null;
//        }
//    }


    public static class updateAsync extends AsyncTask<Word, Void, Void> {
        private final WordsDao wordsDao;

        public updateAsync(WordsDao wordsDao) {
            this.wordsDao = wordsDao;


        }

        @Override
        protected Void doInBackground(Word... words) {
            wordsDao.update(words[0]);
            return null;
        }
    }

//    public static class updateRateAsync extends AsyncTask<Integer, String, Void> {
//        private WordsDao wordsDao;
//
//
//        public updateRateAsync(WordsDao wordsDao) {
//            this.wordsDao = wordsDao;
//        }


//        @Override
//        protected Void doInBackground(Integer... integers) {
//            return null;
//        }
//    }

    public static class deleteAsync extends AsyncTask<Word, Void, Void> {
        private final WordsDao wordsDao;

        public deleteAsync(WordsDao wordsDao) {
            this.wordsDao = wordsDao;


        }

        @Override
        protected Void doInBackground(Word... words) {
            wordsDao.delete(words[0]);
            return null;
        }
    }

    public static class deleteAllAsync extends AsyncTask<Void, Void, Void> {
        private final WordsDao wordsDao;

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
