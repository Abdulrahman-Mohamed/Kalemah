package com.Motawer.kalemah.Models;

public class CategoriesItems
{
    String title,numberOfLevels,MyCurrentLevel;

    public CategoriesItems(String title, String numberOfLevels, String myCurrentLevel) {
        this.title = title;
        this.numberOfLevels = numberOfLevels;
        MyCurrentLevel = myCurrentLevel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNumberOfLevels() {
        return numberOfLevels;
    }

    public void setNumberOfLevels(String numberOfLevels) {
        this.numberOfLevels = numberOfLevels;
    }

    public String getMyCurrentLevel() {
        return MyCurrentLevel;
    }

    public void setMyCurrentLevel(String myCurrentLevel) {
        MyCurrentLevel = myCurrentLevel;
    }
}
