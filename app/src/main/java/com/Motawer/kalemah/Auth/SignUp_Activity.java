package com.Motawer.kalemah.Auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.Motawer.kalemah.MainActivity;
import com.Motawer.kalemah.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class SignUp_Activity extends AppCompatActivity
{
    EditText userNameEditText,emailEditText,passwordEditText,confirmPasswordEditText;
    Button btn_signUp;
    TextView signIn;
    String email,password,confirmPassword,username;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_);

        initViews();
        initButtons();
    }

    private void initButtons()
    {
        signIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(SignUp_Activity.this, SignIn_Activity.class));

            }
        });

        btn_signUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                username = userNameEditText.getText().toString().trim();
                email = emailEditText.getText().toString().trim();
                password = passwordEditText.getText().toString().trim();
                confirmPassword = confirmPasswordEditText.getText().toString().trim();


                if (TextUtils.isEmpty(username))
                {
                    Toast.makeText(getApplicationContext(), "Enter your name", Toast.LENGTH_SHORT).show();
                    userNameEditText.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(email))
                {
                    Toast.makeText(getApplicationContext(), "Enter your email", Toast.LENGTH_SHORT).show();
                    emailEditText.requestFocus();
                    return;
                }
                if (password.length() < 6)
                {
                    Toast.makeText(getApplicationContext(), "Password is too short", Toast.LENGTH_SHORT).show();
                    passwordEditText.requestFocus();
                    return;
                }
                if (!confirmPassword.equals(password))
                {
                    Toast.makeText(getApplicationContext(), "password is not matching", Toast.LENGTH_SHORT).show();
                    confirmPasswordEditText.requestFocus();
                    return;
                }

                progressDialog = new ProgressDialog(SignUp_Activity.this);
                progressDialog.setMessage("Wait ...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();

                createUser(username,email,password);
            }
        });
    }
    public void createUser(final String username, final String email, String password)
    {
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Sign in success, dismiss dialog and start register activity
                            progressDialog.dismiss();

                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            String uid =task.getResult().getUser().getUid();
                            UserModel userModel=new UserModel(username,email);

                            FirebaseDatabase database=FirebaseDatabase.getInstance();
                            DatabaseReference reference =database.getReference("User");
                            reference.child(uid).setValue(userModel);

                           Toast.makeText(SignUp_Activity.this, "Registered.../n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUp_Activity.this,MainActivity.class));
                            finish();

                        } else
                            {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(SignUp_Activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressDialog.dismiss();
                Toast.makeText(SignUp_Activity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void initViews()
    {
        userNameEditText = findViewById(R.id.userNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        btn_signUp = findViewById(R.id.btn_signUp);
        signIn = findViewById(R.id.signIn);
        firebaseAuth=FirebaseAuth.getInstance();

    }

}
