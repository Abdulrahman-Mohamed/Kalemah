package com.Motawer.kalemah.Models;

public class ExamsItems
{
    int imageView,imageUpon;
    String textView;

    public ExamsItems(int imageView, int imageUpon, String textView) {
        this.imageView = imageView;
        this.imageUpon = imageUpon;
        this.textView = textView;
    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    public int getImageUpon() {
        return imageUpon;
    }

    public void setImageUpon(int imageUpon) {
        this.imageUpon = imageUpon;
    }

    public String getTextView() {
        return textView;
    }

    public void setTextView(String textView) {
        this.textView = textView;
    }
}
