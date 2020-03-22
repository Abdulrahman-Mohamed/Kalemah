package com.Motawer.kalemah.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Motawer.kalemah.MainActivity;
import com.Motawer.kalemah.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignIn_Activity extends AppCompatActivity
{
    EditText emailEditText,passwordEditText;
    TextView signUp,forgetPassword;
    Button btn_login;
    String email,password;
    ProgressDialog progressDialog;
    SignInButton signInGoogle;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth firebaseAuth;
    int RC_SIGN_IN=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

     /* if (firebaseAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(SignIn_Activity.this, MainActivity.class));
            finish();
        }*/

        initViews();
        initButtons();
    }
    private void initButtons()
    {
        signInGoogle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (v.getId()) {
                    case R.id.sign_in_google:
                        signIn();
                        break;
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(SignIn_Activity.this,SignUp_Activity.class));

            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(SignIn_Activity.this,ForgetPassword_Activity.class));

            }
        });

        btn_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                email=emailEditText.getText().toString().trim();
                password=passwordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email))
                {
                    Toast.makeText(getApplicationContext(), "Enter your email", Toast.LENGTH_SHORT).show();
                    emailEditText.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password))
                {
                    Toast.makeText(getApplicationContext(), "Enter your password", Toast.LENGTH_SHORT).show();
                    passwordEditText.requestFocus();
                    return;
                }

                progressDialog = new ProgressDialog(SignIn_Activity.this);
                progressDialog.setMessage("Wait ...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();

                firebaseAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful())
                                {
                                    startActivity(new Intent(SignIn_Activity.this,MainActivity.class));
                                    //  finish();
                                }else
                                {
                                    Toast.makeText(SignIn_Activity.this, "Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                }

                            }
                        });
            }
        });
    }

    private void initViews()
    {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signUp = findViewById(R.id.signUp);
        forgetPassword = findViewById(R.id.forgetPassword);
        btn_login = findViewById(R.id.btn_login);
        firebaseAuth=FirebaseAuth.getInstance();
        signInGoogle =findViewById(R.id.sign_in_google);
    }

    private void signIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN)
        {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask)
    {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Intent intent=new Intent(SignIn_Activity.this,MainActivity.class);
            startActivity(intent);
        } catch (ApiException e)
        {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }
}
