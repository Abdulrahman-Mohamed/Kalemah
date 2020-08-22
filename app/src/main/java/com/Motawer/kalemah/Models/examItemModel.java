package com.Motawer.kalemah.Models;

import java.util.ArrayList;

public class examItemModel {
    String title;
    ArrayList<Integer> index;

    public examItemModel(String title, ArrayList<Integer> index) {
        this.title = title;
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Integer> getIndex() {
        return index;
    }

    public void setIndex(ArrayList<Integer> index) {
        this.index = index;
    }
}
