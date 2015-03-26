package com.gntsoft.flagmon.setting;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ListView;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.R;

/**
 * Created by johnny on 15. 3. 11.
 */
public class FindFriendInContactActivity extends FMCommonActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    // Defines the text expression
//    @SuppressLint("InlinedApi")
//    private static final String SELECTION =
//            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
//                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?" :
//                    ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?";
    // Defines a variable for the search string
//    private String mSearchString="*";
    // Defines the array to hold values that replace the ?
//    private String[] mSelectionArgs = { mSearchString };

    @SuppressLint("InlinedApi")
    private static final String[] PROJECTION;

    static {
        PROJECTION = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.LOOKUP_KEY,
                Build.VERSION.SDK_INT
                        >= Build.VERSION_CODES.HONEYCOMB ?
                        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                        ContactsContract.Contacts.DISPLAY_NAME

        };
    }

    /*
         * Defines an array that contains column names to move from
         * the Cursor to the ListView.
         */
    @SuppressLint("InlinedApi")
    private final static String[] FROM_COLUMNS = {
            Build.VERSION.SDK_INT
                    >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                    ContactsContract.Contacts.DISPLAY_NAME
    };
    /*
     * Defines an array that contains resource ids for the layout views
     * that get the Cursor column contents. The id is pre-defined in
     * the Android framework, so it is prefaced with "android.R.id"
     */
    private final static int[] TO_IDS = {
            android.R.id.text1
    };
    private ListView mContactsList;
    private ContactListAdapter mContactListAdapter;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        /*
         * Makes search string into pattern and
         * stores it in the selection array
         */
//        mSelectionArgs[0] = "%" + mSearchString + "%";
        // Starts the query
//        return new CursorLoader(
//                this,
//                ContactsContract.Contacts.CONTENT_URI,
//                PROJECTION,
//                SELECTION,
//                mSelectionArgs,
//                null
//        );

        return new CursorLoader(
                this,
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                null,
                null,
                null
        );


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mContactListAdapter.swapCursor(cursor);
        showFriendCount(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Delete the reference to the existing Cursor
        mContactListAdapter.swapCursor(null);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend_in_contact);

        init();
        makeList();


    }

    private void makeList() {
        // Gets the ListView from the View list of the parent activity
        mContactsList =
                (ListView) findViewById(R.id.listContact);
        // Gets a CursorAdapter
        mContactListAdapter = new ContactListAdapter(
                this, null);
        // Sets the adapter for the ListView
        mContactsList.setAdapter(mContactListAdapter);

    }

    private void init() {
// Initializes the loader
        getLoaderManager().initLoader(0, null, this);

    }

    private void showFriendCount(Cursor cursor) {
        TextView contactFriendCount = (TextView) findViewById(R.id.contactFriendCount);
        contactFriendCount.setText(cursor.getCount() + "명의 친구가 Flagmon에 있습니다");
    }
}
