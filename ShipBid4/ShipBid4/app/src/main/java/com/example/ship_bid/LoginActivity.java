package com.example.ship_bid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class Login extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    EditText mEmailId,mPassword;
    Button btnLogIn;
    TextView btnCreate, forgotTextLink;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    SignInButton signBtn;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_login);

        mEmailId = findViewById (R.id.EmailIdLogin);
        mPassword = findViewById (R.id.PasswordLogin);
        fAuth = FirebaseAuth.getInstance ();
        progressBar = findViewById (R.id.progressBarLogin);
        btnLogIn = findViewById (R.id.LoginBtn);
        btnCreate = findViewById (R.id.CreateBtn);
        forgotTextLink = findViewById (R.id.forgotpassword);
        signBtn= findViewById(R.id.gsingin);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);

        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        btnLogIn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String email = mEmailId.getText ().toString ().trim ();
                String password = mPassword.getText ().toString ().trim ();

                if (TextUtils.isEmpty (email)) {
                    mEmailId.setError (" Email Required ");
                    return;
                }
                if (TextUtils.isEmpty (password)) {
                    mPassword.setError (" Password Required ");
                    return;
                }
                if (password.length () < 6) {
                    mPassword.setError (" Password must me less than 6 Characters  ");
                    return;
                }

                progressBar.setVisibility (View.VISIBLE);

                fAuth.signInWithEmailAndPassword (email, password).addOnCompleteListener (new OnCompleteListener<AuthResult> () {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful ()) {
                            Toast.makeText (Login.this, "Logged in Successfully ", Toast.LENGTH_SHORT).show ();
                            startActivity (new Intent (getApplicationContext (), MainActivity.class));
                        } else {
                            Toast.makeText (Login.this, "Error !" + task.getException ().getMessage (), Toast.LENGTH_SHORT).show ();
                        }
                    }
                });

            }
        });
        btnCreate.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startActivity (new Intent (getApplicationContext (), Registration.class));

            }

        });

        forgotTextLink.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText (v.getContext ());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder (v.getContext ());
                passwordResetDialog.setTitle ("Reset Password ?");
                passwordResetDialog.setMessage ("Enter Your Email To Received Reset Link");
                passwordResetDialog.setView (resetMail);

                AlertDialog.Builder yes = passwordResetDialog.setPositiveButton ("Yes", new OnClickListener () {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extract the email and send reset link
                        String mail = resetMail.getText ().toString ();
                        final Task<Void> voidTask = fAuth.sendPasswordResetEmail (mail).addOnSuccessListener (new OnSuccessListener<Void> () {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText (Login.this, "Reset Link Sent To Your Email", Toast.LENGTH_SHORT).show ();
                            }


                        });

                    }

                });
                passwordResetDialog.setNegativeButton ("No", new

                        OnClickListener () {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //close the dialog
                            }
                        });
                passwordResetDialog.create ().show ();


            }

            ;
        });

    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(Login.this,"SignIn Successfully",Toast.LENGTH_SHORT).show();
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(Login.this,"Signin failed",Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        fAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(Login.this,"Successfully",Toast.LENGTH_SHORT).show();
                            FirebaseUser user = fAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this,"Failed",Toast.LENGTH_SHORT).show();

                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Intent intent =new Intent(Login.this,MainActivity.class);
        startActivity(intent);
    }


    private void firebaseAuthWithGoogle(String idToken) {
    }
}
