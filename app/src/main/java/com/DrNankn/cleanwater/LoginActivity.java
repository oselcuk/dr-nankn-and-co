package com.DrNankn.cleanwater;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static Map<String, String> savedCredentials = new HashMap<>();
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mUserNameView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private EditText mEmailView;
    private EditText mEmailConfirmationView;
    private View mRegisterGroup;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        savedCredentials.put("username", "password");

        mUserNameView = (EditText) findViewById(R.id.username);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mConfirmPasswordView = (EditText) findViewById(R.id.confirm_password);

        new ConfirmationWatcher(mPasswordView, mConfirmPasswordView, "Passwords");

        CheckBox mShowPassword = (CheckBox) findViewById(R.id.show_password);
        mShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int t = isChecked
                        ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                mPasswordView.setInputType(t);
                mConfirmPasswordView.setInputType(t);
            }
        });

        mRegisterGroup = findViewById(R.id.register_group);

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mRegisterGroup.setVisibility(View.GONE);
                attemptLogin();
            }
        });

        Button mCancelLoginButton = (Button) findViewById(R.id.cancel_login_button);
        mCancelLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAuthTask != null) {
                    mAuthTask.cancel(true);
                }
            }
        });

        final Button mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRegisterGroup.getVisibility() != View.VISIBLE) {
                    mRegisterGroup.setVisibility(View.VISIBLE);
                } else {
                    attemptLogin();
                }
            }
        });
        mRegisterButton.setImeActionLabel("Register", KeyEvent.KEYCODE_ENTER);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.progress_group);

        mEmailView = (EditText) findViewById(R.id.email);
        mEmailConfirmationView = (EditText) findViewById(R.id.confirm_email);

        new ConfirmationWatcher(mEmailView, mEmailConfirmationView, "Email addresses");

        mEmailConfirmationView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.register || id == EditorInfo.IME_NULL) {
                    mRegisterButton.callOnClick();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String email = null;

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (!isUsernameValid(username)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        }

        if (mRegisterGroup.getVisibility() == View.VISIBLE) {
            email = mEmailView.getText().toString();
            if (!password.equals(mConfirmPasswordView.getText().toString())) {
                focusView = mConfirmPasswordView;
                cancel = true;
            }
            if (!Pattern.matches("^.+@.+\\..+", email)) {
                mEmailView.setError("Invalid email address");
                focusView = mEmailView;
                cancel = true;
            } else if (!email.equals(mEmailConfirmationView.getText().toString())) {
                focusView = mEmailConfirmationView;
                cancel = true;
            }
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            showProgress(true);

            mAuthTask = new UserLoginTask(username, password, email);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
    }

    private boolean isUsernameValid(String username) {
        //TODO: Replace this with your own logic
        return username.length() > 3;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        private final String mEmail;

        UserLoginTask(String username, String password, String email) {
            mUsername = username;
            mPassword = password;
            mEmail = email;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            if (mEmail == null)
                return mPassword.equals(savedCredentials.get(mUsername));

            // TODO: actually register new user, email confirmation and all that
            savedCredentials.put(mUsername, mPassword);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            onCancelled();

            if (success) {
                mPasswordView.getText().clear();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("USERNAME", mUsername);
                intent.putExtra("NEW_USER", mRegisterGroup.getVisibility() == View.VISIBLE);
                mConfirmPasswordView.getText().clear();
                mEmailView.getText().clear();
                mEmailConfirmationView.getText().clear();
                mRegisterGroup.setVisibility(View.GONE);
                startActivity(intent);
            } else {
                mUserNameView.setError(getString(R.string.error_incorrect_credentials));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
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

