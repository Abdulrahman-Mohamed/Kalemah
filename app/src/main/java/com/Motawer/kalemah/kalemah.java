package com.Motawer.kalemah;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class kalemah extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

}
