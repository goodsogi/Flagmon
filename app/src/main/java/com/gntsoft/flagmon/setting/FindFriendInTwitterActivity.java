package com.gntsoft.flagmon.setting;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.server.FriendModel;

import java.util.ArrayList;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Created by johnny on 15. 3. 11.
 */
public class FindFriendInTwitterActivity extends FMCommonActivity {

    ArrayList<FriendModel> mModel = new ArrayList<>();
    // Twitter
    private Twitter mTwitter;
    private ProgressDialog mProgressDialog;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Twitter Oauth
        if (requestCode == FMConstants.TWITTER_OAUTH
                && resultCode == Activity.RESULT_OK) {
            showProgressDialog("친구 데이터 가져오는중...");
            getAccessToken(data);
            return;

        }
    }

    public void getTwitterUserId() {

        new GetUserIdTask().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend_in_twitter);

        connectToTwitter();


    }

    private void connectToTwitter() {
        showProgressDialog("Twitter에 연결중...");

        initTwitter();


        new GetTwitterRequestTokenTask().execute();
    }

    private void initTwitter() {
        // Create twitter instance
        mTwitter = new TwitterFactory().getInstance();
        mTwitter.setOAuthConsumer(FMConstants.TWITTER_CONSUMER_KEY,
                FMConstants.TWITTER_CONSUMER_SECRET);
    }

    private void showProgressDialog(String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    private void getAccessToken(Intent data) {

        String oauthVerifier = (String) data.getExtras().get(
                FMConstants.URL_TWITTER_OAUTH_VERIFIER);
        new GetTwitterAccessTokenTask().execute(oauthVerifier);

    }

    private void showFriendCount() {
        TextView twitterFriendCount = (TextView) findViewById(R.id.twitterFriendCount);
        twitterFriendCount.setText(mModel.size() + "명의 Twitter 친구가 Flagmon에 있습니다");
    }

    private void makeList() {
        ListView list = (ListView) findViewById(R.id.listTwitter);

        if (list == null || mModel == null) return;
        list.setAdapter(new FindFriendListAdapter(this,
                mModel));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

    }

    private void getFriendsData(IDs ids) {
        long[] friendIds = ids.getIDs();
        int friendIdsSize = friendIds.length;
        for (int i = 0; i < friendIdsSize; i++) {
            getUser(friendIds[i], i == friendIdsSize - 1);
        }
    }

    private void addUserToModel(User user) {
        FriendModel data = new FriendModel();
        //수정이 필요할 수 있음!!
        data.setIdx(String.valueOf(user.getId()));
        data.setName(user.getName());
        data.setProfileImageUrl(user.getProfileImageURL());
        mModel.add(data);
    }

    private void getUser(long id, boolean isEnd) {
        new GetUserTask().execute(id, isEnd);
    }

    private void getFriendIds(Long id) {

        new GetFriendIdsTask().execute(id);
    }

    /**
     * Get Twitter request token
     *
     * @author user
     */
    class GetTwitterRequestTokenTask extends
            AsyncTask<Void, Void, RequestToken> {

        @Override
        protected RequestToken doInBackground(Void... params) {

            RequestToken requestToken = null;
            try {
                // Set access token null because of error:
                // java.lang.IllegalStateException: Access token already
                // available
                // not sure it would work
                mTwitter.setOAuthAccessToken(null);
                requestToken = mTwitter
                        .getOAuthRequestToken(FMConstants.TWITTER_CALLBACK_URL);

            } catch (TwitterException e) {
                e.printStackTrace();
            }

            return requestToken;
        }

        @Override
        protected void onPostExecute(RequestToken requestToken) {
            if (requestToken == null)
                return;
            mProgressDialog.dismiss();


            // Start webview to get oauth token
            Intent i = new Intent(FindFriendInTwitterActivity.this, WebviewLoginActivity.class);
            i.putExtra(FMConstants.KEY_URL,
                    requestToken.getAuthenticationURL());
            startActivityForResult(i, FMConstants.TWITTER_OAUTH);

        }

    }

    /**
     * Get Twitter access token
     *
     * @author user
     */
    class GetTwitterAccessTokenTask extends
            AsyncTask<String, Void, AccessToken> {

        @Override
        protected AccessToken doInBackground(String... params) {

            AccessToken accessToken = null;
            String oauthVerifier = params[0];
            try {

                accessToken = mTwitter.getOAuthAccessToken(oauthVerifier);

            } catch (TwitterException e) {
                e.printStackTrace();
            }

            return accessToken;
        }

        @Override
        protected void onPostExecute(AccessToken accessToken) {
            if (accessToken == null)
                return;

            mTwitter.setOAuthAccessToken(accessToken);

            getTwitterUserId();


        }

    }

    class GetUserIdTask extends
            AsyncTask<String, Void, Long> {

        @Override
        protected Long doInBackground(String... params) {

            try {
                return mTwitter.getId();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long id) {
            if (id == null)
                return;

            getFriendIds(id);
        }

    }

    class GetFriendIdsTask extends
            AsyncTask<Long, Void, IDs> {

        @Override
        protected IDs doInBackground(Long... params) {


            Long userId = params[0];

            long cursor = -1;
            IDs ids;

            try {
                return mTwitter.getFriendsIDs(userId, cursor);

            } catch (TwitterException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(IDs ids) {
            if (ids == null) {
                showFriendCount();
                mProgressDialog.dismiss();
                return;
            }

            getFriendsData(ids);


        }

    }

    class GetUserTask extends
            AsyncTask<Object, Void, User> {

        private boolean mIsEnd;

        @Override
        protected User doInBackground(Object... params) {


            Long userId = (Long) params[0];
            mIsEnd = (boolean) params[1];

            try {
                return mTwitter.showUser(userId);

            } catch (TwitterException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            if (user == null)
                return;

            addUserToModel(user);


            if (mIsEnd) {
                makeList();
                showFriendCount();
                mProgressDialog.dismiss();
            }


        }

    }


}
