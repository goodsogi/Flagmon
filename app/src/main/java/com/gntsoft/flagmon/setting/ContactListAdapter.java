package com.gntsoft.flagmon.setting;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.ServerResultModel;
import com.gntsoft.flagmon.server.ServerResultParser;
import com.gntsoft.flagmon.utils.LoginChecker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
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
 * Created by johnny on 15. 3. 12.
 */
public class ContactListAdapter extends CursorAdapter implements
        PlusOnGetDataListener {
    private static final int SEND_FRIEND_REQUEST = 0;
    private final Context mContext;
    // 이미지 다운로드
    protected ImageLoader mImageLoader;
    protected DisplayImageOptions mOption;

    public ContactListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        mContext = context;
        // UIL 초기화
        mImageLoader = ImageLoader.getInstance();

        mOption = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(com.pluslibrary.R.drawable.empty_photo)
                .showImageOnFail(com.pluslibrary.R.drawable.empty_photo).cacheInMemory(true)
                .cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return View.inflate(context, R.layout.request_friend_list_item, null);
    }

    @Override
    public void bindView(View convertView, Context context, Cursor cursor) {

        int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));


        TextView nameView = PlusViewHolder.get(convertView, R.id.name);


        ImageView img = PlusViewHolder.get(convertView, R.id.img);
        // retrieve the contact photo as a Bitmap
        Uri uri = ContentUris.withAppendedId(Contacts.People.CONTENT_URI, id);
        Bitmap bitmap = Contacts.People.loadContactPhoto(context, uri, R.drawable.empty_photo, null);

        // set it here in the ImageView
        img.setImageBitmap(bitmap);

        String name = cursor.getString(cursor.getColumnIndex(Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.HONEYCOMB ?
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                ContactsContract.Contacts.DISPLAY_NAME));

        nameView.setText(name);

        Button sendFriendRequest = PlusViewHolder.get(convertView, R.id.sendFriendRequest);
        sendFriendRequest.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                sendFriendRequest();
            }
        });
    }

    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null)
            return;
        switch (from) {
            case SEND_FRIEND_REQUEST:
                ServerResultModel model = new ServerResultParser().doIt((String) datas);
                PlusToaster.doIt(mContext, model.getResult().equals("success") ? "친구 신청을 보냈습니다" : "친구 신청을 보내지 못했습니다");
                if (model.getResult().equals("success")) {
                    //추가 액션??
                }
                break;
        }

    }

    private void sendFriendRequest() {
        //수정!!
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("list_menu", FMConstants.DATA_TAB_FRIEND));
        if (LoginChecker.isLogIn((android.app.Activity) mContext)) {
            postParams.add(new BasicNameValuePair("key", ((FMCommonActivity) mContext).getUserAuthKey()));
        }


        new PlusHttpClient((android.app.Activity) mContext, this, false).execute(SEND_FRIEND_REQUEST,
                FMApiConstants.SEND_FRIEND_REQUEST, new PlusInputStreamStringConverter(),
                postParams);
    }

}
