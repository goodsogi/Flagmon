package com.gntsoft.flagmon.server;

import android.graphics.Bitmap;

/**
 * Created by johnny on 15. 3. 9.
 */
public class GalleryPhotoModel {
    private Bitmap thumnail;
    private String imagePath;

    public Bitmap getThumnail() {
        return thumnail;
    }

    public void setThumnail(Bitmap thumnail) {
        this.thumnail = thumnail;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
