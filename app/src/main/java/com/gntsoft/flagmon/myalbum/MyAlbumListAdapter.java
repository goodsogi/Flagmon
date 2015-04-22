package com.gntsoft.flagmon.myalbum;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonAdapter;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.comment.CommentActivity;
import com.gntsoft.flagmon.server.FMModel;
import com.pluslibrary.utils.PlusOnClickListener;
import com.pluslibrary.utils.PlusViewHolder;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 3. 3.
 */
public class MyAlbumListAdapter extends FMCommonAdapter<FMModel> {


    public MyAlbumListAdapter(Context context, ArrayList<FMModel> datas) {
        super(context, R.layout.my_album_list_item, datas);

    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.my_album_list_item,
                    parent, false);
        }

        final FMModel data = mDatas.get(position);
        TextView content = PlusViewHolder.get(convertView, R.id.content);
        TextView date = PlusViewHolder.get(convertView, R.id.date);
        TextView reply = PlusViewHolder.get(convertView, R.id.replyAlarm);
        TextView pin = PlusViewHolder.get(convertView, R.id.pin);
        TextView distance = PlusViewHolder.get(convertView, R.id.distance);
//큰이미지 url 필요!!
        ImageView bigImg = PlusViewHolder.get(convertView, R.id.big_img);
        mImageLoader.displayImage(
                data.getImgUrl(), bigImg,
                mOption);

        content.setText(data.getMemo());
        date.setText(data.getRegisterDate());
        reply.setText(data.getReplyCount());
        pin.setText(data.getScrapCount());
        //거리 처리!!
        //distance.setText(data.getDistance());

        Button writeReply = PlusViewHolder.get(convertView, R.id.writeReply);
        writeReply.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                launchCommentActivity(data.getIdx());
            }
        });

        Button buryTreasure = PlusViewHolder.get(convertView, R.id.buryTreasure);
        buryTreasure.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                buryTreasure(data.getIdx());
            }
        });

//        mImageLoader.displayImage(
//                BBGGConstants.IMG_URL_HEAD + mDatas.get(position).getImg01(), main_img,
//                mOption);


        return convertView;
    }


    private void buryTreasure(String idx) {

        Intent intent = new Intent(mContext, BuryTreasureActivity.class);
        intent.putExtra(FMConstants.KEY_POST_IDX, idx);
        mContext.startActivity(intent);


    }

    private void launchCommentActivity(String idx) {
        Intent intent = new Intent(mContext, CommentActivity.class);
        intent.putExtra(FMConstants.KEY_POST_IDX, idx);
        mContext.startActivity(intent);

    }

    private Bitmap getFrameImg(int imgId) {
        //마스킹
        Bitmap original = BitmapFactory.decodeResource(mContext.getResources(), imgId);
        Bitmap frame = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.thumbnail_2_0001);
        Bitmap mask = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.main_list_mask);
        Log.d("mask", "image witdh: " + mask.getWidth() + " height: " + mask.getHeight());
        Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(original, 0, 0, null);
        mCanvas.drawBitmap(mask, 0, 0, paint);
        mCanvas.drawBitmap(frame, 0, 0, null);
        paint.setXfermode(null);


        return result;


    }


}
