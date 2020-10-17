package com.Motawer.kalemah;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Motawer.kalemah.Adapter.exerciseAdapter;
import com.Motawer.kalemah.Models.ItemExam;
import com.Motawer.kalemah.RoomDataBase.Word;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Excercise_Levels extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = firebaseDatabase.getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    List<Boolean> levelList = new ArrayList<>();
    ArrayList<String> titleList = new ArrayList<>();
    ArrayList<Integer> integers = new ArrayList<>();
    ArrayList<Word> wordArrayList = new ArrayList<>();
    ArrayList<Word> wordsList = new ArrayList<>();

    List<Integer> points = new ArrayList<>();
    ArrayList<Integer> size=new ArrayList<>();
    int categories_level;
    int finalPoints;
    ArrayList<ItemExam> itemExams=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excercise__quizz);
        categories_level = getIntent().getIntExtra("level", 0);
        BackThread backThread=new BackThread();
        backThread.setPriority(Thread.MAX_PRIORITY);
        backThread.start();
        // Toast.makeText(this,String.valueOf(levels),Toast.LENGTH_LONG).show();
        //InitDatabase();
        //  went to the categories activity
        //setPoints();
    }

    private void InitializeRecycler() {
        recyclerView = findViewById(R.id.recycler_exam_items);
        linearLayoutManager = new LinearLayoutManager(Excercise_Levels.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        // exerciseAdapter examItemsAdapter = new exerciseAdapter(modelList, categories_level);
        Context context=Excercise_Levels.this;
        exerciseAdapter exerciseAdapter=new exerciseAdapter(context,itemExams);
        recyclerView.setAdapter(exerciseAdapter);
    }



    private void LevelsHandeler() {

        myRef.child("Words_Eng_Title")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                titleList.add(dataSnapshot1.getValue(String.class));
                            }
                            String previous = "";
                            int pointer = 0;
                            String currNumber;
                            while (pointer < titleList.size()) {
                                currNumber = titleList.get(pointer);
                                for (int i = 0; i < titleList.size(); i++) {
                                    if (currNumber.equals(titleList.get(i)) && i > pointer) {
                                        Log.e("duplicate", currNumber + "in" + i);
                                        integers.add(pointer);
                                        integers.add(i);
                                        System.out.println("Duplicate for " + integers.get(0) + " in " + integers.get(1));
                                        System.out.println("title " + titleList.get(integers.get(0)));

                                        previous = titleList.get(i);

                                        break;
                                    }
                                }
                                if (size.size()!=0)
                                    if (integers.size() > 1) {
                                        int p=0;
                                        for (int j=0;j<integers.size();j++)
                                            while (p<size.get(integers.get(j))) {
                                                wordsList.add(wordArrayList.get(p));
                                                p+=1;
                                            }System.out.println(wordsList.size());
                                        ItemExam itemExam=new ItemExam(wordsList,levelList,categories_level,finalPoints,integers,size,points,titleList.get(integers.get(0)));
                                        setList(itemExam);


                                    } else if (integers.size() == 0 && !titleList.get(pointer).equals(previous)) {
                                        integers.add(pointer);
                                        wordsList=new ArrayList<>();
                                        for (int i=(pointer*50);i<size.get(pointer);i++)
                                            wordsList.add(wordArrayList.get(i));
                                        ItemExam itemExam=new ItemExam(wordsList,levelList,categories_level,finalPoints,integers,size,points,titleList.get(integers.get(0)));
                                        setList(itemExam);



                                    }
                                integers = new ArrayList<>();
                                pointer++;
                            }

                            InitializeRecycler();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        final List<Integer> points = new ArrayList<>();
        // to adapter
        myRef.child("UserPoints")
                .child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        points.add(dataSnapshot1.getValue(Integer.class));
                    }
                    for (int levels = 0; levels < points.size(); levels++) {
                        if (points.get(levels) >= 30)
                            myRef.child("UserLevels").child(uid)
                                    .child(String.valueOf(levels + 1))
                                    .setValue(true);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void setList(ItemExam itemExam)
    {

        itemExams.add(itemExam);
    }



    class BackThread extends Thread
    {

        @Override
        public void run() {

            int limit = 0;
            if (categories_level == 1)
                limit = 5;
            if (categories_level == 2)
                limit = 10;
            if (categories_level == 3)
                limit = 16;
            for (int i = limit - 5; i < limit; i++) {

                myRef.child("WordsEng").child(String.valueOf(i + 1))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    getWords(dataSnapshot);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                System.out.println("canceld");
                            }
                        });
                //      System.out.println("index " + (i+1) );

            }
            // System.out.println("big list" + wordArrayList.size() );

            myRef.child("UserPoints")
                    .child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        getUserPoints(dataSnapshot);
                        //  System.out.println("Points " + finalPoints );


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            if (uid != null)
                myRef.child("UserLevels").child(uid)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                        levelList.add(snapshot.getValue(Boolean.class));

                                } else {
                                    myRef.child("UserLevels").child(uid).child("1").setValue(true);
                                }
                                LevelsHandeler();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        });


        }

        private void getUserPoints(DataSnapshot dataSnapshot)
        {
            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                points.add(dataSnapshot1.getValue(Integer.class));
            }
            for (int i = 0; i < points.size(); i++) {
                if (points.get(i) != 0) {
                    finalPoints = points.get(i) + points.get(i + 1);
                } else {
                    break;
                }
            }
        }

        private void getWords(DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Word word = snapshot.getValue(Word.class);
                wordArrayList.add(word);
            }
            // pointer++;
            System.out.println("List Size " + wordArrayList.size());
            size.add(wordArrayList.size());
        }
    }}
