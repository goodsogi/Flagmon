package com.gntsoft.flagmon;

/**
 * Created by johnny on 15. 2. 12.
 */
public class MainListModel {

    private String title;
    private String content;
    private int img;
    private String time;
    private String replyCount;
    private String pinCount;
    private String registerDate;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(String replyCount) {
        this.replyCount = replyCount;
    }

    public String getPinCount() {
        return pinCount;
    }

    public void setPinCount(String pinCount) {
        this.pinCount = pinCount;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }
}
