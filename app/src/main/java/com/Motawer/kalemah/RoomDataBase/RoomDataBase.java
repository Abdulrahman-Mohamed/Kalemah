package com.Motawer.kalemah.RoomDataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
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
                    .build();
        }
        return instanceRoomDatabase;
    }
}
