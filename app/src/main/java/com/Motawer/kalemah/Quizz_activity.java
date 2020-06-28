package com.Motawer.kalemah;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.Motawer.kalemah.Fragments.QuizzFragment;
import com.Motawer.kalemah.RoomDataBase.Word;
import com.Motawer.kalemah.ViewModel.WordsViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Quizz_activity extends AppCompatActivity implements QuizzFragment.onSomeEventListener {
    FrameLayout frameLayout;
    ProgressBar progressBar;
    Toolbar toolbar;
    TextView quistionsNumberTextView;
    ArrayList<Word> wordsList = new ArrayList<>();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = firebaseDatabase.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    ArrayList<Word> Cword = new ArrayList<>();
    String wordQuistion, meaning;
    List<String> meaningList = new ArrayList<>();
    Word word;
    final String KEY = "MY_APP_SHARED_PREFRENCES";
    final String WORSLIST = "MY_APP_WORDS_LIST";
    WordsViewModel viewModel;

    int index;
    int Reciver = 0;
    int successRate = 0;
    int progress = 0;
    int level;
    int USER_POINTS;
    boolean hasAword = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz_activity);
        viewModel = new ViewModelProvider(this).get(WordsViewModel.class);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(Quizz_activity.this, Excercise_Levels.class);

                startActivity(intent);
                finish();
            }
        };
        initViews();
        actionbar();
        getLevelListOfWords();
        if (Reciver == 0) {
            fragentStart();
        }
    }

    private void actionbar() {
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        this.getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Quizz_activity.this, Excercise_Levels.class);

                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fragentStart() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        wordQuistion = generateword();
        meaningList = generatMeanings();
        wordsList.remove(index);
        QuizzFragment QuizzFragment = new QuizzFragment();
        transaction.replace(R.id.frameLayout, QuizzFragment);
      //  transaction.addToBackStack(null);
        Bundle bundle = new Bundle();
        bundle.putString("word", wordQuistion);
        bundle.putInt("Level", level);
        bundle.putStringArrayList("meanings", (ArrayList<String>) meaningList);
        QuizzFragment.setArguments(bundle);
        transaction.commit();
    }

    private List<String> generatMeanings() {
        getLevelListOfWords();
        List<String> stringList = new ArrayList<>();
        List<Word> altirnative = new ArrayList<>(wordsList);
        Random random = new Random();
        //if (wordsList!=null) {
        int i2, i3, i4;
        String m1, m2, m3, m4;
        m1 = altirnative.get(index).getMeaning();
        meaning = m1;
        altirnative.remove(index);
        i2 = random.nextInt(altirnative.size());
        m2 = altirnative.get(i2).getMeaning();
        altirnative.remove(i2);
//
        i3 = random.nextInt(altirnative.size());
        m3 = altirnative.get(i3).getMeaning();
        altirnative.remove(i3);
//
        i4 = random.nextInt(altirnative.size());
        m4 = altirnative.get(i4).getMeaning();
        altirnative.remove(i4);
//
        stringList.add(m1);
        stringList.add(m2);
        stringList.add(m3);
        stringList.add(m4);
//boolean check=verifyAllEqualUsingALoop(stringList);
//if (check)
//{
//    Log.e("dublicaion","dublicate");
//
//}
        //}
        return stringList;
    }

    private String generateword() {
        //GenerateRevisionWords();
        if (USER_POINTS >= 40) {
            wordsList = new ArrayList<>();
            GenerateRevisionWords();
            int random = new Random().nextInt(20);
            index = random;
            String word = wordsList.get(index).getWord();

            return word;
        }
        int random = new Random().nextInt(wordsList.size());
        index = random;
        String word = wordsList.get(index).getWord();

        return word;
    }

    private void getLevelListOfWords() {
        getPoints();
        if (USER_POINTS >= 40) {
            GenerateRevisionWords();
        } else {
            wordsList = (ArrayList<Word>) getIntent().getSerializableExtra("WordsList");

        }
    }

    private void GenerateRevisionWords() {

        loadData();
        ArrayList<Word> List = new ArrayList<>();
        List = (ArrayList<Word>) getIntent().getSerializableExtra("WordsList");
        wordsList = new ArrayList<>();
        if (Cword.size() >= 20) {
            wordsList = new ArrayList<>(Cword);
        } else if (Cword.size() < 20) {
            for (int i = 0; i < Cword.size(); i++) {
                wordsList.add(Cword.get(i));
                Log.e("hey", Cword.get(Cword.size() - 1).getWord());
                Log.e("hoo", String.valueOf(Cword.size()));
            }
            for (int i = 0; wordsList.size() <= 20; i++) {
                wordsList.add(List.get(i));
            }
            Log.e("hoo", String.valueOf(wordsList.size()));

        }


    }

    private void loadData() {

        SharedPreferences sharedPreferences = getSharedPreferences(KEY, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(WORSLIST, null);
        Type type = new TypeToken<ArrayList<Word>>() {
        }.getType();
        Cword = gson.fromJson(json, type);

        if (Cword == null) {
            Cword = new ArrayList<>();
        }
    }

    private void getPoints() {
        myRef.child("UserPoints")
                .child(firebaseAuth.getCurrentUser()
                        .getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                    if (dataSnapshot.child(String.valueOf(level)).getValue(Integer.class) != null) {
                        USER_POINTS = dataSnapshot.child(String.valueOf(level)).getValue(Integer.class);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        frameLayout = findViewById(R.id.frameLayout);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressBar.setProgress(progress);
        quistionsNumberTextView = findViewById(R.id.counter_quistions);
        level = getIntent().getIntExtra("Level", 0);
    }

    @Override
    public void sendRate(int rate) {
        if (rate == 1) {
            successRate = ++successRate;
//

        } else if (rate == 0) {
            SaveWord();
        }
    }

    private void SaveWord() {
        word = new Word(wordQuistion, meaning, -3);
        Cword.add(word);
//
//        LiveData<List<Word>> wordList = viewModel.getAllWords();
//        wordList.observe(this, new Observer<List<Word>>() {
//            @Override
//            public void onChanged(List<Word> words) {
//
//                     if (!words.contains(word.getWord()))
//                    {
//                        insertWord();
//                    }
//                }
//
//        //    }
//
//
//
//        });


        // viewModel.insetr(word);
        SharedPreferences sharedPref = getSharedPreferences(KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(Cword);
        editor.putString(WORSLIST, json);
        editor.apply();
    }


    private void insertWord() {
        Word word = new Word(wordQuistion, meaning, -3);

        viewModel.insetr(word);
    }

    @Override
    public void sendcounter(int counter) {
        Reciver = ++Reciver;
        if (Reciver <= 20) {
            {
//                count=count+1;
                quistionsNumberTextView.setText(String.valueOf(Reciver));
                progress = progress + 5;
            }
        }
        if (Reciver == 20) {
            dialog();
        }
        progressBar.setProgress(progress);
        fragentStart();
    }

    private void dialog() {
        Dialog dialog = new Dialog(this);
        if (successRate >= 10) {
            SuccessDialog(dialog);
        } else {
            FailDialog(dialog);
        }
    }

    private void FailDialog(Dialog dialog) {
        dialog.setContentView(R.layout.fail_dialog);
        TextView Rate_Text = dialog.findViewById(R.id.points);
        Button Retry = dialog.findViewById(R.id.retry_btn);
        Button GoAHEAD = dialog.findViewById(R.id.go_ahead);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        Rate_Text.setText(String.valueOf(successRate));
        Retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Quizz_activity.class);
                intent.putExtra("WordsList", wordsList);
                startActivity(intent);
            }
        });
        GoAHEAD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Excercise_Levels.class);

                startActivity(intent);
            }
        });
    }

    private void SuccessDialog(Dialog dialog) {
        dialog.setContentView(R.layout.success_dialog);
        TextView Rate_Text = dialog.findViewById(R.id.points);
        Button button = dialog.findViewById(R.id.succeed_btn);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        Rate_Text.setText(String.valueOf(successRate));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePoints();
                Intent intent = new Intent(v.getContext(), Excercise_Levels.class);

                startActivity(intent);
                finish();
            }
        });
    }

    private void savePoints() {
        myRef.child("UserPoints")
                .child(firebaseAuth.getCurrentUser()
                        .getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int points;
                int total;
                if (dataSnapshot.exists())
                    if (dataSnapshot.child(String.valueOf(level)).getValue(Integer.class) != null) {
                        points = dataSnapshot.child(String.valueOf(level)).getValue(Integer.class);

                        if (points >= 20 && points < 40) {
                            total = successRate + points;
                            myRef.child("UserPoints")
                                    .child(firebaseAuth.getCurrentUser().getUid())
                                    .child(String.valueOf(level)).setValue(total);
                            return;
                        }
                        if (points >= 40 && points < 60) {
                            total = successRate + points;
                            if (total > 60) {
                                myRef.child("UserPoints")
                                        .child(firebaseAuth.getCurrentUser().getUid())
                                        .child(String.valueOf(level)).setValue(60);
                                return;
                            }
                            myRef.child("UserPoints")
                                    .child(firebaseAuth.getCurrentUser().getUid())
                                    .child(String.valueOf(level)).setValue(total);
                            return;
                        }

                        myRef.child("UserPoints")
                                .child(firebaseAuth.getCurrentUser().getUid()).child(String.valueOf(level)).setValue(successRate);
                    }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}

