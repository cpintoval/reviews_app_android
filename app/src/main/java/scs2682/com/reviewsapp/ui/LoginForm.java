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

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import scs2682.com.reviewsapp.ApplicationActivity;
import scs2682.com.reviewsapp.R;

/**
 * Created by carlospinto on 7/16/15.
 */
public class LoginForm extends Fragment implements View.OnClickListener
{
    public static final String NAME = LoginForm.class.getSimpleName();

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button signupButton;
    private AlertDialog dialog;


    public void reset()
    {
        usernameEditText.setText("");
        usernameEditText.setCursorVisible(true);
        passwordEditText.setText("");
        passwordEditText.setCursorVisible(true);
        passwordEditText.requestFocus();
    }

    public void onClick(View view)
    {
        if (signupButton.equals(view))
        {
            ((ApplicationActivity) getActivity()).showSignupForm(LoginForm.NAME);
        }
        else if (loginButton.equals(view))
        {
            final String username = usernameEditText.getText() != null ? usernameEditText.getText().toString().trim() : "";
            final String password = passwordEditText.getText() != null ? passwordEditText.getText().toString().trim() : "";

            if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password))
            {
                // Fields are filled up, proceed with login.
                ParseUser.logInInBackground(username, password, new LogInCallback()
                {
                    @Override
                    public void done(ParseUser parseUser, ParseException e)
                    {
                        if (parseUser != null)
                        {
                            // Login succeed.
                            ApplicationActivity aa = (ApplicationActivity) getActivity();
                            Toast.makeText(aa, "Welcome back, " + username, Toast.LENGTH_LONG).show();
                            aa.showHomePage(LoginForm.NAME);
                        }
                        else
                        {
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

                            reset();
                        }
                    }
                });
            }
            else
            {
                if (TextUtils.isEmpty(username))
                {
                    usernameEditText.requestFocus();
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
                        .setIcon(R.drawable.ic_error_black_24dp)
                        .setCancelable(true)
                        .setNegativeButton("Close", new DialogInterface.OnClickListener()
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
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.w(NAME, NAME + ".onCreateView()");
        return inflater.inflate(R.layout.loginform, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        Log.w(NAME, NAME + ".onActivityCreated()");

        //Set references to the needed views
        usernameEditText = (EditText) view.findViewById(R.id.usernameEditTextLoginForm);
        passwordEditText = (EditText) view.findViewById(R.id.passwordEditTextLoginForm);
        loginButton = (Button) view.findViewById(R.id.loginButtonLoginForm);
        loginButton.setOnClickListener(this);
        signupButton = (Button) view.findViewById(R.id.signupButtonLoginForm);
        signupButton.setOnClickListener(this);

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
