package com.Motawer.kalemah;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.Motawer.kalemah.Auth.SignIn_Activity;
import com.Motawer.kalemah.Fragments.exams_frag;
import com.Motawer.kalemah.Fragments.profile_frag;
import com.Motawer.kalemah.Fragments.words_frag;
import com.Motawer.kalemah.MaterialDesign.BottomSheet;
import com.Motawer.kalemah.MaterialDesign.BottomSheetEdit;
import com.Motawer.kalemah.RoomDataBase.Word;
import com.Motawer.kalemah.ViewModel.WordsViewModel;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements BottomSheet.BottomSheetListner, BottomSheetEdit.BottomSheetEditeListner
        , com.Motawer.kalemah.Fragments.words_frag.GetID,BottomSheetEdit.refreshrecycler
{

    Fragment selectedFragment = null;
    private WordsViewModel viewModel;
    int wordIdentefire;
    FirebaseAuth firebaseAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Window window=getWindow();
//        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                , WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        firebaseAuth=FirebaseAuth.getInstance();



        viewModel = new ViewModelProvider(this).get(WordsViewModel.class);

        final MeowBottomNavigation btv = findViewById(R.id.botnav);
        btv.add(new MeowBottomNavigation.Model(3, R.drawable.a_mark));
        btv.add(new MeowBottomNavigation.Model(2, R.drawable.selector));
        btv.add(new MeowBottomNavigation.Model(1, R.drawable.ic_person_black_24dp));



        //click item but not show
        btv.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(final MeowBottomNavigation.Model item) {

                switch (item.getId()) {
                    case 1:
                        selectedFragment = new profile_frag();
                        break;
                    case 2:
                        btv.clearAnimation();

                        selectedFragment = new words_frag();
                     //   item.setIcon(R.drawable.ic_add_black_24dp);
                       // btv.setSelected(true);
                        break;
                    case 3:
                        selectedFragment = new exams_frag();

                        break;
                }
//                Toast.makeText(MainActivity.this, "clicked item : " + item.getId(), Toast.LENGTH_SHORT).show();
            }
        });

        //show but without click
        btv.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(final MeowBottomNavigation.Model item) {

                switch (item.getId()) {
                    case 1:
                        selectedFragment = new profile_frag();
                        break;
                    case 2:
                      //  item.setIcon(R.drawable.ic_add_black_24dp);
                       // onContentChanged();
                        btv.clearAnimation();
                        selectedFragment = new words_frag();


                        break;
                    case 3:
                        selectedFragment = new exams_frag();
                        break;
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_container, selectedFragment).commit();
            }
        });

        //re_click handler
        btv.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(final MeowBottomNavigation.Model item) {

                switch (item.getId()) {
                    case 1:
                        selectedFragment = new profile_frag();
                        break;
                    case 2:
                        btv.clearAnimation();
                        selectedFragment = new words_frag();
//                        BottomSheet bottomSheet = new BottomSheet();
//                        bottomSheet.show(getSupportFragmentManager(), "BottomSheetdialogMain");
                        break;
                    case 3:
                        selectedFragment = new exams_frag();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, selectedFragment).commit();
                //Toast.makeText(MainActivity.this, "reselected item : " + item.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        //defult item
           btv.show(2, true);
    }



    @Override
    public void onButtomClicked(String Word, String meaning, String level)
    {
        Word word = new Word(Word, meaning, Integer.parseInt(level.trim()));
        viewModel.insetr(word);

    }

    @Override
    public void onButtomEditClicked(String Word, String meaning, String level)
    {
        Word word = new Word(Word, meaning, Integer.parseInt(level.trim()));
        word.setID(wordIdentefire);
        viewModel.update(word);
    }

    @Override
    public void getidfrompos(int id)
    {
        wordIdentefire=id;
    }

    @Override
    public void onRecyclerRefresh()
    {

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

    private  void checkUserStatus()
    {
        FirebaseUser user =firebaseAuth.getCurrentUser();
        if (user != null)
        {

        }else
        {
            startActivity(new Intent(MainActivity.this, SignIn_Activity.class));
            finish();
        }
    }

    @Override
    protected void onStart()
    {
        checkUserStatus();
        super.onStart();
    }
}