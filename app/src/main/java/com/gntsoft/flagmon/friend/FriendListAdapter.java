package com.gntsoft.flagmon.friend;

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

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMCommonAdapter;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.comment.CommentActivity;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.FMModel;
import com.gntsoft.flagmon.server.ServerResultModel;
import com.gntsoft.flagmon.server.ServerResultParser;
import com.gntsoft.flagmon.utils.LoginChecker;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusOnClickListener;
import com.pluslibrary.utils.PlusToaster;
import com.pluslibrary.utils.PlusViewHolder;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnny on 15. 3. 3.
 */
public class FriendListAdapter extends FMCommonAdapter<FMModel> implements
        PlusOnGetDataListener {

    private static final int SCRAP_THIS = 0;

    public FriendListAdapter(Context context, ArrayList<FMModel> datas) {
        super(context, R.layout.friend_list_item, datas);

    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.friend_list_item,
                    parent, false);
        }

        final FMModel data = mDatas.get(position);
        TextView name = PlusViewHolder.get(convertView, R.id.name);
        TextView content = PlusViewHolder.get(convertView, R.id.content);
        TextView date = PlusViewHolder.get(convertView, R.id.date);
        TextView reply = PlusViewHolder.get(convertView, R.id.reply);
        TextView pin = PlusViewHolder.get(convertView, R.id.pin);
        TextView distance = PlusViewHolder.get(convertView, R.id.distance);

        ImageView img = PlusViewHolder.get(convertView, R.id.img);
        mImageLoader.displayImage(
                data.getImgUrl(), img,
                mOption);

        //큰이미지 url 필요!!
        ImageView bigImg = PlusViewHolder.get(convertView, R.id.big_img);
        mImageLoader.displayImage(
                data.getImgUrl(), bigImg,
                mOption);

        ImageView scrapPost = PlusViewHolder.get(convertView, R.id.scrapPost);
        ImageView myPost = PlusViewHolder.get(convertView, R.id.myPost);

        //내가 작정한 글인지 스크랩한 글인지 표시
        //scrapPost.setVisibility(View.VISIBLE);
        //myPost.setVisibility(View.VISIBLE);

        name.setText(data.getUserName());
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

        Button scrapThis = PlusViewHolder.get(convertView, R.id.scrapThis);
        scrapThis.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                scrapThis();
            }
        });

//        mImageLoader.displayImage(
//                BBGGConstants.IMG_URL_HEAD + mDatas.get(position).getImg01(), main_img,
//                mOption);


        return convertView;
    }

    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null)
            return;
        switch (from) {
            case SCRAP_THIS:
                ServerResultModel model = new ServerResultParser().doIt((String) datas);
                PlusToaster.doIt(mContext, model.getResult().equals("success") ? "스크했습니다" : "스크랩하지 못했습니다");
                if (model.getResult().equals("success")) {
                    //추가 액션??
                }
                break;

        }

    }

    private void scrapThis() {
        //수정!!
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("list_menu", FMConstants.DATA_TAB_FRIEND));
        if (LoginChecker.isLogIn((android.app.Activity) mContext)) {
            postParams.add(new BasicNameValuePair("key", ((FMCommonActivity) mContext).getUserAuthKey()));
        }


        new PlusHttpClient((android.app.Activity) mContext, this, false).execute(SCRAP_THIS,
                FMApiConstants.SCRAP_THIS, new PlusInputStreamStringConverter(),
                postParams);

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
