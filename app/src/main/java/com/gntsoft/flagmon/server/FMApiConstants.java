package com.gntsoft.flagmon.server;

/**
 * Created by johnny on 15. 3. 6.
 */
public class FMApiConstants {
    private static final String API_MAIN = "http://www.flagmon.com/site/json/";
    public static final String SIGN_UP = API_MAIN + "signup_ok.php";


    public static final String CHECK_EMAIL =  API_MAIN + "email_chk.php";
    public static final String LOG_IN = API_MAIN + "login_ok.php";
    public static final String GET_MAP_DATA = API_MAIN + "maplist.php";
    public static final String GET_LIST_DATA =API_MAIN + "postlist.php";
    public static final String SEND_POST = API_MAIN + "posting_ok.php";
    public static final String GET_DETAIL = API_MAIN + "postview.php";
    public static final String GET_COMMENTS = API_MAIN + "commentlist.php";
    public static final String SEND_COMMENT = API_MAIN + "comment_ok.php";
    public static final String PERFORM_PIN = "";
    public static final String GET_PHOTOS_NEARBY = API_MAIN + "postlist.php";
    public static final String GET_TOTAL_USER_POST = "";
    public static final String ADD_FRIEND = "friendrequest.php";
    public static final String REQUEST_PASSWORD = "";
    public static final String REPORT_POST = "";
    public static final String FIND_TREASURE = "searchtreasure.php"; //url 수정이 필요할 수 있음 !!(/json/이 없음)
    public static final String GET_SENT_FRIEND_REQUEST = "requestsent.php"; //url 수정이 필요할 수 있음 !!(/json/이 없음)
    public static final String GET_GOT_FRIEND_REQUEST = "requestreceived.php"; //url 수정이 필요할 수 있음 !!(/json/이 없음)
    public static final String CANCEL_REQUEST = "cancelrequest.php";
    public static final String ACCEPT_REQUEST = "acceptrequest.php";
    public static final String IGNORE_REQUEST = "rejectrequest.php"; //url 수정이 필요할 수 있음 !!(/json/이 없음)
    public static final String SEND_FRIEND_REQUEST = "";
    public static final String SEARCH_FRIENDS_BY_NAME = "searchfriendname.php";
    public static final String CHOOSE_FRIEND = "";
    public static final String DELETE_FRIEND = "";
    public static final String SELECT_FRIEND = "deletefriend.php";
    public static final String CHECK_IF_HAS_FRIEND = "";
    public static final String SCRAP_THIS = "";
    public static final String GET_USER_LIST_DATA ="postlistuser.php";
    public static final String GET_USER_MAP_DATA = "maplistuser.php";
    public static final String SEARCH_LOCATION = "";
    public static final String BURY_TREASURE = "";
    public static final String MAKE_ALBUM = "";
}
