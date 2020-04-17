package com.Motawer.kalemah.Fragments.ExamFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private ArrayList<Boolean> LevelsList=new ArrayList<>();
    ArrayList<Word> wordsList=new ArrayList<>();
//    int myStr = getArguments().getInt("my_key");
    int level;
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.levels_frag, container, false);
        textView = view.findViewById(R.id.text_level_count);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            level = bundle.getInt("level", 0);
        }
        textView.setText(String.valueOf(level));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitViews();
//        Toast.makeText(requireContext(),LevelsList.get(0).toString(),Toast.LENGTH_LONG).show();
       // startButton();

    }

    private void firebaseinit()
    {
        myRef.child("Users").child(firebaseAuth.getCurrentUser().getUid())
                .child("UserLevels").child(String.valueOf(level))
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (dataSnapshot.exists()){

                   String levels =dataSnapshot.getValue(String.class);
                   if (levels.equals("true")) {
                       LevelsList.add(true);
                   }else
                       {
                       LevelsList.add(false);
                }
                  // toast(String.valueOf(LevelsList.get(0)));
               }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

myRef.child("WordsEng").child(String.valueOf(level)).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists())
            for (DataSnapshot snapshot:dataSnapshot.getChildren())
            {
                Word word=snapshot.getValue(Word.class);
                wordsList.add(word);
            }
        startButton();

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
    }



    private void startButton()
    {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Quizz_activity.class);
                intent.putExtra("level",level);
                //stoped
                intent.putExtra("WordsList",wordsList);
                startActivity(intent);
            }
        });
    }

    private void InitViews() {
        star1 = view.findViewById(R.id.star1);
        star2 = view.findViewById(R.id.star2);
        star3 = view.findViewById(R.id.star3);
        button = view.findViewById(R.id.start_button);
        firebaseinit();

    }
}
