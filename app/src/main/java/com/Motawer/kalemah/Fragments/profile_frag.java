package com.Motawer.kalemah.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.viewpager2.widget.ViewPager2;

import com.Motawer.kalemah.Adapter.examFragmentAdapter;
import com.Motawer.kalemah.Auth.SignIn_Activity;
import com.Motawer.kalemah.Auth.UserModel;
import com.Motawer.kalemah.R;
import com.Motawer.kalemah.RoomDataBase.Word;
import com.Motawer.kalemah.ViewModel.WordsViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class profile_frag extends Fragment {
    private WordsViewModel viewModel;
    imageChanged imageChanged;
    View view;
    ImageView circleImageView;
    TextView textName, textEmail, wordsCount, points, levels;
    StorageReference storageReference;
    GoogleSignInClient mGoogleSignInClient;
    Uri picture;
    FirebaseUser user;
    SharedPreferences.Editor editor;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String userID, username, email;
    final String KEY = "MY_USER_INFO";
    final String USERPoints = "USER_Points0";
    final String USERLevels = "USER_Levels0";
    boolean connected = false;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    List<Boolean> levelList = new ArrayList<>();
    List<Integer> levelPoint = new ArrayList<>();
    int WordsCounter, pointCounter = 0;
    String photo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        view = inflater.inflate(R.layout.frag_profile, container, false);
        initViews();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        checkInternet();
        if (!connected)
            LoadShared();

        initGoogle();
        loadImageFromStorage("data/user/0/com.Motawer.kalemah/app_imageDir");

        initButtons();
        getWordsCount();
        BackThread backThread = new BackThread();
        backThread.start();
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);
    }

    private void loadImageFromStorage(String s) {
        Bitmap b = null;
        try {
            String uid = firebaseAuth.getUid();
            File f = new File(s, uid + ".jpg");
            b = BitmapFactory.decodeStream(new FileInputStream(f));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (b != null) {
            circleImageView.setImageBitmap(b);
        } else {
            Picasso.get()
                    .load(photo)
                    .into(circleImageView);
        }
    }

    private void getWordsCount() {
        setViewModel();
        viewModel.getAllWords().observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                WordsCounter = 0;
                for (int i = 0; i < words.size(); i++)
                    WordsCounter++;
                setViews();

            }
        });

    }

    private void checkInternet() {
        Context context = getActivity();

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }


    private void LoadShared() {
        Context context = getActivity();

        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(USERPoints)) {
            Log.e("sharedPrefrences", "true");
        } else {
            Log.e("sharedPrefrences", "false");

        }
        int pointsshared = sharedPreferences.getInt(USERPoints, 0);
        int levelsshared = sharedPreferences.getInt(USERLevels, 0);
        levels.setText(String.valueOf(levelsshared));
        points.setText(String.valueOf(pointsshared));
    }


//    private void loadData() {
//        final String KEY="FAVORIT_WORDS";
//        final String WORD_FAVORIT="MY_FAV_WORDS";
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(KEY, Context.MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString(WORD_FAVORIT, null);
//        Type type = new TypeToken<ArrayList<Word>>() {}.getType();
//        wordArrayList = gson.fromJson(json, type);
//        if (wordArrayList!=null)
//        recyclerAdapter.setWordList(wordArrayList);
//
//        if (wordArrayList == null) {
//            wordArrayList = new ArrayList<>();
//        }
//    }

    private void setViews() {
        wordsCount.setText(String.valueOf(WordsCounter));
    }

    private void initButtons() {


        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                        .setAspectRatio(1, 1)
                        .start(getActivity());
            }
        });
    }

    private void initGoogle() {

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null)
            if (firebaseAuth.getCurrentUser().getEmail().equals(acct.getEmail()))

                googleSignIn();

        databaseReference.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())

                    showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void SaveData(int pointCounter, int size) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(KEY, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        if (levelList.size() != 0)
            editor.putInt(USERLevels, size);
        Log.e("sharedPrefrences2", String.valueOf(size));
        if (pointCounter != 0)
            editor.putInt(USERPoints, pointCounter);
        Log.e("sharedPrefrences1", String.valueOf(pointCounter));

        editor.apply();
    }

    private void googleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            // Uri personPhoto = acct.getPhotoUrl();

            textName.setText(personName);
            textEmail.setText(personEmail);
