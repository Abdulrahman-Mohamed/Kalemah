package com.Motawer.kalemah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.Motawer.kalemah.Fragments.exams_frag;
import com.Motawer.kalemah.Fragments.profile_frag;
import com.Motawer.kalemah.Fragments.words_frag;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;


public class MainActivity extends AppCompatActivity {

    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MeowBottomNavigation btv = findViewById(R.id.botnav);
        btv.add(new MeowBottomNavigation.Model(3, R.drawable.a_mark));
        btv.add(new MeowBottomNavigation.Model(2, R.drawable.ic_white_feather));
        btv.add(new MeowBottomNavigation.Model(1, R.drawable.ic_person_black_24dp));

        //click item but not show
        btv.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                switch (item.getId()) {
                    case 1:
                        selectedFragment = new profile_frag();
                        break;
                    case 2:
                        selectedFragment = new words_frag();
                        break;
                    case 3:
                        selectedFragment = new exams_frag();
                        break;
                }
//                Toast.makeText(MainActivity.this, "clicked item : " + item.getId(), Toast.LENGTH_SHORT).show();
            }
        });

        //show but without click
        btv.setOnShowListener(new MeowBottomNavigation.ShowListener()
        {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item)
            {
                switch (item.getId())
                {
                    case 1:
                        selectedFragment = new profile_frag();
                        break;
                    case 2:
                        selectedFragment = new words_frag();
                        break;
                    case 3:
                        selectedFragment = new exams_frag();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, selectedFragment).commit();
            }
        });

        //re_click handler
        btv.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

                switch (item.getId()) {
                    case 1:
                        selectedFragment = new profile_frag();
                        break;
                    case 2:
                        selectedFragment = new words_frag();
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
}
