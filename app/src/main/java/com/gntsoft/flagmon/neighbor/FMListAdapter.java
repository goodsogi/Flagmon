package com.gntsoft.flagmon.neighbor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonAdapter;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.server.FMModel;
import com.gntsoft.flagmon.utils.FMPhotoResizer;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.pluslibrary.utils.PlusViewHolder;

import java.util.ArrayList;

public class FMListAdapter extends FMCommonAdapter<FMModel> {


    public FMListAdapter(Context context, ArrayList<FMModel> datas) {
        super(context, R.layout.neighbor_list_item, datas);

    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.neighbor_list_item,
                    parent, false);
        }

        FMModel data = mDatas.get(position);
        TextView title = PlusViewHolder.get(convertView, R.id.title);
        TextView content = PlusViewHolder.get(convertView, R.id.content);
        TextView distance = PlusViewHolder.get(convertView, R.id.distance);
        TextView reply = PlusViewHolder.get(convertView, R.id.reply);
        TextView pin = PlusViewHolder.get(convertView, R.id.pin);
        TextView registerDate = PlusViewHolder.get(convertView, R.id.registerDate);

        ImageView img = PlusViewHolder.get(convertView, R.id.img);

        title.setText(getTitleText(data));
        content.setText(data.getMemo());
        //거리 처리!!
        //distance.setText(data.());
        reply.setText(data.getReplyCount());
        pin.setText(data.getScrapCount());
        registerDate.setText(data.getRegisterDate());

        fetchImageFromServer(data, img);


        return convertView;
    }

    private SpannableString getTitleText(FMModel data) {
        SpannableString coloredString = null;
        if (data.getPostType().equals("0")) //0: 포스팅, 1: 앨범
        {
            String userName = data.getUserName();
            coloredString = new SpannableString(
                    userName);
            coloredString.setSpan(
                    new ForegroundColorSpan(Color
                            .parseColor("#4a83b7")),
                    0, userName.length(), 0);

        } else {
            String userName = data.getUserName();
            String albumName = data.getAlbumName();
            String originalString = userName + "의 앨범" + albumName;
            coloredString = new SpannableString(
                    originalString);
            coloredString.setSpan(
                    new ForegroundColorSpan(Color
                            .parseColor("#4a83b7")),
                    0, userName.length(), 0);

            coloredString.setSpan(
                    new ForegroundColorSpan(Color
                            .parseColor("#4a83b7")),
                    originalString.indexOf(albumName), albumName.length(), 0);


        }

        return coloredString;
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

                imageView.setImageBitmap(getFrameImg(bitmap, fmModel.getPostType()));

            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });


    }

    private Bitmap getFrameImg(Bitmap original, String postType) {
        //마스킹
        Bitmap scaledOriginal = FMPhotoResizer.doIt(original);
        Bitmap frame = BitmapFactory.decodeResource(mContext.getResources(), postType.equals("0") ? R.drawable.thumbnail_2_0001 : R.drawable.thumbnail_2_0002);
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