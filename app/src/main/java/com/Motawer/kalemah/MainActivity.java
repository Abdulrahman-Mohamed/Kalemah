package com.Motawer.kalemah;

import android.animation.Animator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.Motawer.kalemah.Auth.SignIn_Activity;
import com.Motawer.kalemah.Fragments.exams_frag;
import com.Motawer.kalemah.Fragments.profile_frag;
import com.Motawer.kalemah.Fragments.words_frag;
import com.Motawer.kalemah.MaterialDesign.AddWord_Dialog;
import com.Motawer.kalemah.MaterialDesign.AddWord_DialogEdit;
import com.Motawer.kalemah.RoomDataBase.Word;
import com.Motawer.kalemah.ViewModel.WordsViewModel;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements AddWord_Dialog.BottomSheetListner, AddWord_DialogEdit.AddWordSheetEditeListner
        , com.Motawer.kalemah.Fragments.words_frag.GetID, AddWord_DialogEdit.refreshrecycler {

    // Fragment selectedFragment = null;
    private WordsViewModel viewModel;
    int wordIdentefire;
    ImageButton ExamsButton;
    FloatingActionButton WordsFloatingActionButton;
    CircleImageView ProfileImageView;
    // BottomAppBar bottomAppBar;
    FirebaseAuth firebaseAuth;
    int place_word;
    int place_exam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Window window=getWindow();
//        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                , WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // bottomAppBar = findViewById(R.id.bottomAppBar);
        Initialize();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frag_container, new words_frag()).commit();
        getSupportFragmentManager().beginTransaction().addToBackStack(null);
        WordsFloatingActionButton.setImageResource(R.drawable.ic_plus_wese5_new);
        place_word = 1;

        firebaseAuth = FirebaseAuth.getInstance();
        ExamsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (place_word == 1) {
                    YoYo.with(Techniques.RotateOut)
                            .duration(500)
                            .onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    WordsFloatingActionButton.setImageResource(R.drawable.ic_yellow_feather_new);
                                    YoYo.with(Techniques.RotateIn)
                                            .duration(500).playOn(WordsFloatingActionButton);
                                }
                            })
                            .playOn(WordsFloatingActionButton);

                }
                place_word = 0;
                if (place_exam == 0) {
                    place_exam = 1;
                    YoYo.with(Techniques.FadeOut)
                            .duration(400)
                            .onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    ExamsButton.setImageResource(R.drawable.ic_a_mark_yellow);
                                    YoYo.with(Techniques.BounceInUp)
                                            .duration(500).playOn(ExamsButton);
                                }
                            }).playOn(ExamsButton);
                    if (ProfileImageView.getBorderWidth() == 1)
                        ProfileImageView.setBorderWidth(0);
//                YoYo.with(Techniques.RotateIn)
//                        .duration(700)
//                        .playOn(ExamsButton);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frag_container, new exams_frag()).commit();
                    getSupportFragmentManager().beginTransaction().addToBackStack(null);
                }
            }
        });

        WordsFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (place_exam == 1) {
                    place_exam = 0;
                    YoYo.with(Techniques.FadeOut)
                            .duration(400)
                            .onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    ExamsButton.setImageResource(R.drawable.ic_a_mark_gray);
                                    YoYo.with(Techniques.BounceInUp)
                                            .duration(500).playOn(ExamsButton);
                                }
                            }).playOn(ExamsButton);
                }

                if (place_word == 0)
                {

                    place_word = 1;
                    ProfileImageView.setBorderWidth(0);
                    YoYo.with(Techniques.RotateOut)
                            .duration(700)
                            .onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    WordsFloatingActionButton.setImageResource(R.drawable.ic_plus_wese5_new);
                                    YoYo.with(Techniques.RotateIn)
                                            .duration(700).playOn(WordsFloatingActionButton);
                                }
                            })
                            .playOn(WordsFloatingActionButton);


                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frag_container, new words_frag()).commit();

                    getSupportFragmentManager().beginTransaction().addToBackStack(null);
                }
                else if (place_word == 1)
                {
                   openDialog();
                }
            }
        });
        ProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (place_exam == 1) {
                    place_exam = 0;
                    YoYo.with(Techniques.FadeOut)
                            .duration(400)
                            .onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    ExamsButton.setImageResource(R.drawable.ic_a_mark_gray);
                                    YoYo.with(Techniques.BounceInUp)
                                            .duration(500).playOn(ExamsButton);
                                }
                            }).playOn(ExamsButton);
                }

                if (place_word == 1) {
                    YoYo.with(Techniques.RotateOut)
                            .duration(500)
                            .onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    WordsFloatingActionButton.setImageResource(R.drawable.ic_yellow_feather_new);
                                    YoYo.with(Techniques.RotateIn)
                                            .duration(500).playOn(WordsFloatingActionButton);
                                }
                            })
                            .playOn(WordsFloatingActionButton);

                }
                place_word = 0;
                ProfileImageView.setBorderWidth(5);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_container, new profile_frag()).commit();
                getSupportFragmentManager().beginTransaction().addToBackStack(null);
            }
        });

        viewModel = new

                ViewModelProvider(this).

                get(WordsViewModel.class);


