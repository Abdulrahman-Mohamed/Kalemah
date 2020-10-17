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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Quizz_activity extends AppCompatActivity implements QuizzFragment.onSomeEventListener {
    FrameLayout frameLayout;
    ProgressBar progressBar;
    Toolbar toolbar;
    TextView quistionsNumberTextView;
    ArrayList<Word> wordsList = new ArrayList<>();
    ArrayList<Word> completeWordsList = new ArrayList<>();
    List<Word> altirnative = new ArrayList<>();
    int day_statics = 0;
    int totalDay_statics ;



    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = firebaseDatabase.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    //ArrayList<Word> Cword = new ArrayList<>();
    String wordQuistion, meaning;
    List<String> meaningList = new ArrayList<>();
   // Word word;
    //final String KEY = "MY_APP_SHARED_PREFRENCES";
  //  final String WORSLIST = "MY_APP_WORDS_LIST";
    WordsViewModel viewModel;

    int index;
    int Reciver = 0;
    int successRate = 0;
    int progress = 0;
    int level,points_limit;
    int USER_POINTS;
    //boolean hasAword = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz_activity);
        viewModel = new ViewModelProvider(this).get(WordsViewModel.class);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        };
        points_limit=getIntent().getIntExtra("points_limit",0);
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
        // this.getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                alert();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void alert() {
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(Quizz_activity.this);
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
                                Intent intent = new Intent(Quizz_activity.this, Categories_Activity.class);
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

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
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
        //getLevelListOfWords();
        List<String> stringList = new ArrayList<>();
        altirnative = new ArrayList<>(wordsList);

        Log.i("alt list size" + 187, String.valueOf(altirnative.size()));
        Random random = new Random();
        //if (wordsList!=null) {
        int i2, i3, i4;
        String m1, m2, m3, m4;
        m1 = altirnative.get(index).getMeaning();
        Log.i("True_Meaning" + 187, String.valueOf(m1));
        meaning = m1;
        altirnative.remove(index);
        Log.i("alt list size" + 195, String.valueOf(altirnative.size()));
        i2 = random.nextInt(altirnative.size());
        m2 = altirnative.get(i2).getMeaning();
        altirnative.remove(i2);
//
        Log.i("alt list size" + 200, String.valueOf(altirnative.size()));
        i3 = random.nextInt(altirnative.size());
        m3 = altirnative.get(i3).getMeaning();
        altirnative.remove(i3);
//
        Log.i("alt list size" + 205, String.valueOf(altirnative.size()));
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
            // GenerateRevisionWords();
            int random = new Random().nextInt(20);
            index = random;
            String word = wordsList.get(index).getWord();

            return word;
        }
        int random = new Random().nextInt(wordsList.size());
        index = random;
        String word = wordsList.get(index).getWord();
        Log.i("word" + 187, String.valueOf(word));
        return word;
    }

    private void getLevelListOfWords() {
        //  getPoints();
//        if (USER_POINTS >= 40) {
//            GenerateRevisionWords();
//        } else {
        completeWordsList = (ArrayList<Word>) getIntent().getSerializableExtra("WordsList");
        if (completeWordsList!=null)
        wordsList = new ArrayList<>(completeWordsList);

        Log.i("complete list size" + 249, String.valueOf(completeWordsList.size()));


        //     }
    }

//    private void GenerateRevisionWords() {
//
//        loadData();
//        ArrayList<Word> List = new ArrayList<>();
//        List = (ArrayList<Word>) getIntent().getSerializableExtra("WordsList");
//        wordsList = new ArrayList<>();
//        if (Cword.size() >= 20) {
//            wordsList = new ArrayList<>(Cword);
//        } else if (Cword.size() < 20) {
//            for (int i = 0; i < Cword.size(); i++) {
//                wordsList.add(Cword.get(i));
//                Log.e("hey", Cword.get(Cword.size() - 1).getWord());
//                Log.e("hoo", String.valueOf(Cword.size()));
//            }
//            for (int i = 0; wordsList.size() <= 20; i++) {
//                wordsList.add(List.get(i));
//            }
//            Log.e("hoo", String.valueOf(wordsList.size()));
//
//        }
//
//
//    }

