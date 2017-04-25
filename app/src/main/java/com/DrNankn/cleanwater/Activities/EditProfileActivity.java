/* 
* This class represents the activity of editing a user
* profile
*/
package com.DrNankn.cleanwater.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.DrNankn.cleanwater.Models.User;
import com.DrNankn.cleanwater.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditProfileActivity extends AppCompatActivity {

    private User mActiveUser;
    private EditText mAddressView;
    private EditText mNameView;
    private int mShortAnimTime;
    private View mEditView;
    private View mProgressView;
    private ImageButton mProfileImage;

    private StorageReference mStorage;

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
        mEditButton.setOnClickListener(v -> editNewFields());

        final Button mCancelButton = (Button) findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(v -> EditSuccessful(mActiveUser));

        mProfileImage = (ImageButton) findViewById(R.id.profile_picture_button);
        mStorage = FirebaseStorage.getInstance()
                .getReference("profilePictures/" + mActiveUser.getEmail());

        mStorage.getBytes(2<<20/*1mb*/)
                .addOnSuccessListener(bytes -> {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    mProfileImage.setImageBitmap(bmp);
                }).addOnFailureListener(ex -> {
            ex.toString();

        });
        mProfileImage.setOnClickListener(v -> {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto , 0);
        });
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

    private void changePicture(Bitmap bmp) {
        int mx = Math.max(bmp.getWidth(), bmp.getHeight());
        if (mx > 256) {
            double scale = 256d / mx;
            Bitmap old = bmp;
            bmp = Bitmap.createScaledBitmap(bmp,
                    (int)(bmp.getWidth() * scale), (int)(bmp.getHeight() * scale), true);
            old.recycle();
        }
        mProfileImage.setImageBitmap(bmp);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        mStorage.putBytes(data);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch(requestCode) {
            case 0:
                try {
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream stream = getContentResolver().openInputStream(selectedImage);
                    Bitmap bmp = BitmapFactory.decodeStream(stream);
                    changePicture(bmp);
                } catch (FileNotFoundException _) {
                    // not possible
                }
                break;
        }
    }
}
