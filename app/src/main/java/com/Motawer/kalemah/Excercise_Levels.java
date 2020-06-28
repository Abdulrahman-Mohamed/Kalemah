package com.Motawer.kalemah;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.Motawer.kalemah.Adapter.examFragmentAdapter;
import com.Motawer.kalemah.Fragments.ExamFragments.LevelsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Excercise_Levels extends AppCompatActivity {
    ViewPager2 viewPager2;
    FragmentStateAdapter levelAdapter;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = firebaseDatabase.getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    List<Fragment> fragmentArrayList = new ArrayList<>();
    int levels;
    List<Boolean> levelList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excercise__quizz);


        //     Toast.makeText(this,String.valueOf(levels),Toast.LENGTH_LONG).show();
        InitDatabase();
        setPoints();
        LevelsHandeler();


    }

    private void LevelsHandeler()
    {
        final List<Integer> points = new ArrayList<>();
        myRef.child("UserPoints")
                .child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        points.add(dataSnapshot1.getValue(Integer.class));

                    }
                    for (int levels=1;levels<points.size();levels++)
                    {
                        if (points.get(levels-1)>=30)
                            myRef.child("UserLevels").child(uid).child(String.valueOf(levels+1)).setValue(true);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setPoints()
    {

            myRef.child("UserPoints")
                    .child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists())
                        {
                            for (int levels=1;levels<=16;levels++)
                                    myRef.child("UserPoints")
                                            .child(uid).child(String.valueOf(levels)).setValue(0);
                        }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    private void InitDatabase() {
        if (!uid.equals(null))
            myRef.child("UserLevels").child(uid)
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren())
                            levelList.add(snapshot.getValue(Boolean.class));

                    } else {
                        myRef.child("UserLevels").child(uid).child("1").setValue(true);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        myRef.child("wordsCount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    levels = Integer.parseInt(dataSnapshot.child("WordsEng").
                            child("count").getValue(String.class));
                    // Log.e("levels",String.valueOf(levels));
                    InitViewPager();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("The read failed: ", String.valueOf(databaseError.getCode()));

            }
        });
    }


    private void InitViewPager() {
        if (levels > 0) {
            for (int i = 0; i < levels; i++) {
                Bundle bundle = new Bundle();
                bundle.putInt("level", i + 1);

                fragmentArrayList.add(i, new LevelsFragment());
                fragmentArrayList.get(i).setArguments(bundle);
            }
        }

        viewPager2 = findViewById(R.id.pager);
        levelAdapter = new examFragmentAdapter(getSupportFragmentManager(), getLifecycle(), fragmentArrayList);

        viewPager2.setAdapter(levelAdapter);

    }
}
