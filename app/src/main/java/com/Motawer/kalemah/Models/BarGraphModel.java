package com.Motawer.kalemah.Models;

import java.io.Serializable;

public class BarGraphModel implements Serializable
{
    private int month;

    public BarGraphModel(int month) {
        this.month = month;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
