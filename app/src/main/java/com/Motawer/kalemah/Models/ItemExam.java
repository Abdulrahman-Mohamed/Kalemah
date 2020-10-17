package com.Motawer.kalemah.Models;

import com.Motawer.kalemah.RoomDataBase.Word;

import java.util.ArrayList;
import java.util.List;

public class ItemExam {
    ArrayList<Word> wordArrayList=new ArrayList<>();
    List<Boolean> levelList = new ArrayList<>();
    int currentCat;
    int finalPoints;
    ArrayList<Integer> integers = new ArrayList<>();
    ArrayList<Integer> size=new ArrayList<>();
    List<Integer> points = new ArrayList<>();
    String title;

    public ItemExam(ArrayList<Word> wordArrayList, List<Boolean> levelList, int currentCat, int finalPoints, ArrayList<Integer> integers, ArrayList<Integer> size, List<Integer> points, String title) {
        this.wordArrayList = wordArrayList;
        this.levelList = levelList;
        this.currentCat = currentCat;
        this.finalPoints = finalPoints;
        this.integers = integers;
        this.size = size;
        this.points = points;
        this.title = title;
    }

    public ArrayList<Word> getWordArrayList() {
        return wordArrayList;
    }

    public void setWordArrayList(ArrayList<Word> wordArrayList) {
        this.wordArrayList = wordArrayList;
    }

    public List<Boolean> getLevelList() {
        return levelList;
    }

    public void setLevelList(List<Boolean> levelList) {
        this.levelList = levelList;
    }

    public int getCurrentCat() {
        return currentCat;
    }

    public void setCurrentCat(int currentCat) {
        this.currentCat = currentCat;
    }

    public int getFinalPoints() {
        return finalPoints;
    }

    public void setFinalPoints(int finalPoints) {
        this.finalPoints = finalPoints;
    }

    public ArrayList<Integer> getIntegers() {
        return integers;
    }

    public void setIntegers(ArrayList<Integer> integers) {
        this.integers = integers;
    }

    public ArrayList<Integer> getSize() {
        return size;
    }

    public void setSize(ArrayList<Integer> size) {
        this.size = size;
    }

    public List<Integer> getPoints() {
        return points;
    }

    public void setPoints(List<Integer> points) {
        this.points = points;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}