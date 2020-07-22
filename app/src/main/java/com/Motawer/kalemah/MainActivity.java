package com.Motawer.kalemah;

import android.animation.Animator;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.Motawer.kalemah.Auth.SignIn_Activity;
import com.Motawer.kalemah.Auth.UserModel;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements AddWord_Dialog.BottomSheetListner, AddWord_DialogEdit.AddWordSheetEditeListner
        , com.Motawer.kalemah.Fragments.words_frag.GetID, AddWord_DialogEdit.refreshrecycler {

    // Fragment selectedFragment = null;
    Bitmap profileBitmap;
    private WordsViewModel viewModel;
    int wordIdentefire;
    ImageButton ExamsButton;
    FloatingActionButton WordsFloatingActionButton;
    CircleImageView ProfileImageView;
    // BottomAppBar bottomAppBar;
    FirebaseAuth firebaseAuth;
    int place_word;
    int place_exam;
    String Key = "profileIMage", Tag = "myImage";
    String photo;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
//        Window window=getWindow();
//        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                , WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // bottomAppBar = findViewById(R.id.bottomAppBar);


        Initialize();
        // getPhoto();
        loadImageFromStorage("data/user/0/com.Motawer.kalemah/app_imageDir");

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frag_container, new words_frag()) .addToBackStack(null).commit();

        WordsFloatingActionButton.setImageResource(R.drawable.ic_plus_wese5_new);
        place_word = 1;
//        if (photo.equals("") || photo == null)
            getimage();


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
                ProfileImageView.setBorderWidth(0);

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

                    ProfileImageView.setBorderWidth(0);
//                YoYo.with(Techniques.RotateIn)
//                        .duration(700)
//                        .playOn(ExamsButton);

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frag_container, new exams_frag(), "exam").addToBackStack(null).commit();
                      //getSupportFragmentManager().beginTransaction().detach(new words_frag()).detach(new profile_frag()).commit();

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

                if (place_word == 0) {

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
                                .replace(R.id.frag_container, new words_frag(), "word").addToBackStack(null).commit();
                        //getSupportFragmentManager().beginTransaction().detach(new words_frag()).detach(new profile_frag()).commit();




                } else if (place_word == 1) {
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
                            .replace(R.id.frag_container, new profile_frag(), "profile").addToBackStack(null).commit();
                    //getSupportFragmentManager().beginTransaction().detach(new words_frag()).detach(new profile_frag()).commit();


            }
        });

        viewModel = new

                ViewModelProvider(this).

                get(WordsViewModel.class);


    }



    private void loadImageFromStorage(String path) {

        try {
            File f = new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            if (b != null){
                ProfileImageView.setImageBitmap(b);}
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

//    private void getPhoto()
//    {
//        SharedPreferences sharedPref = getSharedPreferences(Key,Context.MODE_PRIVATE);
//        photo=sharedPref.getString(Tag,"");
//        if (!photo.equals("")||photo!=null){
//            Picasso.get()
//                    .load(photo)
//                    .into(ProfileImageView);
//            Log.e("Photo_shared", photo);}
//
//    }

    private void getimage() {

        databaseReference.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    UserModel userModel = dataSnapshot.child(firebaseAuth.getUid()).getValue(UserModel.class);
                    if (userModel.getImage() != null || !dataSnapshot.child(firebaseAuth.getUid()).child("photo").getValue(String.class).equals("")) {
                        photo = dataSnapshot.child(firebaseAuth.getUid()).child("photo").getValue(String.class);
//                        Picasso.get()
//                                .load(photo)
//                                .into(ProfileImageView);
                        savePhoto();
                    } else {
                        googleAcountPhoto();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });
    }

    private void savePhoto() {

        BackThreadImage backThreadImage = new BackThreadImage();
        backThreadImage.execute(photo);

    }


    private void openDialog() {
        AddWord_Dialog addWord_dialog = new AddWord_Dialog();
        addWord_dialog.show(getSupportFragmentManager(), "WordDialog");


    }

    private void Initialize() {
        ExamsButton = findViewById(R.id.exams_icon);
        ProfileImageView = findViewById(R.id.profile_image);
        WordsFloatingActionButton = findViewById(R.id.words_fab);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    public void googleAcountPhoto() {
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

    class BackThreadImage extends AsyncTask<String,Void,Void> {


//            SharedPreferences sharedPref = getSharedPreferences(Key, Context.MODE_PRIVATE);
//            Editor editor = sharedPref.edit();
//            if (photo != null || !photo.equals("")) {
//                editor.putString(Tag, photo);
//                Log.e("Photo", photo);
//                editor.apply();


        @Override
        protected Void doInBackground(String... strings) {
            profileBitmap = loadBitmap(strings[0]);
            saveToInternalStorage(profileBitmap);
            return null;
        }
    }
    public Bitmap loadBitmap(String url) {
        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 8192);
            bm = BitmapFactory.decodeStream(bis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bm;
    }


    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

}
