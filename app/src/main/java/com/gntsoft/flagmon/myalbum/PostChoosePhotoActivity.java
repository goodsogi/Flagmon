package com.gntsoft.flagmon.myalbum;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.pluslibrary.img.CameraAlbumActivity;
import com.pluslibrary.img.PlusImageConstants;
import com.pluslibrary.server.PlusOnGetDataListener;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 3. 3.
 */
public class PostChoosePhotoActivity extends Activity implements
        PlusOnGetDataListener {
    private static final int GET_MAIN_LIST = 0;
    private Object photosFromGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_choose_photo);
        makeGalleryView();
    }

    private void makeGalleryView() {

        final ArrayList<GalleryPhotoModel> model = getPhotosFromGallery();

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new PostGridViewAdapter(this, model));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                moveToPostSetLocationActivity(model.get(position).getImagePath());
            }
        });
    }

    private void moveToPostSetLocationActivity(String imagePath) {

        Intent intent = new Intent(this, PostSetLocationActivity.class);
        intent.putExtra(FMConstants.KEY_IMAGE_PATH, imagePath);
        startActivity(intent);
    }

    public void goBack(View v) {
        finish();
    }

    public void doCamera(View v) {
        Intent intent = new Intent(this, CameraAlbumActivity.class);
        intent.putExtra(PlusImageConstants.KEY_IMAGE_SOURCE,
                PlusImageConstants.SOURCE_CAMERA);
        startActivityForResult(intent,
                PlusImageConstants.FROM_CAMERA);
    }

    /**
     * 이미지 가져오기
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {

            // 카메라로 찍은 이미지 가져오기
            case PlusImageConstants.FROM_CAMERA:
                String imgUri = data.getStringExtra(PlusImageConstants.EXTRA_IMAGE_PATH);

                moveToPostSetLocationActivity(imgUri);

                break;
        }

    }





    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null)
            return;
        switch (from) {
            case GET_MAIN_LIST:
                //makeList(datas);
                break;
        }

    }


    public ArrayList<GalleryPhotoModel> getPhotosFromGallery() {
        ArrayList<GalleryPhotoModel> model = new ArrayList<>();

        final String[] columns = { MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media._ID;
        Cursor imagecursor = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        int image_column_index = imagecursor
                .getColumnIndex(MediaStore.Images.Media._ID);
        int count = imagecursor.getCount();

        for (int i = 0; i < count; i++) {
            GalleryPhotoModel data = new GalleryPhotoModel();
            imagecursor.moveToPosition(i);
            int id = imagecursor.getInt(image_column_index);
            int dataColumnIndex = imagecursor
                    .getColumnIndex(MediaStore.Images.Media.DATA);
            data.setThumnail(MediaStore.Images.Thumbnails.getThumbnail(
                    getApplicationContext().getContentResolver(), id,
                    MediaStore.Images.Thumbnails.MICRO_KIND, null));
            data.setImagePath(imagecursor.getString(dataColumnIndex));

            model.add(data);
        }



        return model;
    }
}
