package com.Motawer.kalemah.RoomDataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WordsDao  {

    @Insert
    void insert(Word word);

    @Update
    void update(Word word);

    @Delete
    void delete(Word word);

    @Query("DELETE FROM Words_Table")
    void deleteAll();

    @Query("SELECT * FROM Words_Table WHERE level>=1 Order By level Desc")
    LiveData<List<Word>> getAllWords();

    @Query("SELECT * FROM Words_Table Where 0>level Order BY level ASC")
    LiveData<List<Word>> getAllUserWords();

    @Query("SELECT * FROM Words_Table WHERE level=-3")
    LiveData<List<Word>> getWordslevel3();

    @Query("SELECT * FROM Words_Table WHERE level=-2")
    LiveData<List<Word>> getWordslevel2();
    
    @Query("SELECT * FROM Words_Table WHERE level=-1")
    LiveData<List<Word>> getWordslevel1();
}