//        final MeowBottomNavigation btv = findViewById(R.id.botnav);
//        btv.add(new MeowBottomNavigation.Model(3, R.drawable.a_mark));
//        btv.add(new MeowBottomNavigation.Model(2, R.drawable.selector));
//        btv.add(new MeowBottomNavigation.Model(1, R.drawable.ic_person_black_24dp));

//defult item

        //click item but not show
//        btv.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
//            @Override
//            public void onClickItem(final MeowBottomNavigation.Model item) {
//
//                switch (item.getId()) {
//                    case 1:
//                        selectedFragment = new profile_frag();
//                        break;
//                    case 2:
//                      //  btv.clearAnimation();
//                           // item.setIcon(R.drawable.ic_add_black_24dp);
//                     //   item.setIcon(R.drawable.ic_add_black_24dp);
//                      //  item.setIcon(R.drawable.ic_add_black_24dp);
//                 //       btv.clearCount(2);
//                     //   btv.clearCount();
//                       // btv.getModelPosition(2);
//                    //    btv.add(new MeowBottomNavigation.Model(2, R.drawable.ic_add_black_24dp));
//                     //   btv.getModelById(2).setIcon(R.drawable.ic_add_black_24dp);
//                        selectedFragment = new words_frag();
//                        //   item.setIcon(R.drawable.ic_add_black_24dp);
//                        // btv.setSelected(true);
//                        break;
//                    case 3:
//                        selectedFragment = new exams_frag();
//
//                        break;
//                }
////                Toast.makeText(MainActivity.this, "clicked item : " + item.getId(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        //show but without click
//        btv.setOnShowListener(new MeowBottomNavigation.ShowListener() {
//            @Override
//            public void onShowItem(final MeowBottomNavigation.Model item) {
//
//                switch (item.getId()) {
//                    case 1:
//                        selectedFragment = new profile_frag();
//                        break;
//                    case 2:
//                        //  item.setIcon(R.drawable.ic_add_black_24dp);
//                        // onContentChanged();
////                        btv.clearCount(2);
////                        btv.add(new MeowBottomNavigation.Model(2, R.drawable.ic_add_black_24dp));
//                       // btv.getModelById(2).setIcon(R.drawable.ic_add_black_24dp);
//                        btv.clearAnimation();
//                        selectedFragment = new words_frag();
//
//
//                        break;
//                    case 3:
//                        selectedFragment = new exams_frag();
//                        break;
//                }
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.frag_container, selectedFragment).commit();
//                getSupportFragmentManager().beginTransaction().addToBackStack(null);
//            }
//        });
//        btv.show(2, false);
//
//        //re_click handler
//        btv.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
//            @Override
//            public void onReselectItem(final MeowBottomNavigation.Model item) {
//
//                switch (item.getId()) {
//                    case 1:
//                        selectedFragment = new profile_frag();
//                        break;
//                    case 2:
//                        btv.clearAnimation();
//                        selectedFragment = new words_frag();
////                        BottomSheet bottomSheet = new BottomSheet();
////                        bottomSheet.show(getSupportFragmentManager(), "BottomSheetdialogMain");
//                        break;
//                    case 3:
//                        selectedFragment = new exams_frag();
//                        break;
//
//                }
//                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, selectedFragment).commit();
//                getSupportFragmentManager().beginTransaction().addToBackStack(null);
//
//                //Toast.makeText(MainActivity.this, "reselected item : " + item.getId(), Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    private void openDialog()
    {
        AddWord_Dialog addWord_dialog = new AddWord_Dialog();
        addWord_dialog.show(getSupportFragmentManager(),"WordDialog");


    }
    private void Initialize() {
        ExamsButton = findViewById(R.id.exams_icon);
        ProfileImageView = findViewById(R.id.profile_image);
        WordsFloatingActionButton = findViewById(R.id.words_fab);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            Uri personPhoto = acct.getPhotoUrl();
            Picasso.get()
                    .load(String.valueOf(personPhoto))
                    .resize(50, 50)
                    .centerCrop()
                    .into(ProfileImageView);
        }
    }


    @Override
    public void getidfrompos(int id) {
        wordIdentefire = id;
    }

    @Override
    public void onRecyclerRefresh() {

        words_frag fragment = (words_frag) getSupportFragmentManager().getFragments().get(0);
        getSupportFragmentManager().beginTransaction()
                .detach(fragment)
                .attach(fragment)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);

        }
    }

    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

        } else {
            startActivity(new Intent(MainActivity.this, SignIn_Activity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }

    @Override
    public void onButtomClicked(String Word, String meaning, String level, int rate) {
        Word word = new Word(Word, meaning, Integer.parseInt(level.trim()), rate);
        viewModel.insetr(word);
    }


    @Override
    public void onAddWordEditClicked(String Word, String meaning, String level, int rate) {
        Word word = new Word(Word, meaning, Integer.parseInt(level.trim()), rate);
        word.setID(wordIdentefire);
        viewModel.update(word);
    }
}