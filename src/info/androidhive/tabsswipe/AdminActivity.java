package info.androidhive.tabsswipe;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class AdminActivity extends ListActivity {
    ArrayAdapter<String> mAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending);

        // Set up ListView example
        String[] items = new String[20];
        for (int i = 0; i < items.length; i++) {
            items[i] = "Update Request " + (i + 1);
        }

        mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                new ArrayList<String>(Arrays.asList(items)));
        setListAdapter(mAdapter);

        ListView listView = getListView();
        // Create a ListView-specific touch listener. ListViews are given special treatment because
        // by default they handle touches for their list items... i.e. they're in charge of drawing
        // the pressed state (the list selector), handling list item clicks, etc.
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView,
                        new SwipeDismissListViewTouchListener.OnDismissCallback() {
                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    mAdapter.remove(mAdapter.getItem(position));
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });
        listView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listView.setOnScrollListener(touchListener.makeScrollListener());

        // Set up normal ViewGroup example
        final ViewGroup dismissableContainer = (ViewGroup) findViewById(R.id.dismissable_container);
        for (int i = 0; i < items.length; i++) {
            final Button dismissableButton = new Button(this);
            dismissableButton.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dismissableButton.setText("Order Approval " + (i + 1));
            dismissableButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(AdminActivity.this,
                            "Clicked " + ((Button) view).getText(),
                            Toast.LENGTH_SHORT).show();
                    dismissableContainer.removeView(dismissableButton);
                }
            });
            // Create a generic swipe-to-dismiss touch listener.
            dismissableButton.setOnTouchListener(new SwipeDismissTouchListener(
                    dismissableButton,
                    null,
                    new SwipeDismissTouchListener.OnDismissCallback() {
                        @Override
                        public void onDismiss(View view, Object token) {
                            dismissableContainer.removeView(dismissableButton);
                        }
                    }));
            dismissableContainer.addView(dismissableButton);
        }
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        Toast.makeText(this,
                "Clicked " + getListAdapter().getItem(position).toString(),
                Toast.LENGTH_SHORT).show();
    }
}
