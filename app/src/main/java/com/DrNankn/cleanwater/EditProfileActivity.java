package com.DrNankn.cleanwater;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Created by Nikhil on 20/2/2017.
 */

public class EditProfileActivity extends AppCompatActivity {

    private User mActiveUser;
    private EditText mEmailView;
    private EditText mAddressView;
    private EditText mNameView;
//    private Spinner mUserType;
    private int mShortAnimTime;
    private View mEditView;
    private View mProgressView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mActiveUser = getIntent().getParcelableExtra("USER");
        mEmailView = (EditText) findViewById(R.id.edit_email);
        mAddressView = (EditText) findViewById(R.id.edit_address);
        mNameView = (EditText) findViewById(R.id.edit_name);
//        mUserType = (Spinner) findViewById(R.id.edit_userType);
        mShortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mEditView = (View) findViewById(R.id.activity_edit_profile);
        mProgressView = findViewById(R.id.progress_group);


//        ArrayAdapter<String> adapter2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item, new ArrayList<>(Arrays.asList(User.Role.values())));
//        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mUserType.setAdapter(adapter2);
//        mUserType.setSelection(mActiveUser.role.ordinal());
        mAddressView.setText(mActiveUser.address);
        mNameView.setText(mActiveUser.name);
        mEmailView.setText(mActiveUser.email);

        final Button mEditButton = (Button) findViewById(R.id.edit_button);
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNewFields();
            }
            }
        );

        final Button mCancelButton = (Button) findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditSuccessful(mActiveUser);
            }
        });
    }

    /**
     * Edits the current User's attributes
     */
    private void editNewFields() {
        if (!validateEmail()) {
            return;
        }
        animateViewVisibility(mEditView, false);
        animateViewVisibility(mProgressView, true);
        mActiveUser.address = mAddressView.getText().toString();
        mActiveUser.name = mNameView.getText().toString();
        mActiveUser.email = mEmailView.getText().toString();
//        mActiveUser.role = (User.Role) mUserType.getSelectedItem();
        animateViewVisibility(mProgressView, false);
        animateViewVisibility(mEditView, true);
        EditSuccessful(mActiveUser);
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
     * Animates the view visibility for the Edit Profile screen
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

    /**
     * Completes the Editing process for the user
     *
     * @param user The user being edited
     */
    private void EditSuccessful(User user) {
        Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
        intent.putExtra("USER", user);
        startActivity(intent);
    }
}
