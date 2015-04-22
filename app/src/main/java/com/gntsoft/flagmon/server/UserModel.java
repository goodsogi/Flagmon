package com.gntsoft.flagmon.server;

/**
 * Created by johnny on 15. 4. 22.
 */
public class UserModel {
    private String statusMsg;
    private String birthday;
    private String userTotalPost;
    private String userTotalScrap;


    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUserTotalPost() {
        return userTotalPost;
    }

    public void setUserTotalPost(String userTotalPost) {
        this.userTotalPost = userTotalPost;
    }

    public String getUserTotalScrap() {
        return userTotalScrap;
    }

    public void setUserTotalScrap(String userTotalScrap) {
        this.userTotalScrap = userTotalScrap;
    }
}
