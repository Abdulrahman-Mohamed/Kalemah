package com.Motawer.kalemah.Auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.Motawer.kalemah.MainActivity;
import com.Motawer.kalemah.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp_Activity extends AppCompatActivity
{
    EditText userNameEditText,emailEditText,passwordEditText,confirmPasswordEditText;
    Button btn_signUp;
    TextView signIn;
    String email,password,confirmPassword,userName;
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
                userName = userNameEditText.getText().toString().trim();
                email = emailEditText.getText().toString().trim();
                password = passwordEditText.getText().toString().trim();
                confirmPassword = confirmPasswordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(userName))
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

                firebaseAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if (!task.isSuccessful())
                                {
//                                    String id=firebaseAuth.getCurrentUser().getUid();
//                                    UserModel usermodel=new UserModel(userName,email);
                                    Toast.makeText(SignUp_Activity.this, "SignUp Unsuccessful", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                }
                                else
                                {
                                    startActivity(new Intent(SignUp_Activity.this, MainActivity.class));
                                    progressDialog.dismiss();
                                    finish();
                                }
                            }
                        });
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
