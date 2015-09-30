package scs2682.com.reviewsapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;

import scs2682.com.reviewsapp.ui.ComposeReview;
import scs2682.com.reviewsapp.ui.DetailView;
import scs2682.com.reviewsapp.ui.HomePage;
import scs2682.com.reviewsapp.ui.LoginForm;
import scs2682.com.reviewsapp.ui.ReviewsPage;
import scs2682.com.reviewsapp.ui.SignupForm;

public class ApplicationActivity extends Activity
{
    public static final String NAME = ApplicationActivity.class.getSimpleName();

    public static final String SUBJECT_KEY = "subjectKey";
    public static final String USERNAME_KEY = "usernameKey";
    public static final String EMAIL_KEY = "emailKey";
    public static final String REVIEWS_KEY = "reviewsKey";
    public static final String OBJECT_ID_KEY = "objectIDKey";

    private static final int ZBAR_SCANNER_REQUEST = 0;
    private static final int ZBAR_QR_SCANNER_REQUEST = 1;
    public static final String SCAN_RESULT = "scanResult";

    public void showSignupForm(String name)
    {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment oldSignup = fragmentManager.findFragmentByTag(SignupForm.NAME);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (oldSignup != null)
        {
            fragmentTransaction.remove(oldSignup);
        }

        Fragment oldFragment = fragmentManager.findFragmentByTag(name);
        fragmentTransaction.remove(oldFragment);

        fragmentTransaction
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.rootView, new SignupForm(), SignupForm.NAME)
                .commit();

        // Hide Keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public void showLoginForm(String removeName)
    {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment oldLogin = fragmentManager.findFragmentByTag(LoginForm.NAME);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (oldLogin != null)
        {
            fragmentTransaction.remove(oldLogin);
        }

        Fragment oldFragment = fragmentManager.findFragmentByTag(removeName);
        fragmentTransaction.remove(oldFragment);

        fragmentTransaction
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.rootView, new LoginForm(), LoginForm.NAME)
                .commit();

        // Hide Keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public void showHomePage(String removeName)
    {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment oldHomePage = fragmentManager.findFragmentByTag(HomePage.NAME);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Remove any old Home Page.
        if (oldHomePage != null)
        {
            fragmentTransaction.remove(oldHomePage);
        }

        // Remove the previous Fragment that was visible.
        Fragment previousFragment = fragmentManager.findFragmentByTag(removeName);
        fragmentTransaction.remove(previousFragment);


        // Create the new Fragment and add it to the Root View.
        fragmentTransaction
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.rootView, new HomePage(), HomePage.NAME)
                .commit();

        // Hide Keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public void showComposeReview(String removeName, String subject)
    {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment oldComposeReview = fragmentManager.findFragmentByTag(ComposeReview.NAME);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (oldComposeReview != null)
        {
            fragmentTransaction.remove(oldComposeReview);
        }

        // Remove the previous Fragment that was visible.
        Fragment previousFragment = fragmentManager.findFragmentByTag(removeName);
        fragmentTransaction.remove(previousFragment);


        // Create the new Fragment and add it to the Root View.
        fragmentTransaction
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.rootView, ComposeReview.newInstance(subject), ComposeReview.NAME)
                .commit();

        // Hide Keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public void showReviewsPage(String removeName, String subject)
    {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment oldReviewsPage = fragmentManager.findFragmentByTag(ReviewsPage.NAME);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (oldReviewsPage != null)
        {
            fragmentTransaction.remove(oldReviewsPage);
        }

        // Remove the previous Fragment that was visible.
        Fragment previousFragment = fragmentManager.findFragmentByTag(removeName);
        fragmentTransaction.remove(previousFragment);


        // Create the new Fragment and add it to the Root View.
        fragmentTransaction
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.rootView, ReviewsPage.newInstance(subject), ReviewsPage.NAME)
                .commit();

        // Hide Keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public void showDetailView(String removeName, String subject, String objectId)
    {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment oldDetailView = fragmentManager.findFragmentByTag(DetailView.NAME);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (oldDetailView != null)
        {
            fragmentTransaction.remove(oldDetailView);
        }

        // Remove the previous Fragment that was visible.
        Fragment previousFragment = fragmentManager.findFragmentByTag(removeName);
        fragmentTransaction.remove(previousFragment);


        // Create the new Fragment and add it to the Root View.
        fragmentTransaction
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.rootView, DetailView.newInstance(subject, objectId), DetailView.NAME)
                .commit();

        // Hide Keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applicationactivity);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "zymKa9lgPxYgIDR5h5HsI3DKxzi4yw22PCSRClvP", "Nxmu5Vrkmh0mI7YCYMKSeW3339BPfZW2iIwzI9ca");

        // add Form
        if (savedInstanceState == null) {
            // adding a fragment dynamically requires to ensure activity is not recreated from saved instance state
            // if 'savedInstanceState' is null - this menas activity is created for the first time, ie. app just started
            getFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.rootView, new LoginForm(), LoginForm.NAME)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.applicationactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startScannerActivity()
    {
        Intent intent = new Intent(this, ScannerActivity.class);
        startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            String subject = data.getStringExtra(SCAN_RESULT);
            Toast.makeText(this, subject + " scanned", Toast.LENGTH_SHORT).show();
            showReviewsPage(HomePage.NAME, subject);
        }
        else if(resultCode == RESULT_CANCELED)
        {
            Toast.makeText(this, "Camera unavailable", Toast.LENGTH_SHORT).show();
        }
    }
}
