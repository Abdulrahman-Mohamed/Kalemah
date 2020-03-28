package com.Motawer.kalemah.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Motawer.kalemah.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword_Activity extends AppCompatActivity
{
    EditText emailText;
    Button btn_resetPassword;
    TextView signUp;
    FirebaseAuth firebaseAuth;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_);

        initViews();
        initButtons();

    }

    private void initButtons()
    {
        signUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(ForgetPassword_Activity.this,SignUp_Activity.class));

            }
        });
        btn_resetPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                email=emailText.getText().toString().trim();

                if (email.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Enter your email", Toast.LENGTH_SHORT).show();
                    emailText.requestFocus();
                    return;

                }else
                {
                    firebaseAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(getApplicationContext(), "password reset email sent!", Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(new Intent(ForgetPassword_Activity.this,SignIn_Activity.class));
                                    }else
                                    {
                                        Toast.makeText(getApplicationContext(), "Error in sending password reset email", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
            }
        });
    }

    private void initViews()
    {

        emailText= findViewById(R.id.emailText);
        btn_resetPassword = findViewById(R.id.btn_resetPassword);
        signUp = findViewById(R.id.signUp);
        firebaseAuth=FirebaseAuth.getInstance();

    }
}
