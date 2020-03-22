package com.Motawer.kalemah.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.Motawer.kalemah.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

public class profile_frag extends Fragment
{
    View view;
    Button logout;
    ImageView imageView;
    TextView name,email,id;
    GoogleSignInClient mGoogleSignInClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.frag_profile,container,false);
        return  view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);
//
        logout=view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.logout:
                        signOut();
                        break;
                }
            }
        });

        imageView =view.findViewById(R.id.imageView);
        name =view.findViewById(R.id.textName);
        email =view.findViewById(R.id.textEmail);
        id =view.findViewById(R.id.textId);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(requireContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            name.setText(personName);
            email.setText(personEmail);
            id.setText(personId);
            Picasso.get()
                    .load(String.valueOf(personPhoto))
                    .into(imageView);

            //Glide.with(this).load(String.valueOf(personPhoto)).into(imageView);
        }
    }
    private void signOut()
    {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        startActivity(new Intent(requireContext(), SignIn_Activity.class));
                        Toast.makeText(requireContext(), "Sign out successfully", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
