package com.Motawer.kalemah;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.Motawer.kalemah.Adapter.onLetterButtonclicked;
import com.Motawer.kalemah.Adapter.recycler_Botton_Adapter;
import com.Motawer.kalemah.RoomDataBase.Word;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class HangMan_Game_Activity extends AppCompatActivity implements onLetterButtonclicked {
    recycler_Botton_Adapter adapter1, adapter2, adapter3, adapter4, adapter5;
    //    String string = "abruptly\n" +
//            "absurd\n" +
//            "abyss\n" +
//            "affix\n" +
//            "askew\n" +
//            "avenue\n" +
//            "awkward\n" +
//            "axiom\n" +
//            "azure\n" +
//            "bagpipes\n" +
//            "bandwagon\n" +
//            "banjo\n" +
//            "bayou\n" +
//            "beekeeper\n" +
//            "bikini\n" +
//            "blitz\n" +
//            "blizzard\n" +
//            "boggle\n" +
//            "bookworm\n" +
//            "boxcar\n" +
//            "boxful\n" +
//            "buckaroo\n" +
//            "buffalo\n" +
//            "buffoon\n" +
//            "buxom\n" +
//            "buzzard\n" +
//            "buzzing\n" +
//            "buzzwords\n" +
//            "caliph\n" +
//            "cobweb\n" +
//            "cockiness\n" +
//            "croquet\n" +
//            "crypt\n" +
//            "curacao\n" +
//            "cycle\n" +
//            "daiquiri\n" +
//            "dirndl\n" +
//            "disavow\n" +
//            "dizzying\n" +
//            "duplex\n" +
//            "dwarves\n" +
//            "embezzle\n" +
//            "equip\n" +
//            "espionage\n" +
//            "euouae\n" +
//            "exodus\n" +
//            "faking\n" +
//            "fishhook\n" +
//            "fixable\n" +
//            "fjord\n" +
//            "flapjack\n" +
//            "flopping\n" +
//            "fluffiness\n" +
//            "flyby\n" +
//            "foxglove\n" +
//            "frazzled\n" +
//            "frizzled\n" +
//            "fuchsia\n" +
//            "funny\n" +
//            "gabby\n" +
//            "galaxy\n" +
//            "galvanize\n" +
//            "gazebo\n" +
//            "giaour\n" +
//            "gizmo\n" +
//            "glowworm\n" +
//            "glyph\n" +
//            "gnarly\n" +
//            "gnostic\n" +
//            "gossip\n" +
//            "grogginess\n" +
//            "haiku\n" +
//            "haphazard\n" +
//            "hyphen\n" +
//            "iatrogenic\n" +
//            "icebox\n" +
//            "injury\n" +
//            "ivory\n" +
//            "ivy\n" +
//            "jackpot\n" +
//            "jaundice\n" +
//            "jawbreaker\n" +
//            "jaywalk\n" +
//            "jazziest\n" +
//            "jazzy\n" +
//            "jelly\n" +
//            "jigsaw\n" +
//            "jinx\n" +
//            "jiujitsu\n" +
//            "jockey\n" +
//            "jogging\n" +
//            "joking\n" +
//            "jovial\n" +
//            "joyful\n" +
//            "juicy\n" +
//            "jukebox\n" +
//            "jumbo\n" +
//            "kayak\n" +
//            "kazoo\n" +
//            "keyhole\n" +
//            "khaki\n" +
//            "kilobyte\n" +
//            "kiosk\n" +
//            "kitsch\n" +
//            "kiwifruit\n" +
//            "klutz\n" +
//            "knapsack\n" +
//            "larynx\n" +
//            "lengths\n" +
//            "lucky\n" +
//            "luxury\n" +
//            "lymph\n" +
//            "marquis\n" +
//            "matrix\n" +
//            "megahertz\n" +
//            "microwave\n" +
//            "mnemonic\n" +
//            "mystify\n" +
//            "naphtha\n" +
//            "nightclub\n" +
//            "nowadays\n" +
//            "numbskull\n" +
//            "nymph\n" +
//            "onyx\n" +
//            "ovary\n" +
//            "oxidize\n" +
//            "oxygen\n" +
//            "pajama\n" +
//            "peekaboo\n" +
//            "phlegm\n" +
//            "pixel\n" +
//            "pizazz\n" +
//            "pneumonia\n" +
//            "polka\n" +
//            "pshaw\n" +
//            "psyche\n" +
//            "puppy\n" +
//            "puzzling\n" +
//            "quartz\n" +
//            "queue\n" +
//            "quips\n" +
//            "quixotic\n" +
//            "quiz\n" +
//            "quizzes\n" +
//            "quorum\n" +
//            "razzmatazz\n" +
//            "rhubarb\n" +
//            "rhythm\n" +
//            "rickshaw\n" +
//            "schnapps\n" +
//            "scratch\n" +
//            "shiv\n" +
//            "snazzy\n" +
//            "sphinx\n" +
//            "spritz\n" +
//            "squawk\n" +
//            "staff\n" +
//            "strength\n" +
//            "strengths\n" +
//            "stretch\n" +
//            "stronghold\n" +
//            "stymied\n" +
//            "subway\n" +
//            "swivel\n" +
//            "syndrome\n" +
//            "thriftless\n" +
//            "thumbscrew\n" +
//            "topaz\n" +
//            "transcript\n" +
//            "transgress\n" +
//            "transplant\n" +
//            "triphthong\n" +
//            "twelfth\n" +
//            "twelfths\n" +
//            "unknown\n" +
//            "unworthy\n" +
//            "unzip\n" +
//            "uptown\n" +
//            "vaporize\n" +
//            "vixen\n" +
//            "vodka\n" +
//            "voodoo\n" +
//            "vortex\n" +
//            "voyeurism\n" +
//            "walkway\n" +
//            "waltz\n" +
//            "wave\n" +
//            "wavy\n" +
//            "waxy\n" +
//            "wellspring\n" +
//            "wheezy\n" +
//            "whiskey\n" +
//            "whizzing\n" +
//            "whomever\n" +
//            "wimpy\n" +
//            "witchcraft\n" +
//            "wizard\n" +
//            "woozy\n" +
//            "wristwatch\n" +
//            "wyvern\n" +
//            "xylophone\n" +
//            "yachtsman\n" +
//            "yippee\n" +
//            "yoked\n" +
//            "youthful\n" +
//            "yummy\n" +
//            "zephyr\n" +
//            "zigzag\n" +
//            "zigzagging\n" +
//            "zilch\n" +
//            "zipper\n" +
//            "zodiac\n" +
//            "zombie";
    int length;
    String word;
    int attembts = 9;
    char[] chars;
    char hinted;
    TextView textView, chances, category;
    Button z;
    ImageButton hint;
    Toolbar toolbar;
    int score = 8;
    ArrayList<String> trueWords = new ArrayList<>();
    boolean hintState = false;
    ImageView hangMan;
    ArrayList<String> dashes = new ArrayList<>();
    //String w ;
    CountDownLatch done = new CountDownLatch(1);

    RecyclerView line1, line2, line3, line4, line5;
    LinearLayoutManager linearLayoutManager, linearLayoutManager2, linearLayoutManager3, linearLayoutManager4, linearLayoutManager5;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = firebaseDatabase.getReference();
    ArrayList<Word> wordArrayList = new ArrayList<>();
    String title;
    String getword;
    int r;
    Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hang_man__game_);
        //  Asynctask task = new Asynctask();
        // task.execute();

        myRef.child("WordsEng").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    InitializeUI();
                    actionbar();
                    r = rand.nextInt((int) snapshot.getChildrenCount()) + 1;

                    System.out.println("random: " + r);
                    for (DataSnapshot datasnapshot : snapshot.child(String.valueOf(r))
                            .getChildren()) {
                        wordArrayList.add(datasnapshot.getValue(Word.class));
                    }
                    if (wordArrayList.size() != 0) {
                        int r1 = rand.nextInt(wordArrayList.size());
                        getword = wordArrayList.get(r1).getWord().toLowerCase();


                    }

                    myRef.child("Words_Eng_Title").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                title = snapshot.child(String.valueOf(r)).getValue(String.class);
                                // done.countDown();
                                if (title != null)
                                    category.setText(title.toUpperCase());
                                word = getword;
                                if (word != null) {
                                    length = word.length();
                                    //getWord();
                                    Log.e("hang word: ", word);
                                    List<Character> sizeList = getChars(word);
                                    System.out.println("word" + sizeList);

                                    setDashes();
                                    buttonsClick(word);
                                    done.countDown();


                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            System.out.println("error" + error);

                        }
                    });

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("error" + error);

            }
        });


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        };


