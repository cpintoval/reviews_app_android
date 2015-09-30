package scs2682.com.reviewsapp.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
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

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import scs2682.com.reviewsapp.Application;
import scs2682.com.reviewsapp.ApplicationActivity;
import scs2682.com.reviewsapp.R;

/**
 * Created by carlospinto on 7/21/15.
 */
public class DetailView extends Fragment implements View.OnClickListener
{
    public static DetailView newInstance(String subject, String objectId)
    {
        // stay as much as possible away from null
        subject = subject != null ? subject : "";
        objectId = objectId != null ? objectId : "";

        // use this bundle as setArguments() so we can get it from within onActivityCreated()
        final Bundle bundle = new Bundle();
        bundle.putString(ApplicationActivity.SUBJECT_KEY, subject);
        bundle.putString(ApplicationActivity.OBJECT_ID_KEY, objectId);

        final DetailView fragment = new DetailView();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static final String NAME = DetailView.class.getSimpleName();

    private ImageButton cancelButton;
    private EditText reviewEditText;
    private Button updateReview;
    private ImageButton deleteReview;
    private AlertDialog dialog;

    public void onClick(View view)
    {
        if (cancelButton.equals(view))
        {
            Bundle arguments = getArguments();
            String subject = arguments.getString(ApplicationActivity.SUBJECT_KEY);
            ((ApplicationActivity) getActivity()).showReviewsPage(DetailView.NAME, subject);
        }
        else if (updateReview.equals(view))
        {
            final String updatedText = reviewEditText.getText().toString();

            if (!TextUtils.isEmpty(updatedText))
            {
                Bundle arguments = getArguments();
                String objectID = arguments.getString(ApplicationActivity.OBJECT_ID_KEY);

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Review");
                query.getInBackground(objectID, new GetCallback<ParseObject>()
                {
                    @Override
                    public void done(ParseObject parseObject, ParseException e)
                    {
                        if (e == null)
                        {
                            parseObject.put("text", updatedText);
                            parseObject.saveInBackground();

                            Bundle arguments = getArguments();
                            String subject = arguments.getString(ApplicationActivity.SUBJECT_KEY);

                            ApplicationActivity aa = (ApplicationActivity) getActivity();
                            Toast.makeText(aa, "Review Updated!", Toast.LENGTH_LONG).show();
                            aa.showReviewsPage(DetailView.NAME, subject);
                        }
                        else
                        {
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
                if (dialog != null && dialog.isShowing())
                {
                    dialog.dismiss();
                }

                // create the dialog by using its Builder
                dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Empty Review")
                        .setMessage("Please type in your updated review.")
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
        else if (deleteReview.equals(view))
        {
            if (dialog != null && dialog.isShowing())
            {
                dialog.dismiss();
            }

            // create the dialog by using its Builder
            dialog = new AlertDialog.Builder(getActivity())
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete the review?")
                    .setCancelable(true)
                    .setIcon(R.drawable.ic_warning_black_24dp)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which)
                        {
                            Bundle arguments = getArguments();
                            String objectID = arguments.getString(ApplicationActivity.OBJECT_ID_KEY);

                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Review");
                            query.getInBackground(objectID, new GetCallback<ParseObject>()
                            {
                                @Override
                                public void done(ParseObject parseObject, ParseException e)
                                {
                                    if (e == null)
                                    {
                                        parseObject.deleteInBackground();

                                        Bundle arguments = getArguments();
                                        String subject = arguments.getString(ApplicationActivity.SUBJECT_KEY);

                                        ApplicationActivity aa = (ApplicationActivity) getActivity();
                                        Toast.makeText(aa, "Review Deleted!", Toast.LENGTH_LONG).show();
                                        aa.showReviewsPage(DetailView.NAME, subject);
                                    }
                                    else
                                    {
                                        if (dialog != null && dialog.isShowing())
                                        {
                                            dialog.cancel();
                                            dialog = null;
                                        }

                                        ApplicationActivity aa = (ApplicationActivity) getActivity();
                                        Toast.makeText(aa, "Error Deleting", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which)
                        {
                            if (dialog != null && dialog.isShowing())
                            {
                                dialog.cancel();
                                dialog = null;
                            }
                        }
                    })
                    .create();
            dialog.show();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.w(NAME, NAME + ".onCreateView()");
        return inflater.inflate(R.layout.detailview, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        Log.w(NAME, NAME + ".onActivityCreated()");

        //Set references to the needed views
        reviewEditText = (EditText) view.findViewById(R.id.editReviewEditText);
        updateReview = (Button) view.findViewById(R.id.updateReviewButton);
        updateReview.setOnClickListener(this);
        cancelButton = (ImageButton) view.findViewById(R.id.cancelButtonDetailView);
        cancelButton.setOnClickListener(this);
        deleteReview = (ImageButton) view.findViewById(R.id.deleteReviewButton);
        deleteReview.setOnClickListener(this);

        update();

    }

    public void update()
    {
        Bundle arguments = getArguments();
        String objectID = arguments.getString(ApplicationActivity.OBJECT_ID_KEY);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Review");
        query.getInBackground(objectID, new GetCallback<ParseObject>()
        {
            @Override
            public void done(ParseObject parseObject, ParseException e)
            {
                if (e == null)
                {
                    reviewEditText.setText(parseObject.getString("text"));
                }
                else
                {
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
