package com.Motawer.kalemah.RoomDataBase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Words_Table" )
public class Word implements Serializable {
@PrimaryKey(autoGenerate = true )
    private int ID;//here

    private String word;
    private String meaning;
    private int level;

    public Word()
    {
    }

    public Word(String word, String meaning, int level)
    {
        this.word = word;
        this.meaning = meaning;
        this.level = level;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public String getWord() {
        return word;
    }

    public String getMeaning() {
        return meaning;
    }

    public int getLevel() {
        return level;
    }
}