//            Picasso.get()
//                    .load(String.valueOf(personPhoto))
//                    .into(circleImageView);
        }

    }

    private void showData(DataSnapshot dataSnapshot) {
        UserModel userModel = dataSnapshot.child(userID).getValue(UserModel.class);

        if (userModel != null) {
            username = userModel.getUsername();
            email = userModel.getEmail();
            textName.setText(username);
            textEmail.setText(email);

            photo = dataSnapshot.child(userID).child("photo").getValue(String.class);
//            Picasso.get()
//                    .load(photo)
//                    .into(circleImageView);
        }

    }

    private void initViews() {
        circleImageView = view.findViewById(R.id.circleImageView);
        textName = view.findViewById(R.id.textName);
        textEmail = view.findViewById(R.id.textEmail);
        //  recyclerView=view.findViewById(R.id.prefered_Words);
        tabLayout = view.findViewById(R.id.chart_tabLayout);
        viewPager2 = view.findViewById(R.id.charts_viewPager);
        wordsCount = view.findViewById(R.id.words_Count);
        levels = view.findViewById(R.id.levels_count);
        points = view.findViewById(R.id.points);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);
        storageReference = getInstance().getReference();
        userID = user.getUid();
        toolbar = view.findViewById(R.id.setting_toolbar);
        InitializeViewPagerWIthToolbar();
//InitializeRecycler();
    }

    private void InitializeViewPagerWIthToolbar() {
        examFragmentAdapter adapter = new examFragmentAdapter(requireActivity());
        viewPager2.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Tests");
                        break;
                    case 1:
                        tab.setText("Words");
                        break;
//                    case 2:
//                        tab.setText("S/F Rate");
//                        break;
                }
            }
        }).attach();
    }


    private void setViewModel() {
        viewModel = new ViewModelProvider((ViewModelStoreOwner) getActivity()).get(WordsViewModel.class);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == Activity.RESULT_OK) {
                if (result != null) {
                    picture = result.getUri();
                    if (picture != null) {
                        imageChanged.imageChangedHappen(String.valueOf(picture));
                        Picasso.get()
                                .load(picture)
                                .into(circleImageView);
                        uploadImage(picture);
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImage(Uri picture) {
        UploadTask uploadTask;
        storageReference = FirebaseStorage.getInstance().getReference().child("images/" + picture.getLastPathSegment());
        uploadTask = storageReference.putFile(picture);

        Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri image = task.getResult();
                String pictureUrl = image.toString();

                saveToDatabase(pictureUrl);
            }
        });
    }

    private void saveToDatabase(String pictureUrl) {
        user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();
        databaseReference.child("User").child(uid).child("photo").setValue(String.valueOf(pictureUrl));

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.setting, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.log_out) {
            FirebaseAuth.getInstance().signOut();
            viewModel.deleteAllWords();
            Toast.makeText(getActivity(), "Logout successful..", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), SignIn_Activity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    class BackThread extends Thread {
        @Override
        public void run() {

            databaseReference.child("UserLevels").child(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists())
                        for (DataSnapshot snapshot : dataSnapshot.getChildren())
                            levelList.add(snapshot.getValue(Boolean.class));
                    levels.setText(String.valueOf(levelList.size()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            databaseReference.child("UserPoints").child(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists())
                        for (DataSnapshot snapshot : dataSnapshot.getChildren())
                            levelPoint.add(snapshot.getValue(Integer.class));
                    for (int i = 0; i < levelPoint.size(); i++) {
                        if (levelPoint.get(i) != 0) {
                            pointCounter = pointCounter + levelPoint.get(i);
                        } else if (levelPoint.get(i) == 0) {
                            points.setText(String.valueOf(pointCounter));
                            return;
                        }

                    }
                    points.setText(String.valueOf(pointCounter));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
            SaveData(pointCounter, levelList.size());
        }


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            imageChanged = (imageChanged) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement bottom sheet");
        }
    }
}

