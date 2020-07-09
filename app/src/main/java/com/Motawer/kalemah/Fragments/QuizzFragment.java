package com.Motawer.kalemah.Fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.Motawer.kalemah.R;
import com.Motawer.kalemah.RoomDataBase.Word;
import com.Motawer.kalemah.ViewModel.WordsViewModel;

import java.util.ArrayList;
import java.util.Random;

public class QuizzFragment extends Fragment {
    View view;
    Button button1, button2, button3, button4;
    TextView word, wordcounter;
    String recivedWord, meaning;
    onSomeEventListener someEventListener;
    ArrayList<String> listMeanings = new ArrayList<>();
    //    ArrayList<Word> Cword ;

    private WordsViewModel viewModel;
    MediaPlayer right;
    MediaPlayer wrong;
    String choise, type;
    int level, index = 0, size, id,old_rate;
    boolean checkerword;



    public QuizzFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
            , @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(WordsViewModel.class);

        if (getArguments() != null) {
            recivedWord = getArguments().getString("word");
            listMeanings = getArguments().getStringArrayList("meanings");
            level = getArguments().getInt("Level");
            type = getArguments().getString("type");
            id = getArguments().getInt("ID");
            old_rate=getArguments().getInt("rate");

//
        }
        view = inflater.inflate(R.layout.frag_choose_quizz, container, false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        wordcounter = getActivity().findViewById(R.id.counter_quistions);
        right = MediaPlayer.create(getActivity(), R.raw.success);
        wrong = MediaPlayer.create(getActivity(), R.raw.fail);
        InitChoose();
        if (listMeanings.size() != 0)
            meaning = listMeanings.get(0);
        if (recivedWord != null)
            setWord();
        if (listMeanings.size() != 0)
            setMeanings();
        buttons();


    }

    boolean check(String choise) {
        if (choise.equals(meaning)) {

            return true;

        }
        return false;

    }

    private void buttons() {
        final Handler handler = new Handler();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choise = button1.getText().toString();
                boolean result = check(choise);
                if (result) {
                    button1.setBackgroundResource(R.drawable.right_choise_button);
                    right.start();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                        //    setRate();

                            someEventListener.sendRate(1);
                            someEventListener.sendcounter(1);

                        }
                    }, 800);
                } else {
                    wrong.start();
                    button1.setBackgroundResource(R.drawable.wrong_choice_botton);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            someEventListener.sendRate(0);
                            someEventListener.sendcounter(1);
                        }
                    }, 800);
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choise = button2.getText().toString();
                boolean result = check(choise);
                if (result) {
                    right.start();
//
                    button2.setBackgroundResource(R.drawable.right_choise_button);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                          //  setRate();
                            someEventListener.sendRate(1);
                            someEventListener.sendcounter(1);
                        }
                    }, 800);
                } else {
//
                    button2.setBackgroundResource(R.drawable.wrong_choice_botton);
                    wrong.start();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            someEventListener.sendRate(0);
                            someEventListener.sendcounter(1);
                        }
                    }, 800);
                }
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choise = button3.getText().toString();
                boolean result = check(choise);
                if (result) {
                    right.start();
                    button3.setBackgroundResource(R.drawable.right_choise_button);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                          //  setRate();
                            someEventListener.sendRate(1);
                            someEventListener.sendcounter(1);
                        }
                    }, 800);
                } else {
                    wrong.start();
                    button3.setBackgroundResource(R.drawable.wrong_choice_botton);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            someEventListener.sendRate(0);
                            someEventListener.sendcounter(1);
                        }
                    }, 800);
                }
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choise = button4.getText().toString();
                boolean result = check(choise);
                if (result) {
                    right.start();
                    button4.setBackgroundResource(R.drawable.right_choise_button);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                       //     setRate();
                            someEventListener.sendRate(1);
                            someEventListener.sendcounter(1);
                        }
                    }, 800);
                } else {
                    wrong.start();
                    button4.setBackgroundResource(R.drawable.wrong_choice_botton);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            someEventListener.sendRate(0);
                            someEventListener.sendcounter(1);
                        }
                    }, 800);
                }
            }
        });
    }

    private void setRate() {
        if (type.equals("wordRevvv")) {
            int rate;
            Word word;
            //  word.setID(id);
            // Log.e("rate",String.valueOf(old_rate));

            if (old_rate < 5) {
                rate = (old_rate + 1);
                checkRate(rate);
                word = new Word(recivedWord, meaning, level,rate);

                Log.e("OLD_rate", String.valueOf(old_rate));
                Log.e("new_Rate", String.valueOf(rate));
                word.setID(id);
                viewModel.update(word);
            } else {
                word = new Word(recivedWord, meaning, -1);
                word.setID(id);
                viewModel.update(word);
            }
        }

    }

    private void checkRate(int rate) {
        Word word;
        if (rate == 3 && level == -3) {
            word = new Word(recivedWord, meaning, -2);
            word.setID(id);
            viewModel.update(word);
        }
        if (rate == 5 && level == -3) {
            word = new Word(recivedWord, meaning, -1);
            word.setID(id);
            viewModel.update(word);
        }
        if (rate == 3 && level == -2) {
            word = new Word(recivedWord, meaning, -1);
            word.setID(id);
            viewModel.update(word);
        }

    }

    private void setMeanings() {
        Random random = new Random();
        int a = random.nextInt(listMeanings.size());
        button1.setText(listMeanings.get(a));
        listMeanings.remove(a);
        int b = random.nextInt(listMeanings.size());
        button2.setText(listMeanings.get(b));
        listMeanings.remove(b);
        int c = random.nextInt(listMeanings.size());
        button3.setText(listMeanings.get(c));
        listMeanings.remove(c);
        button4.setText(listMeanings.get(0));
    }

    private void setWord() {
        word.setText(recivedWord);
    }

    private void InitChoose() {
        word = view.findViewById(R.id.quistion_word);
        button1 = view.findViewById(R.id.button2);
        button2 = view.findViewById(R.id.button3);
        button3 = view.findViewById(R.id.button4);
        button4 = view.findViewById(R.id.button5);


    }

    public interface onSomeEventListener {
        void sendRate(int rate);

        void sendcounter(int counter);


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            someEventListener = (onSomeEventListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
        }
    }
}
