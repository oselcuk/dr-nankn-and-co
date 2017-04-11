/* 
* This class represents the activity of editing a user
* profile
*/
package com.DrNankn.cleanwater.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.DrNankn.cleanwater.Models.User;
import com.DrNankn.cleanwater.R;

public class EditProfileActivity extends AppCompatActivity {

    private User mActiveUser;
    private EditText mAddressView;
    private EditText mNameView;
    private int mShortAnimTime;
    private View mEditView;
    private View mProgressView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mActiveUser = getIntent().getParcelableExtra("USER");
        mAddressView = (EditText) findViewById(R.id.edit_address);
        mNameView = (EditText) findViewById(R.id.edit_name);
        mShortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mEditView = findViewById(R.id.activity_edit_profile);
        mProgressView = findViewById(R.id.progress_group);

        mAddressView.setText(mActiveUser.getAddress());
        mNameView.setText(mActiveUser.getName());

        final Button mEditButton = (Button) findViewById(R.id.edit_button);
        mEditButton.setOnClickListener(v -> editNewFields()
        );

        final Button mCancelButton = (Button) findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(v -> EditSuccessful(mActiveUser));
    }

    /**
     * Edits the current User's attributes
     */
    private void editNewFields() {
        animateViewVisibility(mEditView, false);
        animateViewVisibility(mProgressView, true);
        mActiveUser.setAddress(mAddressView.getText().toString());
        mActiveUser.setName(mNameView.getText().toString());
        animateViewVisibility(mProgressView, false);
        animateViewVisibility(mEditView, true);
        EditSuccessful(mActiveUser);
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
    private void EditSuccessful(@SuppressWarnings("TypeMayBeWeakened") User user) {
        // Since the parent of this activity expects the "USER" extra to be a User, we only accept
        //  a User object as argument to this class
        Intent result = new Intent();
        result.putExtra("USER", user);
        setResult(Activity.RESULT_OK, result);
        finish();
    }
}
