package com.gntsoft.flagmon.friend;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonAdapter;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.neighbor.NeighborListModel;
import com.pluslibrary.utils.PlusOnClickListener;
import com.pluslibrary.utils.PlusToaster;
import com.pluslibrary.utils.PlusViewHolder;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 3. 3.
 */
public class FriendListAdapter extends FMCommonAdapter<NeighborListModel> {


    public FriendListAdapter(Context context, ArrayList<NeighborListModel> datas) {
        super(context, R.layout.friend_list_item, datas);

    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.friend_list_item,
                    parent, false);
        }

        NeighborListModel data = mDatas.get(position);
        TextView name = PlusViewHolder.get(convertView, R.id.name);
        TextView content = PlusViewHolder.get(convertView, R.id.content);
        TextView date = PlusViewHolder.get(convertView, R.id.date);
        TextView reply = PlusViewHolder.get(convertView, R.id.reply);
        TextView pin = PlusViewHolder.get(convertView, R.id.pin);
        TextView distance = PlusViewHolder.get(convertView, R.id.distance);

        ImageView img = PlusViewHolder.get(convertView, R.id.img);
        img.setImageResource(data.getImg());


        ImageView bigImg = PlusViewHolder.get(convertView, R.id.big_img);
        bigImg.setImageResource(data.getBigImg());

        name.setText(data.getName());
        content.setText(data.getContent());
        date.setText(data.getRegisterDate());
        reply.setText(data.getReplyCount());
        pin.setText(data.getPinCount());
        distance.setText(data.getDistance());

        Button writeReply = PlusViewHolder.get(convertView, R.id.writeReply);
        writeReply.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                goToReply();
            }
        });

        Button getArticle = PlusViewHolder.get(convertView, R.id.getArticle);
        getArticle.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                doPin();
            }
        });

//        mImageLoader.displayImage(
//                BBGGConstants.IMG_URL_HEAD + mDatas.get(position).getImg01(), main_img,
//                mOption);





        return convertView;
    }

    private void doPin() {
        //구현!!
        PlusToaster.doIt(mContext, "준비중...");

    }

    private void goToReply() {
//구현!!
        PlusToaster.doIt(mContext, "준비중...");
    }


    private Bitmap getFrameImg(int imgId) {
        //마스킹
        Bitmap original = BitmapFactory.decodeResource(mContext.getResources(), imgId);
        Bitmap frame = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.thumbnail_2_0001);
        Bitmap mask = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.main_list_mask);
        Log.d("mask", "image witdh: " + mask.getWidth() + " height: " + mask.getHeight());
        Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(original, 0, 0, null);
        mCanvas.drawBitmap(mask, 0, 0, paint);
        mCanvas.drawBitmap(frame, 0, 0, null);
        paint.setXfermode(null);


        return  result;



    }





}
