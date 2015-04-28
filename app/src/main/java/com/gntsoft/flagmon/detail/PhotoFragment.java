package com.gntsoft.flagmon.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gntsoft.flagmon.FMCommonFragment;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusOnClickListener;
import com.pluslibrary.utils.PlusToaster;

/**
 * Created by johnny on 15. 2. 27.
 */
public class PhotoFragment extends FMCommonFragment implements
        PlusOnGetDataListener {

    public PhotoFragment() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photo_detail,
                container, false);
        return rootView;
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

        mainImage.setOnTouchListener(new View.OnTouchListener() {
            public static final float MIN_DISTANCE = 150;
            public float x1;
            public float x2;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        float deltaX = x2 - x1;

                        if (Math.abs(deltaX) > MIN_DISTANCE)
                        {
                            // Left to Right swipe action
                            if (x2 > x1)
                            {
                                ((PlusSwipeDetector) mActivity).onLeftToRightSwiped();
                            }

                            // Right to left swipe action
                            else
                            {
                                ((PlusSwipeDetector) mActivity).onRightToLeftSwiped();
                            }

                        }
                        else
                        {
                            // consider as something else - a screen tap for example
                        }
                        break;
                }
                return true;
            }
        });
    }

    private void goToImageViewer() {
        Intent intent = new Intent(mActivity, ImageViewerActivity.class);
        intent.putExtra(FMConstants.KEY_IMAGE_URL, getArguments().getString(FMConstants.KEY_IMAGE_URL, ""));
        startActivity(intent);

    }

}
