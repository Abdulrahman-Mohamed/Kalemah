package com.Motawer.kalemah;

import android.app.Dialog;
import android.content.Intent;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.Motawer.kalemah.Fragments.QuizzFragment;
import com.Motawer.kalemah.RoomDataBase.Word;
import com.Motawer.kalemah.ViewModel.WordsViewModel;
import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class My_Words_Quizz extends AppCompatActivity implements QuizzFragment.onSomeEventListener {
    FrameLayout frameLayout;
    ProgressBar progressBar;
    Toolbar toolbar;
    TextView quistionsNumberTextView;
    ArrayList<Word> wordsList = new ArrayList<>();
    // ArrayList<Word> checkRateList = new ArrayList<>();
    ArrayList<Word> doubleList = new ArrayList<>();

    ArrayList<Integer> Random = new ArrayList<>();


    String wordQuistion, meaning;
    List<String> meaningList = new ArrayList<>();

    LottieAnimationView lottieAnimationView;

    WordsViewModel viewModel;

    int index;
    int Reciver = 0;
    int successRate = 0;
    int progress = 0;
    int level;
    int id;
    int rate;
  //  int sucessrate = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__words__quizz);
        //   Log.e("thread", String.valueOf(Thread.currentThread()));
        //lottieAnimationView.setAnimation(getAssets().open());
        viewModel = new ViewModelProvider(My_Words_Quizz.this).get(WordsViewModel.class);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(My_Words_Quizz.this, Excercise_Levels.class);

                startActivity(intent);
                finish();
            }
        };
        initViews();
        actionbar();
        if (wordsList.size() == 0)
            fillWordsList();
        // getLevelListOfWords();

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
                Intent intent = new Intent(My_Words_Quizz.this, MainActivity.class);

                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fragentStart(ArrayList<Integer> generateword) {
        viewModel.getlevelWords(-3).removeObservers(this);
        viewModel.getlevelWords(-2).removeObservers(this);
        viewModel.getlevelWords(-1).removeObservers(this);

        index = new Random().nextInt(generateword.size());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (wordsList.size() != 0) {
            Log.e("WordsList", String.valueOf(wordsList.size()));
            wordQuistion = wordsList.get(index).getWord();
            level = wordsList.get(index).getLevel();
            id = wordsList.get(index).getID();
            rate = wordsList.get(index).getRate();
            meaningList = generatMeanings(index);
        }


        QuizzFragment QuizzFragment = new QuizzFragment();
        transaction.replace(R.id.frameLayout, QuizzFragment);
        //  transaction.addToBackStack(null);
        Bundle bundle = new Bundle();
        bundle.putString("word", wordQuistion);
        // bundle.putString("type", "wordRev");
        bundle.putInt("Level", level);
        bundle.putInt("ID", id);
        //     bundle.putInt("rate", rate);
        bundle.putStringArrayList("meanings", (ArrayList<String>) meaningList);
        QuizzFragment.setArguments(bundle);
        transaction.commit();
    }

    private List<String> generatMeanings(int index) {
        List<String> stringList = new ArrayList<>();
        List<Word> altirnative = new ArrayList<>(wordsList);

        String m1, m2, m3, m4;
        m1 = wordsList.get(index).getMeaning();
        meaning = m1;
        altirnative.remove(index);
        m2 = meaningLoop(meaning);
        m3 = meaningLoop(meaning, m2);
        m4 = meaningLoop(meaning, m2, m3);

//        i2 = random.nextInt(altirnative.size());
//        m2 = altirnative.get(i2).getMeaning();
//        altirnative.remove(i2);
////
//        i3 = random.nextInt(altirnative.size());
//        m3 = altirnative.get(i3).getMeaning();
//        altirnative.remove(i3);
////
//        i4 = random.nextInt(altirnative.size());
//        m4 = altirnative.get(i4).getMeaning();
//        altirnative.remove(i4);

        stringList.add(m1);
        stringList.add(m2);
        stringList.add(m3);
        stringList.add(m4);

        return stringList;
    }

    private String meaningLoop(String meaning) {
        for (int i = 0; i < wordsList.size(); i++) {
            if (wordsList.get(i).getMeaning() != meaning) {
                return wordsList.get(i).getMeaning();
            }
        }
        return "";
    }

    private String meaningLoop(String meaning, String m2) {
        for (int i = 0; i < wordsList.size(); i++) {
            if (!wordsList.get(i).getMeaning().equals(meaning) && !wordsList.get(i).getMeaning().equals(m2)) {
                return wordsList.get(i).getMeaning();
            }
        }
        return "";
    }

    private String meaningLoop(String meaning, String m2, String m3) {
        for (int i = 0; i < wordsList.size(); i++) {
            if (!wordsList.get(i).getMeaning().equals(meaning)
                    && !wordsList.get(i).getMeaning().equals(m2)
                    && !wordsList.get(i).getMeaning().equals(m3)) {
                return wordsList.get(i).getMeaning();
            }
        }
        return "";
    }


    private ArrayList<Integer> generateword() {
        //  String singleword;
        for (int i = 0; i < wordsList.size(); i++) {
            int random = new Random().nextInt(wordsList.size());
            Random.add(random);
        }
        // index = random;
//        int random = new Random().nextInt(wordsList.size());
//        index = random;
//       Word word= checkWord(index);
//       if (word!=null){
//       singleword = word.getWord();
//           id  = word.getID();
//           rate = word.getRate();}
//       else
//           {
//               singleword=wordsList.get(index).getWord();
//               id  = wordsList.get(index).getID();
//               rate = wordsList.get(index).getRate();
//           }


        //     Log.e("word rate", String.valueOf(rate) + " " + wordsList.get(index).getWord());


        return Random;
    }

