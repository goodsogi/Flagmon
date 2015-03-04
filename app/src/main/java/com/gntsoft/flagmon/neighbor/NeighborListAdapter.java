package com.gntsoft.flagmon.neighbor;

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
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonAdapter;
import com.gntsoft.flagmon.R;
import com.pluslibrary.utils.PlusViewHolder;

import java.util.ArrayList;

public class NeighborListAdapter extends FMCommonAdapter<NeighborListModel> {


    public NeighborListAdapter(Context context, ArrayList<NeighborListModel> datas) {
        super(context, R.layout.main_list_item, datas);

    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.main_list_item,
                    parent, false);
        }

        NeighborListModel data = mDatas.get(position);
        TextView title = PlusViewHolder.get(convertView, R.id.title);
        TextView content = PlusViewHolder.get(convertView, R.id.content);
        TextView time = PlusViewHolder.get(convertView, R.id.time);
        TextView reply = PlusViewHolder.get(convertView, R.id.reply);
        TextView pin = PlusViewHolder.get(convertView, R.id.pin);
        TextView registerDate = PlusViewHolder.get(convertView, R.id.registerDate);

        ImageView img = PlusViewHolder.get(convertView, R.id.img);
        img.setImageBitmap(getFrameImg(data.getImg()));

        title.setText(data.getTitle());
        content.setText(data.getContent());
        time.setText(data.getTime());
        reply.setText(data.getReplyCount());
        pin.setText(data.getPinCount());
        registerDate.setText(data.getRegisterDate());

//        mImageLoader.displayImage(
//                BBGGConstants.IMG_URL_HEAD + mDatas.get(position).getImg01(), main_img,
//                mOption);


        return convertView;
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