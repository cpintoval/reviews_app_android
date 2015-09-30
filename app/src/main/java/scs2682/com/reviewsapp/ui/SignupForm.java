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
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import scs2682.com.reviewsapp.ApplicationActivity;
import scs2682.com.reviewsapp.R;

/**
 * Created by carlospinto on 7/16/15.
 */
public class SignupForm extends Fragment implements View.OnClickListener
{
    public static final String NAME = SignupForm.class.getSimpleName();

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signupButton;
    private Button cancelButton;
    private AlertDialog dialog;

    public void reset()
    {
        usernameEditText.setText("");
        usernameEditText.setCursorVisible(true);
        emailEditText.setText("");
        emailEditText.setCursorVisible(true);
        passwordEditText.setText("");
        passwordEditText.setCursorVisible(true);
        passwordEditText.requestFocus();
    }

    public void onClick(View view)
    {
        if(cancelButton.equals(view))
        {
            ((ApplicationActivity) getActivity()).showLoginForm(SignupForm.NAME);
        }
        else if (signupButton.equals(view))
        {
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser == null)
            {
                final String username = usernameEditText.getText() != null ? usernameEditText.getText().toString().trim() : "";
                final String email = emailEditText.getText() != null ? emailEditText.getText().toString().trim() : "";
                final String password = passwordEditText.getText() != null ? passwordEditText.getText().toString().trim() : "";

                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))
                {
                    // Fields are filled, proceed with sign up.
                    ParseUser user = new ParseUser();
                    user.setUsername(username);
                    user.setEmail(email);
                    user.setPassword(password);

                    user.signUpInBackground(new SignUpCallback()
                    {
                        @Override
                        public void done(ParseException e)
                        {
                            if (e == null)
                            {
                                // Sign up succeed!
                                // Show the Home Page
                                ApplicationActivity aa = (ApplicationActivity) getActivity();
                                Toast.makeText(aa, "Welcome, " + username, Toast.LENGTH_LONG).show();
                                aa.showHomePage(SignupForm.NAME);
                            }
                            else
                            {
                                // Sign up didn't succeed.

                                if (dialog != null && dialog.isShowing())
                                {
                                    dialog.dismiss();
                                }

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
                    // One or more fields are empty, raise an error.
                    if (TextUtils.isEmpty(username))
                    {
                        usernameEditText.requestFocus();
                    }
                    else if (TextUtils.isEmpty(email))
                    {
                        emailEditText.requestFocus();
                    }
                    else
                    {
                        passwordEditText.requestFocus();
                    }

                    // Create an error dialog
                    // Close any previous alert dialog
                    if (dialog != null && dialog.isShowing())
                    {
                        dialog.dismiss();
                    }

                    // create the dialog by using its Builder
                    dialog = new AlertDialog.Builder(getActivity())
                            .setTitle("Error")
                            .setMessage("Please provide the information required.")
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
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.w(NAME, NAME + ".onCreateView()");
        return inflater.inflate(R.layout.signupform, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        Log.w(NAME, NAME + ".onActivityCreated()");

        //Set references to the needed views
        usernameEditText = (EditText) view.findViewById(R.id.usernameEditTextSignupForm);
        emailEditText = (EditText) view.findViewById(R.id.emailEditTextSignupForm);
        passwordEditText = (EditText) view.findViewById(R.id.passwordEditTextSignupForm);
        signupButton = (Button) view.findViewById(R.id.signupButtonSignupForm);
        signupButton.setOnClickListener(this);
        cancelButton = (Button) view.findViewById(R.id.cancelButtonSignupForm);
        cancelButton.setOnClickListener(this);

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
