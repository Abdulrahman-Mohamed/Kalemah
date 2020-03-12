package com.Motawer.kalemah.RoomDataBase;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Word.class,version = 1)
abstract class WordsDataBase extends RoomDatabase
{
    private static WordsDataBase instanceRoomDatabase;
    public abstract WordsDao wordsDao();
    public static synchronized WordsDataBase getInstance(Context context)
    {
        if (instanceRoomDatabase==null)
        {
            instanceRoomDatabase= Room.databaseBuilder(context.getApplicationContext(),
                    WordsDataBase.class,"Words_Database").fallbackToDestructiveMigration()
                    .addCallback(callback).build();
        }
        return instanceRoomDatabase;
    }
    private static RoomDatabase.Callback callback=new RoomDatabase.Callback()
    {


        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new populateAsync(instanceRoomDatabase).execute();
        }
    };
   static class populateAsync extends AsyncTask<Void,Void,Void> {
        private WordsDao wordsDao;
        public populateAsync(WordsDataBase db) {
            wordsDao = db.wordsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            wordsDao.insert(new Word("Car","سياره",1));
            wordsDao.insert(new Word("person","شخص",2));
            wordsDao.insert(new Word("entertainment","تسليه",3));

            return null;
        }
    }
}
