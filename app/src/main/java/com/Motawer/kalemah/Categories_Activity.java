package com.Motawer.kalemah;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Motawer.kalemah.Adapter.Categories_Adapter;
import com.Motawer.kalemah.Models.CategoriesItems;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Categories_Activity extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    Categories_Adapter categories_adapter;
    ArrayList<CategoriesItems> list = new ArrayList<>();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = firebaseDatabase.getReference();
    List<Boolean> levelList = new ArrayList<>();
    ArrayList<Integer> points=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_);
        FireBase();
        setPoints();
    }

//    private void setLevels()
//    {
//        for (int i = 1; i <points.size() ; i++) {
//            if (points.get(i-1)>60*i)
//            {
//                String level=String.valueOf(i+1);
//                myRef.child("UserLevels").child(uid).child(level).setValue(true);
//
//
//            }
//        }
//
//    }

    private void InitializeRecycler() {
        recyclerView = findViewById(R.id.Recycler_Categories);
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        Context context=Categories_Activity.this;
        categories_adapter=new Categories_Adapter( context,list);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });
        recyclerView.setAdapter(categories_adapter);




    }

    private void setPoints() {

        myRef.child("UserPoints")
                .child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    for (int levels = 1; levels <= 16; levels++)
                        myRef.child("UserPoints")
                                .child(uid).child(String.valueOf(levels)).setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void FireBase() {
        if (uid != null)
            myRef.child("UserLevels").child(uid)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                    levelList.add(snapshot.getValue(Boolean.class));
                                //Log.e("hi", "iam here " );


                            } else {
                                myRef.child("UserLevels").child(uid).child("1").setValue(true);
                                levelList.add(0, true);
                                //  Log.e("hi", "iam here but no snap " );

                            }
                            if (levelList.size() <= 5) {
                                list.add(new CategoriesItems("Beginners Words", "5", String.valueOf(levelList.size())));
                                list.add(new CategoriesItems("Intermediate Words", "5", "0"));
                                list.add(new CategoriesItems("Advanced Words", "5", "0"));
                            }
                            if (levelList.size() > 5 && levelList.size() <= 10) {
                                list.add(new CategoriesItems("Beginners Words", "5", "5"));
                                list.add(new CategoriesItems("Intermediate Words", "5", String.valueOf(levelList.size())));
                                list.add(new CategoriesItems("Advanced Words", "5","0"));
                            }
                            if (levelList.size() > 10 && levelList.size() <= 15) {
                                list.add(new CategoriesItems("Beginners Words", "5", "5"));
                                list.add(new CategoriesItems("Intermediate Words", "5", "5"));
                                list.add(new CategoriesItems("Advanced Words", "5", String.valueOf(levelList.size())));
                            }
                            //setLevels();

                            InitializeRecycler();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        myRef.child("UserPoints")
                .child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    for (int i = 0; i <snapshot.getChildrenCount() ; i++) {
                        points.add(snapshot.child(String.valueOf(i+1)).getValue(Integer.class));

                    }
               // setLevels();

                    }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}