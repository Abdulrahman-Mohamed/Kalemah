package com.Motawer.kalemah.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.Motawer.kalemah.Auth.SignIn_Activity;
import com.Motawer.kalemah.Auth.UserModel;
import com.Motawer.kalemah.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class profile_frag extends Fragment
{
     View view;
     ImageView circleImageView;
     TextView textName,textEmail;
     StorageReference storageReference;
     GoogleSignInClient mGoogleSignInClient;
     Uri picture;
     FirebaseUser user;
     FirebaseAuth firebaseAuth;
     FirebaseDatabase firebaseDatabase;
     DatabaseReference databaseReference;
     String userID,username,email;
     Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view= inflater.inflate(R.layout.frag_profile,container,false);
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {

        super.onViewCreated(view, savedInstanceState);

        initViews();
        initGoogle();
        initButtons();

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);
    }

    private void initButtons()
    {


        circleImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                        .setAspectRatio(1,1)
                        .start(getActivity());
            }
        });
    }

    private void initGoogle()
    {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null)
            if (firebaseAuth.getCurrentUser().getEmail().equals(acct.getEmail()))

                googleSignIn();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                if (dataSnapshot.exists())

                    showData(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void googleSignIn()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null)
        {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            Uri personPhoto = acct.getPhotoUrl();

            textName.setText(personName);
            textEmail.setText(personEmail);
            Picasso.get()
                    .load(String.valueOf(personPhoto))
                    .into(circleImageView);
        }
    }

    private void showData(DataSnapshot dataSnapshot)
    {
            UserModel userModel = dataSnapshot.child(userID).getValue(UserModel.class);

            if (userModel != null)
            {
                username= userModel.getUsername();
                email =userModel.getEmail();
                textName.setText(username);
                textEmail.setText(email);

                String photo = dataSnapshot.child(userID).child("photo").getValue(String.class);
                    Picasso.get()
                            .load(String.valueOf(photo))
                            .into(circleImageView);
            }
    }

    private void initViews()
    {
        circleImageView =view.findViewById(R.id.circleImageView);
        textName =view.findViewById(R.id.textName);
        textEmail =view.findViewById(R.id.textEmail);
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
        storageReference= getInstance().getReference();
        userID=user.getUid();
        toolbar = view.findViewById(R.id.setting_toolbar);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == Activity.RESULT_OK)
            {
                if (result != null)
                {
                    picture = result.getUri();

                    Picasso.get()
                            .load(picture)
                            .into(circleImageView);
                    uploadImage(picture);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImage(Uri picture)
    {
        UploadTask uploadTask;
        storageReference = FirebaseStorage.getInstance().getReference().child("images/" + picture.getLastPathSegment());
        uploadTask = storageReference.putFile(picture);

        Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
        {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
            {
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>()
        {
            @Override
            public void onComplete(@NonNull Task<Uri> task)
            {
                Uri image = task.getResult();
                String pictureUrl = image.toString();

                saveToDatabase(pictureUrl);
            }
        });
    }

    private void saveToDatabase(String pictureUrl)
    {
        user =firebaseAuth.getCurrentUser();
        String uid= user.getUid();
        databaseReference.child("User").child(uid).child("photo").setValue(String.valueOf(pictureUrl));

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        inflater.inflate(R.menu.setting, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id =item.getItemId();
        if (id== R.id.log_out)
        {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getActivity(), "LogOut successful..", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(),SignIn_Activity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
