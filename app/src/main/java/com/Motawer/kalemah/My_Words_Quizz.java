package com.Motawer.kalemah;

import android.app.Dialog;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.Motawer.kalemah.Fragments.QuizzFragment;
import com.Motawer.kalemah.RoomDataBase.Word;
import com.Motawer.kalemah.ViewModel.WordsViewModel;
import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class My_Words_Quizz extends AppCompatActivity implements QuizzFragment.onSomeEventListener {
    FrameLayout frameLayout;
    ProgressBar progressBar;
    Toolbar toolbar;
    TextView quistionsNumberTextView;
    ArrayList<Word> wordsList = new ArrayList<>();
    ArrayList<Word> wordsA = new ArrayList<>();
    ArrayList<Word> wordsB = new ArrayList<>();
    ArrayList<Word> wordsC = new ArrayList<>();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = firebaseDatabase.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    ArrayList<Word> doubleList = new ArrayList<>();

    ArrayList<Integer> Random = new ArrayList<>();


    ArrayList<Integer> sizes = new ArrayList<>();

    String wordQuistion;
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
    int counter = 0;
    int day_statics = 0;
    int totalDay_statics ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__words__quizz);
        //   Log.e("thread", String.valueOf(Thread.currentThread()));
        //lottieAnimationView.setAnimation(getAssets().open());
        viewModel = new ViewModelProvider(My_Words_Quizz.this).get(WordsViewModel.class);
        if (wordsList.size() == 0)
            //checkSizeWordsMethod();
            getAllWords();
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        };


        initViews();

        actionbar();

        // getLevelListOfWords();

    }

    private void alert() {
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(My_Words_Quizz.this);
        builder.setMessage("Are u sure you want to exit?\n\nYour progress won't be saved!");
        builder.setTitle("Alert Message !");
        builder.setCancelable(false);
        builder
                .setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent intent = new Intent(My_Words_Quizz.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                // When the user click yes button
                                // then app will close
                            }
                        });

        // Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.
        builder
                .setNegativeButton(
                        "No",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                // If user click no
                                // then dialog box is canceled.
                                dialog.cancel();
                            }
                        });
        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }


    private void getAllWords() {
        viewModel.getlevelWords((-3)).observeForever(new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {

                wordsC = new ArrayList<>(words);
                counter++;
                Log.i("wordsC", String.valueOf(wordsC.size()));
                if (counter == 3)
                    checkSizeWordsMethod();
            }
        });
        viewModel.getlevelWords((-2)).observeForever(new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                counter++;
                wordsB = new ArrayList<>(words);
                Log.i("wordsB", String.valueOf(wordsB.size()));
                if (counter == 3)
                    checkSizeWordsMethod();
            }
        });
        viewModel.getlevelWords((-1)).observeForever(new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                counter++;

                wordsA = new ArrayList<>(words);
                Log.i("wordsA", String.valueOf(wordsA.size()));

                if (counter == 3)
                    checkSizeWordsMethod();
            }

        });

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
                alert();


                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fragentStart(ArrayList<Integer> random) {
        viewModel.getlevelWords(-3).removeObservers(this);
        viewModel.getlevelWords(-2).removeObservers(this);
        viewModel.getlevelWords(-1).removeObservers(this);
        // Log.i("generated words" + 112, String.valueOf(generateword.size()));

        int ran = new Random().nextInt(wordsList.size());
        index = ran;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (wordsList.size() != 0) {
            Log.e("words size", String.valueOf(wordsList.size()));
            wordQuistion = wordsList.get(index).getWord();
            level = wordsList.get(index).getLevel();
            id = wordsList.get(index).getID();
            rate = wordsList.get(index).getRate();
            meaningList = generatMeanings(index);
           // wordsList.remove(index);
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
       int counter=1;
       stringList.add(altirnative.get(index).getMeaning());
       while (true)
       {

           Random random = new Random();
           int i=random.nextInt(altirnative.size());
           if (!stringList.contains(altirnative.get(i).getMeaning())){
               stringList.add(altirnative.get(i).getMeaning());
               counter++;
           }
           if (counter==4)
               break;

       }
//        Log.i("alt list size" + 187, String.valueOf(altirnative.size()));
//        Random random = new Random();
//        int i2, i3, i4;
//        String m1, m2, m3, m4;
//       m1 = altirnative.get(index).getMeaning();
//        meaning = m1;
//        altirnative.remove(index);
//        Log.i("alt list size" + 195, String.valueOf(altirnative.size()));
//        i2 = random.nextInt(altirnative.size());
//        m2 = altirnative.get(i2).getMeaning();
//        altirnative.remove(i2);
//
//        Log.i("alt list size" + 200, String.valueOf(altirnative.size()));
//        i3 = random.nextInt(altirnative.size());
//        m3 = altirnative.get(i3).getMeaning();
//        altirnative.remove(i3);
//
//        Log.i("alt list size" + 205, String.valueOf(altirnative.size()));
//        i4 = random.nextInt(altirnative.size());
//        m4 = altirnative.get(i4).getMeaning();
//        altirnative.remove(i4);
//
//        stringList.add(m1);
//        stringList.add(m2);
//        stringList.add(m3);
//        stringList.add(m4);


        return stringList;

    }

//    private String meaningLoop(String meaning) {
//        for (int i = 0; i < wordsList.size(); i++) {
//            if (wordsList.get(i).getMeaning() != meaning) {
//                return wordsList.get(i).getMeaning();
//            }
//        }
//        return "";
//    }
//
//    private String meaningLoop(String meaning, String m2) {
//        for (int i = 0; i < wordsList.size(); i++) {
//            if (!wordsList.get(i).getMeaning().equals(meaning) && !wordsList.get(i).getMeaning().equals(m2)) {
//                return wordsList.get(i).getMeaning();
//            }
//        }
//        return "";
//    }
//
//    private String meaningLoop(String meaning, String m2, String m3) {
//        for (int i = 0; i < wordsList.size(); i++) {
//            if (!wordsList.get(i).getMeaning().equals(meaning)
//                    && !wordsList.get(i).getMeaning().equals(m2)
//                    && !wordsList.get(i).getMeaning().equals(m3)) {
//                return wordsList.get(i).getMeaning();
//            }
//        }
//        return "";
//    }


    private void generateword() {
        //  String singleword;
        Random = new ArrayList<>();
        for (int i = 0; i < wordsList.size(); i++) {
            int random = new Random().nextInt(wordsList.size());
            Random.add(random);
        }
        Log.e("random", String.valueOf(Random.size()));

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


        // return Random;
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


    //    private void fillWordsList() {
//        // check method array of [-3,-2,-1] call viewmodel.getlevelwords one time
//
//
//        Log.i("sizes size" + 246, String.valueOf(sizes.size()));
//        if (sizes.size() < 3) {
//            Log.e("sizes error" + 249, String.valueOf(sizes.size()));
//            return;
//        }
//
//
//
//    }
    private void getLevelWords(int counter, int i) {
        int c = 0;
        if (i == -3) {
            if (wordsC.size() >= counter) {
                while (true) {
                    Random rand = new Random();
                    int r = rand.nextInt(wordsC.size());
                    if (!wordsList.contains(wordsC.get(r)))
                    {c++;
                        wordsList.add(wordsC.get(r));}
                    if (c == counter)
                        break;
                }
            } else {
                while (true) {

                    c++;
                    Random rand = new Random();
                    int r = rand.nextInt(wordsC.size());
                    wordsList.add(wordsC.get(r));
                    if (c == counter)
                        break;
                }
            }
        } else if (i == -2) {
            if (wordsB.size() >= counter) {
                while (true) {
                    Random rand = new Random();
                    int r = rand.nextInt(wordsB.size());
                    if (!wordsList.contains(wordsB.get(r)))
                    { c++;
                        wordsList.add(wordsB.get(r));}
                    if (c == counter)
                        break;
                }
            } else {
                while (true) {

                    c++;
                    Random rand = new Random();
                    int r = rand.nextInt(wordsB.size());
                    wordsList.add(wordsB.get(r));
                    if (c == counter)
                        break;
                }
            }
        } else {
            if (wordsA.size() >= counter) {
                while (true) {

                    Random rand = new Random();
                    int r = rand.nextInt(wordsA.size());
                    if (!wordsList.contains(wordsA.get(r))){
                        c++;
                        wordsList.add(wordsA.get(r));}
                    if (c == counter)
                        break;
                }

            } else {
                while (true) {
                    c++;
                    Random rand = new Random();
                    int r = rand.nextInt(wordsA.size());
                    wordsList.add(wordsA.get(r));
                    if (c == counter)
                        break;
                }
            }
        }

    }

    private void getLevelWords_test(int counter, int i) {

        viewModel.getlevelWords((i)).observe(My_Words_Quizz.this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                //  Log.e("list", String.valueOf(words.size()));
//                if (wordsList.size() == 0)
                if (words.size() >= counter) {
                    for (int i = 0; i < counter; i++) {
//                        if (wordsList.size() == 20) {
//                            return;
//                        }
                        wordsList.add(words.get(i));
                        Log.e("list3", String.valueOf(wordsList.get(i).getWord()));

                        Log.i("list-3", String.valueOf(wordsList.get(i)));

                    }
                    Log.i("list-3", String.valueOf(wordsList.size()));

                } else {
                    if (!words.isEmpty() || words.size() != 0) {
                        int random = new Random().nextInt(words.size());
                        Log.e("random 285", String.valueOf(random));

                        while (true) {
                            if (wordsList.size() == counter) {
                                break;
                            }
                            wordsList.add(words.get(random));
                            //    Log.e("rate",String.valueOf(words.get(i).getRate())+words.get(i).getWord());

                        }
                    }

                }
            }
        });
    }


    private void checkSizeWordsMethod() {

        sizes = new ArrayList<>();
        if (wordsC.size() != 0)
            sizes.add(wordsC.size());
        else
            sizes.add(0);
        if (wordsB.size() != 0)
            sizes.add(wordsB.size());
        else
            sizes.add(0);

        if (wordsA.size() != 0)
            sizes.add(wordsA.size());
        else
            sizes.add(0);

        if (sizes.size() == 3)
            InitiatGetWords();



    }

    private void InitiatGetWords() {
        if (sizes.get(1) == 0 && sizes.get(2) == 0) {
            getLevelWords(20, -3);
            //      fragentStart();


        } else if (sizes.get(0) == 0 && sizes.get(1) == 0) {
            getLevelWords(20, -1);
            //    fragentStart();


        } else if (sizes.get(0) == 0 && sizes.get(2) == 0) {
            getLevelWords(20, -2);
            //    fragentStart();

        } else {
            if (sizes.get(0) > 0)
                if (sizes.get(0) >= 10) {
                    getLevelWords(10, -3);
                } else {
                    getLevelWords(sizes.get(0), -3);
                }
            if (sizes.get(1) > 0) {
                if (sizes.get(1) >= 5) {
                    getLevelWords(5, -2);
                } else {
                    getLevelWords(sizes.get(1), -2);
                }
            }
            if (sizes.get(2) > 0)
                if (sizes.get(2) >= 5) {
                    getLevelWords(5, -1);
                } else {
                    getLevelWords(sizes.get(2), -1);
                }
            if (wordsList.size() < 20) {
                int max;
                int remain = (20 - wordsList.size());
                max = Collections.max(sizes);
                if (sizes.get(0) == max) {
                    getLevelWordsRandom(remain, -3);

                } else if (sizes.get(1) == max) {
                    getLevelWordsRandom(remain, -2);

                } else if (sizes.get(2) == max) {
                    getLevelWordsRandom(remain, -1);

                }


            }

        }

        if (Reciver == 0)
        {
            generateword();
            fragentStart(Random);
        }
        // fragentStart();
    }

    private void getLevelWordsRandom(int remain, int i) {
        int c=0;
        if (i == -3) {

                while (true) {

                    c++;
                    Random rand = new Random();
                    int r = rand.nextInt(wordsC.size());
                    wordsList.add(wordsC.get(r));
                    if (c == remain)
                        break;
                }

        } else if (i == -2)

    {
        while (true) {

            c++;
            Random rand = new Random();
            int r = rand.nextInt(wordsB.size());
            wordsList.add(wordsB.get(r));
            if (c == remain)
                break;
        }

    }else {

                while (true) {
                    c++;
                    Random rand = new Random();
                    int r = rand.nextInt(wordsA.size());
                    wordsList.add(wordsA.get(r));
                    if (c == remain)
                        break;
                }
            }}




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
            successRate = ++successRate;
            if (wordsList.get(index).getRate() <= 5) {
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

                }
            }
        } else if (rate == 0) {

        }

        generateword();
        fragentStart(Random);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

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
        saveSharedStatistics();
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
        dialog.setCancelable(false);

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
                Intent intent = new Intent(v.getContext(), MainActivity.class);

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
        dialog.setCancelable(false);

        Rate_Text.setText(String.valueOf(successRate));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // savePoints();
                Intent intent = new Intent(v.getContext(), MainActivity.class);

                startActivity(intent);
                finish();
            }
        });
    }
    private void saveSharedStatistics() {


        int year;
        int month;
        int day;
//        String mKey="MONTH";
//        String dKey="DAY";
        DateFormat dateFormat = new SimpleDateFormat("YYYY/MM/dd");
        Date date = new Date();
        String date1 = String.valueOf(dateFormat.format(date));
        String[] words = date1.split("/");//splits the string based on whitespace
        year = Integer.parseInt(words[0]);
        month = Integer.parseInt(words[1]);
        day = Integer.parseInt(words[2]);
        myRef.child("Users")
                .child(firebaseAuth.getCurrentUser()
                        .getUid()).child("stat").child("Local_Test").child(String.valueOf(year)).child(String.valueOf(month)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    day_statics = snapshot.child(String.valueOf(day)).getValue(Integer.class);
                    totalDay_statics = day_statics + 1;
                    myRef.child("Users")
                            .child(firebaseAuth.getCurrentUser()
                                    .getUid()).child("stat").child("GRE_Test").child(String.valueOf(year)).child(String.valueOf(month)).child(String.valueOf(day)).setValue(totalDay_statics);



                } else {
                    for (int day = 1; day <= 30; day++) {
                        myRef.child("Users")
                                .child(firebaseAuth.getCurrentUser()
                                        .getUid()).child("stat").child("GRE_Test").child(String.valueOf(year)).child(String.valueOf(month)).child(String.valueOf(day)).setValue(0);
                    }
                    myRef.child("Users")
                            .child(firebaseAuth.getCurrentUser()
                                    .getUid()).child("stat").child("GRE_Test").child(String.valueOf(year)).child(String.valueOf(month)).child(String.valueOf(day)).setValue(1);


                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Log.i("Date", String.valueOf("month " + month + " day " + day));
//        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putInt(mKey,month);
//        editor.putInt(dKey,day);
//        editor.put
//        editor.apply();


    }
}