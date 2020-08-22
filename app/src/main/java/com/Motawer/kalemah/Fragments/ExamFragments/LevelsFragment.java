package com.Motawer.kalemah.Fragments.ExamFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.Motawer.kalemah.Quizz_activity;
import com.Motawer.kalemah.R;
import com.Motawer.kalemah.RoomDataBase.Word;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LevelsFragment extends Fragment {
    View view;
    ImageView star1, star2, star3;
    Button button;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = firebaseDatabase.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private ArrayList<Boolean> LevelsList = new ArrayList<>();
    boolean levelAccess;
    ArrayList<Word> wordsList = new ArrayList<>();
    //    int myStr = getArguments().getInt("my_key");
    int level, points = 0;
    TextView textView;
    FrameLayout lock;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.levels_frag, container, false);
        textView = view.findViewById(R.id.text_level_count);
        // بناخد رقم المرحله من الكلاس اللي قبله
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            level = bundle.getInt("level", 0);
        }
        // بنطبع رقم المرحله في التيكست فيو
        textView.setText(String.valueOf(level));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitViews();
        getPoints();
//        Toast.makeText(requireContext(),LevelsList.get(0).toString(),Toast.LENGTH_LONG).show();
        // startButton();

    }

    private void StarsDisplay() {
        if (points > 0) {
            if (points >= 10) {
                star1.setImageResource(R.drawable.ic_star);
                if (points > 20) {
                    star2.setImageResource(R.drawable.ic_star);
                }
                if (points == 60) {
                    star3.setImageResource(R.drawable.ic_star);
                }
            }

        }
    }

    private void getPoints() {
        myRef.child("UserPoints")
                .child(uid).child(String.valueOf(level)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    points = dataSnapshot.getValue(Integer.class);
                    StarsDisplay();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void firebaseinit()
    {
        myRef.child("UserLevels")
                .child(firebaseAuth.getCurrentUser().getUid())

                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            if (dataSnapshot.child(String.valueOf(level))
                                    .getValue(Boolean.class) != null) {
                                levelAccess = dataSnapshot.child(String.valueOf(level)).getValue(Boolean.class);
                                if (!levelAccess)
                                    lock.setVisibility(View.VISIBLE);
                            } else {
                                lock.setVisibility(View.VISIBLE);
                                button.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

// on card click
    private void startButton() {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setInitialPoint();
                Intent intent = new Intent(getActivity(), Quizz_activity.class);
                intent.putExtra("WordsList",wordsList);
                intent.putExtra("Level",level);
                startActivity(intent);
                getActivity().finish();

            }
        });
    }
// set initial Level points
    private void setInitialPoint() {
        myRef.child("UserPoints")
                .child(firebaseAuth.getCurrentUser().getUid()).child(String.valueOf(level)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    return;

                myRef.child("UserPoints")
                        .child(firebaseAuth.getCurrentUser().getUid())
                        .child(String.valueOf(level)).setValue(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void InitViews() {
        star1 = view.findViewById(R.id.star1);
        star2 = view.findViewById(R.id.star2);
        star3 = view.findViewById(R.id.star3);
        button = view.findViewById(R.id.start_button);
        lock = view.findViewById(R.id.lock);
        firebaseinit();
        getWords();


    }
// get words from current level needs edit
    private void getWords() {
        myRef.child("WordsEng").child(String.valueOf(level)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Word word = snapshot.getValue(Word.class);
                        wordsList.add(word);
                    }
                startButton();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