//    private void loadData() {
//
//        SharedPreferences sharedPreferences = getSharedPreferences(KEY, Context.MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString(WORSLIST, null);
//        Type type = new TypeToken<ArrayList<Word>>() {
//        }.getType();
//        Cword = gson.fromJson(json, type);
//
//        if (Cword == null) {
//            Cword = new ArrayList<>();
//        }
//    }

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
        System.out.println("level quizz: "+level);
    }

    @Override
    public void sendRate(int rate) {
        if (rate == 1) {
            successRate = ++successRate;
        } else if (rate == 0) {
            // SaveWord();

            insertWord();
        }
    }

    private boolean setViewModel(String wordQuistion, String meaning) {
        final boolean[] statue = {false};
        viewModel = new ViewModelProvider(this).get(WordsViewModel.class);
        viewModel.getAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                for (int i = 0; i < words.size(); i++) {
                    // see if the word or meaning is allready in our data base //could be updated
                    if (words.get(i).getWord().equals(wordQuistion) || words.get(i).getMeaning().equals(meaning)) {
                        statue[0] = true;
                        break;
                    }
                }
            }
        });
        return statue[0];


    }

//    private void SaveWord() {
//        word = new Word(wordQuistion, meaning, -3);
//        Cword.add(word);
//        SharedPreferences sharedPref = getSharedPreferences(KEY, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(Cword);
//        editor.putString(WORSLIST, json);
//        editor.apply();
//    }


    private void insertWord() {
        boolean repeated = setViewModel(wordQuistion, meaning);
        if (!repeated) {
            Word word = new Word(wordQuistion, meaning, -3, 5);
            Log.i("new word added", wordQuistion);

            viewModel.insetr(word);
        } else {
            Log.i("repeated word", wordQuistion);
        }
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
        saveSharedStatistics();
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


                Intent intent = new Intent(v.getContext(), Quizz_activity.class);
                dialog.dismiss();
                intent.putExtra("WordsList", completeWordsList);
                startActivity(intent);
                finish();
            }
        });
        GoAHEAD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Categories_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void SuccessDialog(Dialog dialog) {
        dialog.setContentView(R.layout.success_dialog);

        savePoints();
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
                savePoints();
                dialog.dismiss();
                Intent intent = new Intent(v.getContext(), Categories_Activity.class);
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
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String date1 = String.valueOf(dateFormat.format(date));
        String[] words = date1.split("/");//splits the string based on whitespace
        year = Integer.parseInt(words[0]);
        month = Integer.parseInt(words[1]);
        day = Integer.parseInt(words[2]);
        myRef.child("Users")
                .child(firebaseAuth.getCurrentUser()
                        .getUid()).child("stat").child("GRE_Test").child(String.valueOf(year))
                .child(String.valueOf(month)).addListenerForSingleValueEvent(new ValueEventListener() {
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

                        if (points >= 10 && points < 40) {
                            total = successRate + points;
                            myRef.child("UserPoints")
                                    .child(firebaseAuth.getCurrentUser().getUid())
                                    .child(String.valueOf(level)).setValue(total);
                            return ;
                        }else if (points >= 40 && points < 60) {
                            total = successRate + points;
                            if (points_limit != 2) {
                                if (total > 60) {
                                    myRef.child("UserPoints")
                                            .child(firebaseAuth.getCurrentUser().getUid())
                                            .child(String.valueOf(level)).setValue(60);
                                    return;
                                }
                            } else{
                                if (total>60)
                                myRef.child("UserPoints")
                                        .child(firebaseAuth.getCurrentUser().getUid())
                                        .child(String.valueOf(level)).setValue(60);
                                myRef.child("UserLevels").child(firebaseAuth.getCurrentUser().getUid()).child(String.valueOf(level+1)).setValue(true);


                                return;}
                        }else if (points==0){

//                        }else if (points >= 60 && points < 120) {
//                            total = successRate + points;
//
//
//                                if (total > 120) {
//                                    myRef.child("UserPoints")
//                                            .child(firebaseAuth.getCurrentUser().getUid())
//                                            .child(String.valueOf(level)).setValue(120);
//                                    return;
//                                }else {
//
//                            myRef.child("UserPoints")
//                                    .child(firebaseAuth.getCurrentUser().getUid())
//                                    .child(String.valueOf(level)).setValue(total);
//                            return;}
//                        }else

                        myRef.child("UserPoints")
                                .child(firebaseAuth.getCurrentUser().getUid()).child(String.valueOf(level)).setValue(successRate);
                    }}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}

