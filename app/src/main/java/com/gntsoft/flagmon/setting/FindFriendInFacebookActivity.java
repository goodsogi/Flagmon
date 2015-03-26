package com.gntsoft.flagmon.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
import com.gntsoft.flagmon.server.FriendModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend_in_facebook);

        init();


    }

    private void getUserData(final Session session) {
        Request request = Request.newMeRequest(session,
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (user != null && session == Session.getActiveSession()) {

                            getFriends();

                        }
                        if (response.getError() != null) {

                        }
                    }
                });
        request.executeAsync();
    }

    private void getFriends() {
        final Session activeSession = Session.getActiveSession();
        if (activeSession.getState().isOpened()) {
            //현재 이 앱을 사용하고 있는 친구 리스트를 반환
            Request friendRequest = Request.newMyFriendsRequest(activeSession,
                    new Request.GraphUserListCallback() {
                        @Override
                        public void onCompleted(List<GraphUser> users,
                                                Response response) {

                            handleResponse(response);


                        }
                    });
            Bundle params = new Bundle();
            params.putString("fields", "id,name,picture");
            friendRequest.setParameters(params);
            friendRequest.executeAsync();
        }
    }

    private void handleResponse(Response response) {
        ArrayList<FriendModel> model = parseResponse(response);
        makeList(model);
        showFriendCount(model);
    }

    private void showFriendCount(ArrayList<FriendModel> model) {
        TextView facebookFriendCount = (TextView) findViewById(R.id.facebookFriendCount);
        facebookFriendCount.setText(model.size() + "명의 Facebook 친구가 Flagmon에 있습니다");
    }

    private void makeList(ArrayList<FriendModel> model) {
        ListView list = (ListView) findViewById(R.id.listFacebook);

        if (list == null || model == null) return;
        list.setAdapter(new FindFriendListAdapter(this,
                model));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

    }

    private ArrayList<FriendModel> parseResponse(Response response) {
        ArrayList<FriendModel> model = new ArrayList<>();
        GraphObject graphObject = response.getGraphObject();
        if (graphObject != null) {
            JSONObject jsonObject = graphObject.getInnerJSONObject();
            try {
                JSONArray array = jsonObject.getJSONArray("data");


                for (int i = 0; i < array.length(); i++) {
                    FriendModel data = new FriendModel();
                    JSONObject object = (JSONObject) array.get(i);
                    //수정이 필요할 수 있음!!
                    data.setIdx(object.optString("id"));
                    data.setName(object.optString("name"));
                    data.setProfileImageUrl(object.optString("picture"));
                    model.add(data);
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }

        }

        return model;


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

    private void init() {

        String[] PERMISSION_ARRAY_READ = {"user_friends"};
        final List<String> permissionList = Arrays.asList(PERMISSION_ARRAY_READ);

// start Facebook Login
        Session.openActiveSession(this, true, new Session.StatusCallback() {

            // callback when session changes state
            @Override
            public void call(final Session session, SessionState state, Exception exception) {
                if (session.isOpened()) {

                    if (session.getPermissions().contains("user_friends")) {
                        getFriends();
                    } else {
                        //public_profile만 디폴트로 permission이 부여되었으므로 친구리스트를 얻기 위해서는 user_friends permission을 별도로 요청해야 함
                        Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(FindFriendInFacebookActivity.this, permissionList);
                        session.requestNewReadPermissions(newPermissionsRequest);
                    }

                }
            }
        });

    }

    private void requestMyAppFacebookFriendsWithAppInstalled(Session session) {

        Request friendsRequest = createRequest(session);
        friendsRequest.setCallback(new Request.Callback() {
            @Override
            public void onCompleted(Response response) {
                //SetUpList
                List<GraphUser> friends = getResults(response);
                GraphUser user;
                ArrayList<FriendModel> friendsList = new ArrayList<FriendModel>();
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


}
