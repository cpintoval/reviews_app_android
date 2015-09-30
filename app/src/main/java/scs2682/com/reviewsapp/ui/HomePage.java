package scs2682.com.reviewsapp.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


import java.util.List;

import scs2682.com.reviewsapp.ApplicationActivity;
import scs2682.com.reviewsapp.R;

/**
 * Created by carlospinto on 7/17/15.
 */
public class HomePage extends Fragment implements View.OnClickListener
{
    public static final String NAME = HomePage.class.getSimpleName();

    private Button logoutButton;
    private Button searchButton;
    private Button scanButton;
    private EditText queryEditText;
    private TextView tUsername;
    private TextView tEmail;
    private TextView tReviews;
    private AlertDialog dialog;

    public void onClick(View view)
    {
        if (logoutButton.equals(view))
        {
            ParseUser.logOut();
            ((ApplicationActivity) getActivity()).showLoginForm(HomePage.NAME);
        }
        else if (searchButton.equals(view))
        {
            String query = queryEditText.getText().toString();

            if (!TextUtils.isEmpty(query))
            {
                ((ApplicationActivity) getActivity()).showReviewsPage(HomePage.NAME, query);
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
                        .setTitle("Empty Query")
                        .setMessage("Please type in your query.")
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
        else if(scanButton.equals(view))
        {
            // Call method from the main activity that calls the Intent
            ((ApplicationActivity) getActivity()).startScannerActivity();
        }

    }

    public void update()
    {
        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null)
        {
            String username = currentUser.getUsername();
            tUsername.setText(username);
            String email = currentUser.getEmail();
            tEmail.setText(email);

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Review");
            query.whereEqualTo("user", username);
            query.findInBackground(new FindCallback<ParseObject>()
            {
                @Override
                public void done(List<ParseObject> list, ParseException e)
                {
                    if (e == null)
                    {
                        // List successfully retrieved.
                        tReviews.setText("" + list.size());
                    }
                    else
                    {
                        // List was not retrieved.
                        tReviews.setText("0");
                    }
                }
            });

        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.w(NAME, NAME + ".onCreateView()");
        return inflater.inflate(R.layout.homepage, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        Log.w(NAME, NAME + ".onActivityCreated()");

        logoutButton = (Button) view.findViewById(R.id.logoutButtonHomePage);
        logoutButton.setOnClickListener(this);
        searchButton = (Button) view.findViewById(R.id.searchButtonHomePage);
        searchButton.setOnClickListener(this);
        scanButton = (Button) view.findViewById(R.id.scanQRCodeButtonHomePage);
        scanButton.setOnClickListener(this);
        queryEditText = (EditText) view.findViewById(R.id.queryEditTextHomePage);
        tUsername = (TextView) view.findViewById(R.id.usernameTextViewHomePage);
        tEmail = (TextView) view.findViewById(R.id.emailTextViewHomePage);
        tReviews = (TextView) view.findViewById(R.id.reviewsNumberTextViewHomePage);

        update();

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
