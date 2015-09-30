package scs2682.com.reviewsapp.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import scs2682.com.reviewsapp.ApplicationActivity;
import scs2682.com.reviewsapp.R;

/**
 * Created by carlospinto on 7/18/15.
 */
public class ComposeReview extends Fragment implements View.OnClickListener
{
    public static ComposeReview newInstance(String subject)
    {
        // stay as much as possible away from null
        subject = subject != null ? subject : "";

        // use this bundle as setArguments() so we can get it from within onActivityCreated()
        final Bundle bundle = new Bundle();
        bundle.putString(ApplicationActivity.SUBJECT_KEY, subject);

        final ComposeReview fragment = new ComposeReview();
        fragment.setArguments(bundle);
        return fragment;
    }

    //private static final int IMAGE_REQUEST_CODE = 101;
    public static final String NAME = ComposeReview.class.getSimpleName();

    private EditText composeReviewEditText;
    private Button postButton;
    private ImageButton cancelButton;
    private ImageButton addPhotoButton;
    private AlertDialog dialog;
    //private ImageView image;
    //private String imageUrl;

    public void onClick(View view)
    {
        if (postButton.equals(view))
        {
            String reviewText = composeReviewEditText.getText().toString();

            if (!TextUtils.isEmpty(reviewText))
            {
                Bundle arguments = getArguments();
                String subject = arguments.getString(ApplicationActivity.SUBJECT_KEY);

                ParseUser currentUser = ParseUser.getCurrentUser();
                String currentUsername = currentUser.getUsername();

                ParseObject review = new ParseObject("Review");
                review.put("subject", subject);
                review.put("text", reviewText);
                review.put("user", currentUsername);
                review.saveInBackground(new SaveCallback()
                {
                    @Override
                    public void done(ParseException e)
                    {
                        if (e == null)
                        {
                            // Save succeed.
                            Bundle arguments = getArguments();
                            String subject = arguments.getString(ApplicationActivity.SUBJECT_KEY);

                            ApplicationActivity aa = (ApplicationActivity) getActivity();
                            Toast.makeText(aa, "Review Posted!", Toast.LENGTH_LONG).show();
                            aa.showReviewsPage(ComposeReview.NAME, subject);
                        }
                        else
                        {
                            // Save didn't succeed.
                            // Create an error dialog
                            // Close any previous alert dialog
                            if (dialog != null && dialog.isShowing())
                            {
                                dialog.dismiss();
                            }

                            // create the dialog by using its Builder
                            dialog = new AlertDialog.Builder(getActivity())
                                    .setTitle("Error")
                                    .setMessage(e.getMessage())
                                    .setCancelable(true)
                                    .setIcon(R.drawable.ic_error_black_24dp)
                                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int which) {
                                            if (dialog != null && dialog.isShowing()) {
                                                dialog.cancel();
                                                dialog = null;
                                            }
                                        }
                                    })
                                    .create();
                            dialog.show();
                        }
                    }
                });
            }
            else
            {
                // Create an error dialog
                // Close any previous alert dialog
                if (dialog != null && dialog.isShowing())
                {
                    dialog.dismiss();
                }

                // create the dialog by using its Builder
                dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Empty Review")
                        .setMessage("Please type in your review.")
                        .setCancelable(true)
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.cancel();
                                    dialog = null;
                                }
                            }
                        })
                        .create();
                dialog.show();
            }
        }
        else if (cancelButton.equals(view))
        {
            Bundle arguments = getArguments();
            String subject = arguments.getString(ApplicationActivity.SUBJECT_KEY);
            ((ApplicationActivity) getActivity()).showReviewsPage(ComposeReview.NAME, subject);
        }
        /*else if (addPhotoButton.equals(view))
        {
            final Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, IMAGE_REQUEST_CODE);
        }*/
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.w(NAME, NAME + ".onCreateView()");
        return inflater.inflate(R.layout.composereview, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        Log.w(NAME, NAME + ".onActivityCreated()");

        //Set references to the needed views
        composeReviewEditText = (EditText) view.findViewById(R.id.composeReviewEditText);
        postButton = (Button) view.findViewById(R.id.postReviewButton);
        postButton.setOnClickListener(this);
        cancelButton = (ImageButton) view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);
        //addPhotoButton = (ImageButton) view.findViewById(R.id.addPhotoButton);
        //addPhotoButton.setOnClickListener(this);
        //image = (ImageView) view.findViewById(R.id.reviewImage);

    }

    /*public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

        // we have a successful result and intent is not null
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && intent != null)
        {
            final Uri uri = intent.getData();
            final String[] projection = {
                    MediaStore.Images.Media.DATA
            };

            // read from content provider
            final Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();
            final int columnIndex = cursor.getColumnIndex(projection[0]);
            imageUrl = cursor.getString(columnIndex);
            cursor.close();
            updateImage();
        }
    }*/

    /*public void updateImage()
    {
        if (!TextUtils.isEmpty(imageUrl))
        {
            final Bitmap bitmap = BitmapFactory.decodeFile(imageUrl);

            if (bitmap != null)
            {
                image.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 256, 256, false));
                image.setVisibility(View.VISIBLE);
            }
        }
    }*/

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        Log.w(NAME, NAME + ".onSaveInstanceState()");

    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        Log.w(NAME, NAME + ".onDestroyView()");

        if (dialog != null && dialog.isShowing())
        {
            dialog.dismiss();
        }
        dialog = null;

    }
}
