package com.gntsoft.flagmon.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphMultiResult;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;
import com.facebook.model.GraphUser;
import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.R;
import com.pluslibrary.utils.PlusToaster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by johnny on 15. 3. 2.
 */
public class FindFriendInFacebookActivity extends FMCommonActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend_in_facebook);

        init();


    }

    private void getUserData(final Session session){
        Request request = Request.newMeRequest(session,
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if(user != null && session == Session.getActiveSession()){
                            getFriends();

                        }
                        if(response.getError() !=null){

                        }
                    }
                });
        request.executeAsync();
    }

    private void getFriends(){
        Session activeSession = Session.getActiveSession();
        if(activeSession.getState().isOpened()){
            Request friendRequest = Request.newMyFriendsRequest(activeSession,
                    new Request.GraphUserListCallback(){
                        @Override
                        public void onCompleted(List<GraphUser> users,
                                                Response response) {
                            Log.i("INFO", response.toString());

                        }
                    });
            Bundle params = new Bundle();
            params.putString("fields", "id,name,picture");
            friendRequest.setParameters(params);
            friendRequest.executeAsync();
        }
    }

    private void init() {
// start Facebook Login
        Session.openActiveSession(this, true, new Session.StatusCallback() {

            // callback when session changes state
            @Override
            public void call(final Session session, SessionState state, Exception exception) {
                if (session.isOpened()) {

                    getFriends();
                }
            }
        });

    }






//    private void init() {
//// start Facebook Login
//        Session.openActiveSession(this, true, new Session.StatusCallback() {
//
//            // callback when session changes state
//            @Override
//            public void call(final Session session, SessionState state, Exception exception) {
//                if (session.isOpened()) {
//
//                    // make request to the /me API
//                    Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
//
//                        // callback after Graph API response with user object
//                        @Override
//                        public void onCompleted(GraphUser user, Response response) {
//                            if (user != null) {
//                                PlusToaster.doIt(FindFriendInFacebookActivity.this, "페이스북 로그인 성공!");
//                                requestMyAppFacebookFriendsWithAppInstalled(session);
//                            }
//                        }
//                    });
//                }
//            }
//        });
//
//    }

    private void requestMyAppFacebookFriendsWithAppInstalled(Session session) {

        Request friendsRequest = createRequest(session);
        friendsRequest.setCallback(new Request.Callback() {
            @Override
            public void onCompleted(Response response) {
                //SetUpList
                List<GraphUser> friends = getResults(response);
                GraphUser user;
                ArrayList<FacebookFriendModel> friendsList = new ArrayList<FacebookFriendModel>();
                boolean installed = false;
                if (friends != null) {
                    for (int count = 0; count < friends.size(); count++) {
                        user = friends.get(count);
                        if (user.getProperty("installed") != null) {
                            installed = (Boolean) user.getProperty("installed");
                            Log.i("facebook", "user: " + user.getInnerJSONObject());
                        }
                    }
                }
            }
        });

        friendsRequest.executeAsync();
    }


    private Request createRequest(Session session) {
        Request request = Request.newGraphPathRequest(session, "me/friends", null);

        Set<String> fields = new HashSet<String>();
        String[] requiredFields = new String[]{"id", "name", "picture", "hometown",
                "installed"};
        fields.addAll(Arrays.asList(requiredFields));

        Bundle parameters = request.getParameters();
        parameters.putString("fields", TextUtils.join(",", fields));
        request.setParameters(parameters);

        return request;
    }


    private class FacebookFriendModel {
        String Name, ID, ImageUrl;
        boolean selected;
    }


    private List<GraphUser> getResults(Response response) throws NullPointerException {
        try {
            GraphMultiResult multiResult = response
                    .getGraphObjectAs(GraphMultiResult.class);
            GraphObjectList<GraphObject> data = multiResult.getData();
            return data.castToListOf(GraphUser.class);
        } catch (NullPointerException e) {
            return null;
            //at times the flow enters this catch block. I could not figure out the reason for this.
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }



}
