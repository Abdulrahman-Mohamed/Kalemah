package com.Motawer.kalemah.RoomDataBase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Words_Table" )
public class Word implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int ID;
    private String word;
    private String meaning;
    private int level;
    int Rate;

    public Word()
    {

    }

    public Word(String word, String meaning, int level, int rate) {
        this.word = word;
        this.meaning = meaning;
        this.level = level;
        Rate = rate;
    }

    public Word(String word, String meaning, int level)
    {
        this.word = word;
        this.meaning = meaning;
        this.level = level;

    }

    public int getRate() {
        return Rate;
    }

    public void setRate(int rate) {
        Rate = rate;
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
