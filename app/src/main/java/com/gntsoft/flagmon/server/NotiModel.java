package com.gntsoft.flagmon.server;

/**
 * Created by johnny on 15. 4. 21.
 */
public class NotiModel {
    private String title;
    private String content;
    private String wdate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWdate() {
        return wdate;
    }

    public void setWdate(String date) {
        this.wdate = date;
    }
}
