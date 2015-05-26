package com.gntsoft.flagmon.myalbum;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.text.Layout;
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
import com.gntsoft.flagmon.main.MainActivity;
import com.gntsoft.flagmon.server.FMModel;
import com.pluslibrary.utils.PlusOnClickListener;
import com.pluslibrary.utils.PlusViewHolder;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 3. 3.
 */
public class MyAlbumListAdapter extends FMCommonAdapter<FMModel> {


    private static final int CONTENT_LINES = 6;
    private static final int ELLIPSIZED_TEXT = 80;
    private final String testText = "그 부분을 제 소스에 어느 부분에 넣어야 하는지 좀 알려주실수 있나요?\n" +
            "위에 첨부된 소스를 한번 살펴봐주세요.\n" +
            "\n" +
            "protected void onDraw(Canvas canvas) {\n" +
            "        if(stroke) {\n" +
            "            ColorStateList states = getTextColors();\n" +
            "            getPaint().setStyle(Style.STROKE);\n" +
            "            getPaint().setStrokeWidth(strokeWidth);\n";


    public MyAlbumListAdapter(Context context, ArrayList<FMModel> datas) {
        super(context, R.layout.my_album_list_item, datas);

    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.my_album_list_item,
                    parent, false);
        }

        final FMModel data = mDatas.get(position);
        final TextView content = PlusViewHolder.get(convertView, R.id.content);
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


        final TextView viewMore = PlusViewHolder.get(convertView, R.id.view_more);
        if(isEllipsized(data.getMemo())) {
            viewMore.setVisibility(View.VISIBLE);
            viewMore.setOnClickListener(new PlusOnClickListener() {
                @Override
                protected void doIt() {
                    viewMore.setVisibility(View.GONE);
                    content.setLines(CONTENT_LINES);
                }
            });
        }


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

//    public boolean isEllipsized(TextView textView) {
//        //layout이 null 오류발생
//        Layout layout = textView.getLayout();
//        int ellipsisCount = layout.getEllipsisCount(CONTENT_LINES-1);
//            return ellipsisCount > 0;
//    }

    public boolean isEllipsized(String s) {
        //layout이 null 오류발생
        return s.length() > ELLIPSIZED_TEXT ;
    }


    private void buryTreasure(String idx) {

        Intent intent = new Intent(mContext, BuryTreasureActivity.class);
        intent.putExtra(FMConstants.KEY_POST_IDX, idx);
        mContext.startActivity(intent);

        ((MainActivity)mContext).setBurryTreasureFlag(true);


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
