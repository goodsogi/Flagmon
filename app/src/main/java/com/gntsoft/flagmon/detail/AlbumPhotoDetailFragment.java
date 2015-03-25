package com.gntsoft.flagmon.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gntsoft.flagmon.FMCommonFragment;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusOnClickListener;

/**
 * Created by johnny on 15. 3. 19.
 */
public class AlbumPhotoDetailFragment extends FMCommonFragment implements
        PlusOnGetDataListener {

    public AlbumPhotoDetailFragment() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //아마 viewpager 사용하도록 수정!!
        View rootView = inflater.inflate(R.layout.fragment_album_detail,
                container, false);
        return rootView;
    }

    @Override
    protected void addListenerToButton() {
        ImageView mainImage = (ImageView) mActivity.findViewById(R.id.main_img);
        mImageLoader.displayImage(getArguments().getString(FMConstants.KEY_IMAGE_URL, ""), mainImage, mOption);
        mainImage.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                goToImageViewer();
            }
        });
    }

    private void goToImageViewer() {
        Intent intent = new Intent(mActivity, ImageViewerActivity.class);
        intent.putExtra(FMConstants.KEY_IMAGE_URL, getArguments().getString(FMConstants.KEY_IMAGE_URL, ""));
        startActivity(intent);

    }

    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null)
            return;
//        switch (from) {
//            case GET_MAP_DATA:
//                makeList(datas);
//                break;
//        }

    }

}