//        for (String s:wordsList)
//            System.out.println(s);


    }

    private void actionbar() {
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        this.getSupportActionBar().setHomeButtonEnabled(true);

    }

    private void buttonsClick(String word) {
        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hintState) {
                    ArrayList<Character> sizeList = (ArrayList<Character>) getChars(word);
                    ArrayList<Character> alt = new ArrayList<>(sizeList);
                    ArrayList<Integer> idx = new ArrayList<>();

                    for (int i = 0; i < trueWords.size(); i++) {
                        char c = trueWords.get(i).charAt(0);

                        for (int j = 0; j < alt.size(); j++) {
                            if (alt.get(j) == c)
                                idx.add(j);


                        }
                    }
                    for (int i = 0; i < idx.size(); i++) {
                        alt.remove(idx.get(i));

                    }

                    Random random = new Random();
                    int r = random.nextInt(alt.size());
                    hinted = alt.get(r);

                    System.out.println("random " + hinted);
                    for (int w = 0; w < sizeList.size(); w++) {
                        if (hinted == sizeList.get(w)) {
                            dashes.set(w, String.valueOf(hinted));
                            System.out.println("dd " + hinted);

                        }
                    }
                    hintState = true;

                    adapter1.setHighligtedItemPosition(String.valueOf(hinted));
                    adapter1.notifyDataSetChanged();
                    adapter2.setHighligtedItemPosition(String.valueOf(hinted));
                    adapter2.notifyDataSetChanged();
                    adapter3.setHighligtedItemPosition(String.valueOf(hinted));
                    adapter3.notifyDataSetChanged();
                    adapter4.setHighligtedItemPosition(String.valueOf(hinted));
                    adapter4.notifyDataSetChanged();
                    adapter5.setHighligtedItemPosition(String.valueOf(hinted));
                    adapter5.notifyDataSetChanged();

                    setTextView();
                }
            }

        });

        z.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Character> sizeList = (ArrayList<Character>) getChars(word);
                String charachter = z.getText().toString();
                boolean isIn = false;
                for (int w = 0; w < sizeList.size(); w++) {
                    if (sizeList.get(w) == charachter.charAt(0)) {
                        dashes.set(w, charachter);
                        isIn = true;
                    }
                }
                if (isIn) {
                    trueWords.add(charachter);
                    settleScore(0);
                    z.setBackgroundResource(R.drawable.right_choise_button);
                    z.setEnabled(false);
                } else if (!isIn) {
                    z.setBackgroundResource(R.drawable.wrong_choice_botton);
                    z.setEnabled(false);
                    settleScore(-1);
                }
                setTextView();
            }
        });
    }

    private List<Character> getChars(String word) {
        List<Character> list = new ArrayList<Character>();
        for (char c : word.toCharArray()) {
            list.add(c);
        }
        return list;
    }

    private void setDashes() {

        for (int i = 0; i < length; i++) {
            dashes.add(" " + "__");
        }
        setTextView();
    }

    private void setTextView() {
        String text = "";
        for (String s : dashes)
            text = text + s + " ";

        textView.setText(text);
        chances.setText(String.valueOf(score));
    }

    private void getWord() {
        // String[] wordsList = string.split("\n");
        //Random rand = new Random(System.currentTimeMillis());
        //int random_number = rand.nextInt(wordsList.length);
        //word = wordsList[random_number];
        length = word.length();
    }

    private void InitializeUI() {
        textView = findViewById(R.id.word_dashes);
        category = findViewById(R.id.category);
        chances = findViewById(R.id.chances);
        toolbar = findViewById(R.id.toolbar);
        z = findViewById(R.id.z);
        hint = findViewById(R.id.hint);
        hangMan = findViewById(R.id.hangman);
        line1 = findViewById(R.id.recycler_botton_line1);
        line2 = findViewById(R.id.recycler_botton_line2);
        line3 = findViewById(R.id.recycler_botton_line3);
        line4 = findViewById(R.id.recycler_botton_line4);
        line5 = findViewById(R.id.recycler_botton_line5);
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(line1);
        recyclerAdapter();

    }

    private void recyclerAdapter() {
        String[] l1 = {"a", "b", "c", "d", "e"};
        String[] l2 = {"f", "g", "h", "i", "j"};
        String[] l3 = {"k", "l", "m", "n", "o"};
        String[] l4 = {"p", "q", "r", "s", "t"};
        String[] l5 = {"u", "v", "w", "x", "y"};
        adapter1 = new recycler_Botton_Adapter(l1, this);
        if (hintState) {
            for (int i = 0; i < l1.length; i++) {
                if (l1[i] == String.valueOf(hinted)) {
                    adapter1.setHighligtedItemPosition(String.valueOf(hinted));
                    adapter1.notifyDataSetChanged();
                }

            }
        }
        adapter2 = new recycler_Botton_Adapter(l2, this);
        if (hintState) {
            adapter2.setHighligtedItemPosition(String.valueOf(hinted));
            adapter2.notifyDataSetChanged();
        }

        adapter3 = new recycler_Botton_Adapter(l3, this);
        if (hintState) {
            adapter3.setHighligtedItemPosition(String.valueOf(hinted));
            adapter3.notifyDataSetChanged();
        }

        adapter4 = new recycler_Botton_Adapter(l4, this);
        if (hintState) {
            adapter4.setHighligtedItemPosition(String.valueOf(hinted));
            adapter4.notifyDataSetChanged();
        }

        adapter5 = new recycler_Botton_Adapter(l5, this);
        if (hintState) {
            adapter5.setHighligtedItemPosition(String.valueOf(hinted));
            adapter5.notifyDataSetChanged();
        }


        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };

        linearLayoutManager2 = new LinearLayoutManager(this
                , RecyclerView.HORIZONTAL, false) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };

        linearLayoutManager3 = new LinearLayoutManager(this
                , RecyclerView.HORIZONTAL, false) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };

        linearLayoutManager4 = new LinearLayoutManager(this
                , RecyclerView.HORIZONTAL, false) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };

        linearLayoutManager5 = new LinearLayoutManager(this
                , RecyclerView.HORIZONTAL, false) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };

        line1.setLayoutManager(linearLayoutManager);
        line1.setAdapter(adapter1);
        line2.setLayoutManager(linearLayoutManager2);
        line2.setAdapter(adapter2);
        line3.setLayoutManager(linearLayoutManager3);
        line3.setAdapter(adapter3);
        line4.setLayoutManager(linearLayoutManager4);
        line4.setAdapter(adapter4);
        line5.setLayoutManager(linearLayoutManager5);
        line5.setAdapter(adapter5);


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

    private void alert() {
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(HangMan_Game_Activity.this);
        builder.setMessage("Are u sure you want to exit?");
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
                                Intent intent = new Intent(HangMan_Game_Activity.this, MainActivity.class);
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
    public boolean onButtonClicked(String letter) {
        if (getword != null)
            try {
                done.await(); //it will wait till the response is received from firebase.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        //System.out.println("letter" + letter);
        List<Character> list = getChars(word);
        if (list.contains(letter.charAt(0))) {
            setletters(letter, list);
            settleScore(0);
            return true;
        } else {
            settleScore(-1);
            return false;
        }
    }

    private void setletters(String letter, List<Character> list) {
        char s = letter.charAt(0);
        System.out.println("char " + s);


        for (int i = 0; i < list.size(); i++) {
            if (s == list.get(i)) {
                trueWords.add(letter);
                dashes.set(i, letter);
                System.out.println("dashes " + dashes);

            }


        }

    }

    @Override
    public void onBackPressed() {

    }

    void settleScore(int i) {
        score += i;
        setHangMan(score);
        setTextView();

        if (score == 0) {


            final Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms

                    dialog(false);

                }
            }, 400);
        }
        //System.out.println(dashes);

        if (!dashes.contains(" __")) {
            dialog(true);
        }


    }

    private void setHangMan(int score) {
        if (score == 7) {
            hangMan.setImageResource(R.drawable.hang_man_2);

        } else if (score == 6) {
            hangMan.setImageResource(R.drawable.hang_man_3);

        } else if (score == 5) {
            hangMan.setImageResource(R.drawable.hang_man_4);

        } else if (score == 4) {
            hangMan.setImageResource(R.drawable.hang_man_5);

        } else if (score == 3) {
            hangMan.setImageResource(R.drawable.hang_man_6);

        } else if (score == 2) {
            hangMan.setImageResource(R.drawable.hang_man_7);

        } else if (score == 1) {
            hangMan.setImageResource(R.drawable.hang_man_8);

        } else if (score == 0) {
            hangMan.setImageResource(R.drawable.hang_man_9);
        }

    }

    private void dialog(boolean state) {
        Dialog dialog = new Dialog(this);
        if (state) {
            SuccessDialog(dialog);
        } else {
            FailDialog(dialog);
        }
    }

    private void FailDialog(Dialog dialog) {
        dialog.setContentView(R.layout.fail_dialog);
        TextView Rate_Text = dialog.findViewById(R.id.points);
        TextView Text_point = dialog.findViewById(R.id.textpoint);
        Button Retry = dialog.findViewById(R.id.retry_btn);
        Button GoAHEAD = dialog.findViewById(R.id.go_ahead);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        Rate_Text.setText("The Correct word is:");
        Text_point.setText(word);
        Text_point.setTextSize(24);

        Retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HangMan_Game_Activity.class);
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
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        Rate_Text.setText(String.valueOf(1));

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

//    class Asynctask extends AsyncTask<Void, Void, String> {
//
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//
//
//        }
//
//        @Override
//        protected String doInBackground(Void... voids) {
//
//
//            // if (getword!=null)
////            try {
////                //done.await(); //it will wait till the response is received from firebase.
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
////            return getword;
//
//        }
//
//        @Override
//        protected void onPostExecute(String strings) {
//            super.onPostExecute(strings);
//
//        }
//
//
//    }
}