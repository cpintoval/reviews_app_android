package scs2682.com.reviewsapp.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import scs2682.com.reviewsapp.Application;
import scs2682.com.reviewsapp.ApplicationActivity;
import scs2682.com.reviewsapp.R;

/**
 * Created by carlospinto on 7/18/15.
 */
public class ReviewsPage extends Fragment implements View.OnClickListener
{
    public static final String NAME = ReviewsPage.class.getSimpleName();

    private static final class CellViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private final TextView username;
        private final TextView date;
        private final TextView review;

        private ParseObject reviewObject;

        private CellViewHolder(View view)
        {
            super(view);
            username = (TextView) view.findViewById(R.id.usernameTextViewReviewCell);
            date = (TextView) view.findViewById(R.id.dateTextViewReviewCell);
            review = (TextView) view.findViewById(R.id.reviewTextViewReviewCell);
        }

        private void update(ParseObject object)
        {
            if (object == null)
            {
                return;
            }

            this.reviewObject = object;

            itemView.setOnClickListener(this);

            //DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            //Date creationDate = reviewObject.getCreatedAt();
            String sDate = reviewObject.getUpdatedAt().toString();

            username.setText(reviewObject.getString("user"));
            date.setText(sDate);
            review.setText(reviewObject.getString("text"));
        }

        public void onClick(final View view)
        {
            ParseUser currentUser = ParseUser.getCurrentUser();

            if (currentUser != null)
            {
                if (currentUser.getUsername().equals(username.getText().toString()))
                {
                    // The user made the review, show the detail view.
                    ApplicationActivity aa = (ApplicationActivity) view.getContext();
                    aa.showDetailView(ReviewsPage.NAME, reviewObject.getString("subject"), reviewObject.getObjectId());
                }
            }
        }
    }

    private static final class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    {
        private final LayoutInflater layoutInflater;
        private final List<ParseObject> items;
        private final int itemsSize;


        private Adapter(@NonNull Context context, List<ParseObject> items)
        {
            layoutInflater = LayoutInflater.from(context);

            setHasStableIds(true);

            this.items = items != null ? items : Collections.<ParseObject>emptyList();
            itemsSize = this.items.size();
        }

        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup recyclerView, final int viewType)
        {
            View view = layoutInflater.inflate(viewType, recyclerView, false);
            // for empty cell use ViewHolder, otherwise CellViewHolder
            return R.layout.review_empty_cell == viewType ? new RecyclerView.ViewHolder(view) {
            } : new CellViewHolder(view);
        }

        public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position)
        {
            if (viewHolder instanceof CellViewHolder)
            {
                ((CellViewHolder) viewHolder).update(items.get(position));
            }
        }

        public int getItemCount()
        {
            return itemsSize;
        }

        public int getItemViewType(final int position)
        {
            // if model is null and only one item, we treat this one as no data - will be used
            // to load R.layout.contacts_empty_cell. Otherwise regular layout
            ParseObject object = items.get(position);
            return itemsSize == 0 || object == null ? R.layout.review_empty_cell : R.layout.review_cell;
        }

        public long getItemId(final int position)
        {
            return position;
        }
    }

    private RecyclerView list;
    private AlertDialog dialog;
    private ImageButton homeButton;
    private ImageButton addButton;

    public static ReviewsPage newInstance(String subject)
    {
        // stay as much as possible away from null
        subject = subject != null ? subject : "";

        // use this bundle as setArguments() so we can get it from within onActivityCreated()
        final Bundle bundle = new Bundle();
        bundle.putString(ApplicationActivity.SUBJECT_KEY, subject);

        final ReviewsPage fragment = new ReviewsPage();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void update()
    {
        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null)
        {
            final Activity activity = getActivity();
            Bundle arguments = getArguments();
            String subject = arguments.getString(ApplicationActivity.SUBJECT_KEY);

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Review");
            query.whereEqualTo("subject", subject);
            query.findInBackground(new FindCallback<ParseObject>()
            {
                @Override
                public void done(List<ParseObject> reviewsList, ParseException e)
                {
                    if (e == null)
                    {
                        // List successfully retrieved.
                        Collections.reverse(reviewsList);
                        list.setAdapter(new Adapter(activity, reviewsList));
                    }
                    else
                    {
                        // There was an error retrieving the list.
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
    }

    public void onClick(final View view)
    {
        if (homeButton.equals(view))
        {
            ((ApplicationActivity) getActivity()).showHomePage(ReviewsPage.NAME);
        }
        else if (addButton.equals(view))
        {
            Bundle arguments = getArguments();
            String subject = arguments.getString(ApplicationActivity.SUBJECT_KEY);
            ((ApplicationActivity) getActivity()).showComposeReview(ReviewsPage.NAME, subject);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.reviewspage, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        list = (RecyclerView) view.findViewById(R.id.recyclerView);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        list.setItemAnimator(new DefaultItemAnimator());

        homeButton = (ImageButton) view.findViewById(R.id.homeButton);
        homeButton.setOnClickListener(this);
        addButton = (ImageButton) view.findViewById(R.id.addButton);
        addButton.setOnClickListener(this);

        // enable options menu
        //setHasOptionsMenu(true);

        update();
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.reviews, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.homePageButton:
                ((ApplicationActivity) getActivity()).showHomePage(ReviewsPage.NAME);
                return true;
            case R.id.addReviewButton:
                Bundle arguments = getArguments();
                String subject = arguments.getString(ApplicationActivity.SUBJECT_KEY);
                ((ApplicationActivity) getActivity()).showComposeReview(ReviewsPage.NAME, subject);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
}
