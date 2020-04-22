package com.Motawer.kalemah.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.Motawer.kalemah.Auth.SignIn_Activity;
import com.Motawer.kalemah.Auth.SignUp_Activity;
import com.Motawer.kalemah.Auth.UserModel;
import com.Motawer.kalemah.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class profile_frag extends Fragment
{
    View view;
    private CircleImageView circleImageView;
    private TextView textName,textEmail;
    private Button logout;
    private StorageReference storageReference;
    private GoogleSignInClient mGoogleSignInClient;
    private Uri picture;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String userID,username,email;

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
        if (user!= null)
        {
            String name =user.getDisplayName();
            String email=user.getEmail();
            String PhotoURL =user.getPhotoUrl().toString();

            Picasso.get().load(PhotoURL).into(circleImageView);
            textName.setText(name);
            textEmail.setText(email);
        }


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

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
                if (firebaseAuth.getCurrentUser().getEmail().equals(acct.getEmail()))
                {
                    mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            startActivity(new Intent(getActivity(),SignIn_Activity.class));
                            Toast.makeText(getActivity(), "Sign Out success", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else
                {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getActivity(),SignIn_Activity.class));
                }
            }
        });

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
        logout=view.findViewById(R.id.logout);
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
        storageReference= getInstance().getReference();
        userID=user.getUid();

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
}
