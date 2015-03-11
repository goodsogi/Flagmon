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
}
