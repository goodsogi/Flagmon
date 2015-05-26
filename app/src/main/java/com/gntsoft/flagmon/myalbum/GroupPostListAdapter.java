package com.gntsoft.flagmon.myalbum;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonAdapter;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.server.FMModel;
import com.gntsoft.flagmon.utils.FMPhotoResizer;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.pluslibrary.utils.PlusViewHolder;

import java.util.ArrayList;

public class GroupPostListAdapter extends FMCommonAdapter<FMModel> {


    private final ArrayList<String> mSelectedPositions;

    public GroupPostListAdapter(Context context, ArrayList<FMModel> datas) {
        super(context, R.layout.group_post_list_item, datas);

        mSelectedPositions = new ArrayList<>();
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.group_post_list_item,
                    parent, false);
        }

        FMModel data = mDatas.get(position);
        RelativeLayout rowContainer = PlusViewHolder.get(convertView, R.id.rowContainer);
        rowContainer.setSelected(mSelectedPositions.contains(String.valueOf(position)));
        rowContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    v.setSelected(false);
                    mSelectedPositions.remove(String.valueOf(position));
                } else {
                    v.setSelected(true);
                    mSelectedPositions.add(String.valueOf(position));
                }
            }
        });

        TextView title = PlusViewHolder.get(convertView, R.id.title);
        TextView content = PlusViewHolder.get(convertView, R.id.content);
        TextView distance = PlusViewHolder.get(convertView, R.id.distance);
        TextView reply = PlusViewHolder.get(convertView, R.id.replyAlarm);
        TextView pin = PlusViewHolder.get(convertView, R.id.pin);
        TextView registerDate = PlusViewHolder.get(convertView, R.id.registerDate);

        ImageView img = PlusViewHolder.get(convertView, R.id.img);

        title.setText(data.getUserName() + "의 앨범 " + data.getAlbumName());
        content.setText(data.getMemo());
        //거리 처리!!
        //distance.setText(data.());
        reply.setText(data.getReplyCount());
        pin.setText(data.getScrapCount());
        registerDate.setText(data.getRegisterDate());

        fetchImageFromServer(data, img);


        return convertView;
    }

    public ArrayList<String> getSelectedPostIdxs() {
        ArrayList<String> idxs = new ArrayList<>();
        for (String position : mSelectedPositions) {
            idxs.add(mDatas.get(Integer.parseInt(position)).getIdx());
        }
        return idxs;
    }

    private void fetchImageFromServer(final FMModel fmModel, final ImageView imageView) {
        mImageLoader.loadImage(fmModel.getImgUrl(), mOption, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                imageView.setImageBitmap(getFrameImg(bitmap));

            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });


    }

    private Bitmap getFrameImg(Bitmap original) {
        //마스킹
        Bitmap scaledOriginal = FMPhotoResizer.doIt((Activity) mContext,original);
        Bitmap frame = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.thumbnail_2_0001);
        Bitmap mask = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.main_list_mask);
        Log.d("mask", "image witdh: " + mask.getWidth() + " height: " + mask.getHeight());
        Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(scaledOriginal, 0, 0, null);
        mCanvas.drawBitmap(mask, 0, 0, paint);
        mCanvas.drawBitmap(frame, 0, 0, null);
        paint.setXfermode(null);


        return result;


    }


}