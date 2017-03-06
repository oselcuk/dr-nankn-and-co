package com.DrNankn.cleanwater.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.DrNankn.cleanwater.Models.User;
import com.DrNankn.cleanwater.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText mPasswordView;
    private EditText mPasswordConfirmationView;
    private EditText mEmailView;
    private EditText mEmailConfirmationView;
    private Spinner mUserType;
    private View mRegisterGroup;
    private View mProgressView;
    private View mLoginFormView;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    private int mShortAnimTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up firebase
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("FIREBASE", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d("FIREBASE", "onAuthStateChanged:signed_out");
                }
            }
        };
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mShortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    login();
                    return true;
                }
                return false;
            }
        });

        mPasswordConfirmationView = (EditText) findViewById(R.id.confirm_password);
        new ConfirmationWatcher(mPasswordView, mPasswordConfirmationView, "Passwords");

        CheckBox mShowPassword = (CheckBox) findViewById(R.id.show_password);
        mShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int t = isChecked
                        ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                mPasswordView.setInputType(t);
                mPasswordConfirmationView.setInputType(t);
            }
        });

        mRegisterGroup = findViewById(R.id.register_group);

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        final Button mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRegisterGroup.getVisibility() != View.VISIBLE) {
                    animateViewVisibility(mRegisterGroup, true);
                } else {
                    createAccount();
                }
            }
        });
        mRegisterButton.setImeActionLabel("Register", KeyEvent.KEYCODE_ENTER);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.progress_group);

        mEmailView = (EditText) findViewById(R.id.email);
        mEmailConfirmationView = (EditText) findViewById(R.id.confirm_email);
        mUserType = (Spinner) findViewById(R.id.userType);

        ArrayAdapter<String> adapter2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item, new ArrayList<>(Arrays.asList(User.Role.values())));
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mUserType.setAdapter(adapter2);

        new ConfirmationWatcher(mEmailView, mEmailConfirmationView, "Email addresses");

        mEmailConfirmationView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.register || id == EditorInfo.IME_NULL) {
                    createAccount();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * Creates an account for the user in the system and stores necessary information for the user like
     * email, password and role. The method also checks that the password and email are valid entries.
     */
    private void createAccount() {
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();
        final User.Role role = (User.Role) mUserType.getSelectedItem();


        if (!validatePassword() || !validateEmail()
                || !email.equals(mEmailConfirmationView.getText().toString())
                || !password.equals(mPasswordConfirmationView.getText().toString())) {
            return;
        }
        animateViewVisibility(mLoginFormView, false);
        animateViewVisibility(mRegisterGroup, false);
        animateViewVisibility(mProgressView, true);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("FIREBASE", "createUserWithEmail:onComplete:" + task.isSuccessful());
                        animateViewVisibility(mProgressView, false);
                        animateViewVisibility(mLoginFormView, true);
                        if (task.isSuccessful()) {
                            User user = new User(email, role);
                            mDatabase.child("users").push().setValue(user);
                            loginSuccessful(user, true);
                        } else {
                            mEmailView.setError("Couldn't register");
                            mEmailView.requestFocus();
                            animateViewVisibility(mRegisterGroup, true);
                        }
                    }
                });
    }

    /**
     * Attempts to log the user into the system. If the user enters a valid email adress and password then control is
     * passed to loginSuccessful, otherwise the method returns.
     */
    private void login() {
        if (mEmailView.getText().length() == 0) {
            loginSuccessful(new User("admin", User.Role.Administrator), false);
            return;
        }
        final String password = mPasswordView.getText().toString();
        final User user = new User(mEmailView.getText().toString());
        if (!validatePassword() || !validateEmail()) {
            return;
        }
        animateViewVisibility(mLoginFormView, false);
        animateViewVisibility(mRegisterGroup, false);
        animateViewVisibility(mProgressView, true);
        mAuth.signInWithEmailAndPassword(user.email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("FIREBASE", "signInWithEmail:onComplete:" + task.isSuccessful());
                        animateViewVisibility(mProgressView, false);
                        animateViewVisibility(mLoginFormView, true);
                        if (task.isSuccessful()) {
                            loginSuccessful(user, false);
                        } else {
                            mEmailView.setError("Couldn't login");
                            mEmailView.requestFocus();
                        }
                    }
                });
    }

    /**
     * Logs the specified user into the system
     *
     * @param user The user being logged into the system
     * @param newUser A boolean that indicates whether the user is a new user
     */
    private void loginSuccessful(User user, boolean newUser) {
        mPasswordView.getText().clear();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("USER", user);
        if (newUser) {
            intent.putExtra("NEW_USER", true);
            mPasswordConfirmationView.getText().clear();
            mEmailConfirmationView.getText().clear();
            mRegisterGroup.setVisibility(View.GONE);
        }
        startActivity(intent);
    }

    /**
     * Checks to see if the password is a valid password
     *
     * @return true if password is valid
     */
    private boolean validatePassword() {
        String password = mPasswordView.getText().toString();
        boolean valid = password.length() > 3;
        if (!valid) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            mPasswordView.requestFocus();
        }
        return valid;
    }

    /**
     * Checks to see if the email is a valid email address
     *
     * @return true is email is valid
     */
    private boolean validateEmail() {
        String email = mEmailView.getText().toString();
        boolean valid = Pattern.matches("^.+@.+\\..+", email);
        if (!valid) {
            mEmailView.setError("Invalid email address");
            mEmailView.requestFocus();
        }
        return valid;
    }

    /**
     * Animates the view visibility for the login screen
     *
     * @param view The view for the page
     * @param show A boolean indicating whether the page should be displayed
     */
    private void animateViewVisibility(final View view, final boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
        view.animate().setDuration(mShortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    private class ConfirmationWatcher implements TextWatcher {

        private EditText mOriginal;
        private EditText mConfirmation;
        private CharSequence mTypes;

        ConfirmationWatcher(EditText original, EditText confirmation, CharSequence types) {
            mOriginal = original;
            mConfirmation = confirmation;
            mTypes = types;
            original.addTextChangedListener(this);
            confirmation.addTextChangedListener(this);
        }

        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!mConfirmation.getText().toString().equals(mOriginal.getText().toString())) {
                mConfirmation.setError(mTypes + " must match");
            } else {
                mConfirmation.setError(null);
            }
        }

        @Override public void afterTextChanged(Editable s) { }
    }
}