//    private Word checkWord(int index) {
//        if (checkRateList.size() != 0)
//            for (int i = 0; i < checkRateList.size(); i++) {
//                if (checkRateList.get(i).getWord().equals(wordsList.get(index).getWord())) {
//                    return checkRateList.get(i);
//                }
//            }
//        return null;
//    }


    private void fillWordsList() {
        viewModel.getlevelWords((-3)).observe(My_Words_Quizz.this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                //  Log.e("list", String.valueOf(words.size()));
                if (wordsList.size() == 0)
                    if (words.size() >= 10) {
                        for (int i = 0; i < 10; i++) {
//                        if (wordsList.size() == 20) {
//                            return;
//                        }
                            wordsList.add(words.get(i));

                            // Log.e("list2", String.valueOf(wordsList.get(i).getRate()));

                        }
                    } else {
                        for (int i = 0; i < words.size(); i++) {
                            if (wordsList.size() == 20) {
                                return;
                            }
                            wordsList.add(words.get(i));
                            //Log.e("rate",String.valueOf(words.get(i).getRate())+words.get(i).getWord());

                        }
                    }
            }
        });
      //  viewModel.getlevelWords(-3).removeObservers(this);

        viewModel.getlevelWords((-2)).observe(My_Words_Quizz.this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                if (wordsList.size() < 20)
                    if (words.size() >= 5) {
                        for (int i = 0; i < 5; i++) {
//                        if (wordsList.size() == 20) {
//                            return;
//                        }
                            wordsList.add(words.get(i));
                            //  Log.e("rate",String.valueOf(words.get(i).getRate())+words.get(i).getWord());

                            //   Log.e("list3", String.valueOf(wordsList.get(i+10).getRate()));

                        }

                    } else {
                        for (int i = 0; i < words.size(); i++) {
                            if (wordsList.size() == 20) {
                                return;
                            }
                            wordsList.add(words.get(i));
                            // Log.e("rate",String.valueOf(words.get(i).getRate())+words.get(i).getWord());

                        }
                    }
            }
        });

    //    viewModel.getlevelWords(-2).removeObservers(this);


        viewModel.getlevelWords((-1)).observe(My_Words_Quizz.this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                if (wordsList.size() < 20) {
                    if (words.size() >= 5) {
                        for (int i = 0; i < 5; i++) {
                            if (wordsList.size() == 20) {
                                return;
                            }
                            wordsList.add(words.get(i));
                            // Log.e("rate",String.valueOf(words.get(i).getRate())+words.get(i).getWord());

                            //  Log.e("list4", String.valueOf(wordsList.get(i+15).getRate()));

                        }
                    }
                    if (words.size() < 5) {
                        for (int i = 0; i < words.size(); i++) {
                            if (wordsList.size() == 20) {
                                return;
                            }
                            wordsList.add(words.get(i));
                            //  Log.e("rate",String.valueOf(words.get(i).getRate())+words.get(i).getWord());

                        }

                    }

                    if (Reciver == 0) {
                        generateword();
                        fragentStart(Random);
                    }

                }

            }
        });

      //  viewModel.getlevelWords(-1).removeObservers(this);
    }

    private void initViews() {
        lottieAnimationView = findViewById(R.id.animation_view);
        toolbar = findViewById(R.id.toolbar);
        frameLayout = findViewById(R.id.frameLayout);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressBar.setProgress(progress);
        quistionsNumberTextView = findViewById(R.id.counter_quistions);
    }

    @Override
    public void sendRate(int rate) {
        int myRate = 0;
        int counter = 0;
        viewModel.getlevelWords(-3).removeObservers(this);
        viewModel.getlevelWords(-2).removeObservers(this);
        viewModel.getlevelWords(-1).removeObservers(this);

        if (rate == 1) {
            if (wordsList.get(index).getRate()<=5) {
                doubleList.add(wordsList.get(index));
                for (int i = 0; i < doubleList.size(); i++) {
                    if (doubleList.get(i).getWord() == wordsList.get(index).getWord()) {
                        //  Double=true;
                        counter = ++counter;
                        // myRate = wordsList.get(index).getRate() + 2;
                    }
                }

                myRate = wordsList.get(index).getRate() + counter;

                if (myRate < 3) {
                    //            checkRate(myRate);
                    Log.e("OLD_rate", String.valueOf(wordsList.get(index).getRate()));
                    Log.e("new_Rate", String.valueOf(myRate));
                    Word word = new Word(wordsList.get(index).getWord()
                            , wordsList.get(index).getMeaning(), wordsList.get(index).getLevel(), myRate);
                    word.setID(wordsList.get(index).getID());
                    viewModel.update(word);
                }
                if (myRate >= 5) {
                    Log.e("OLD_rate", String.valueOf(wordsList.get(index).getRate()));
                    Log.e("new_Rate", String.valueOf(myRate));
                    Word word = new Word(wordsList.get(index).getWord()
                            , wordsList.get(index).getMeaning(), -1);
                    word.setID(wordsList.get(index).getID());
                    viewModel.update(word);
                }
                if (myRate >= 3 && myRate < 5) {
                    Log.e("OLD_rate", String.valueOf(wordsList.get(index).getRate()));
                    Log.e("new_Rate", String.valueOf(myRate));

                    Word word = new Word(wordsList.get(index).getWord()
                            , wordsList.get(index).getMeaning(), -2, myRate);
                    word.setID(wordsList.get(index).getID());
                    viewModel.update(word);


                    successRate = ++successRate;
                }
            }
        } else if (rate == 0) {
        }
        fragentStart(Random);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(My_Words_Quizz.this, MainActivity.class);
        startActivity(intent);
    }



    @Override
    public void sendcounter(int counter) {
        Reciver = ++Reciver;

        if (Reciver <= 20) {
            {
                quistionsNumberTextView.setText(String.valueOf(Reciver));
                progress = progress + 5;
            }
        }
        if (Reciver == 20) {
            dialog();
        }
        progressBar.setProgress(progress);
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
                Intent intent = new Intent(v.getContext(), My_Words_Quizz.class);
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
        //savePoints();
        TextView Rate_Text = dialog.findViewById(R.id.points);
        Button button = dialog.findViewById(R.id.succeed_btn);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        Rate_Text.setText(String.valueOf(successRate));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // savePoints();
                Intent intent = new Intent(v.getContext(), Excercise_Levels.class);

                startActivity(intent);
                finish();
            }
        });
    }

}


