package com.Motawer.kalemah.RoomDataBase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Words_Table")
public class Word {
@PrimaryKey(autoGenerate = true)
    private int ID;
    private String word;
    private String meaning;
    private int level;

    public Word(String word, String meaning, int level) {
        this.word = word;
        this.meaning = meaning;
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
