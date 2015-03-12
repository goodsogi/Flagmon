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

import com.gntsoft.flagmon.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.pluslibrary.utils.PlusOnClickListener;
import com.pluslibrary.utils.PlusToaster;
import com.pluslibrary.utils.PlusViewHolder;

/**
 * Created by johnny on 15. 3. 12.
 */
public class ContactListAdapter extends CursorAdapter {

    private final Context mContext;
    // 이미지 다운로드
    protected ImageLoader mImageLoader;
    protected DisplayImageOptions mOption;

    public ContactListAdapter(Context context, Cursor cursor) {
        super(context, cursor,0);
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

        Button sendRequest = PlusViewHolder.get(convertView, R.id.sendRequest);
        sendRequest.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                sendRequest();
            }
        });
    }

    private void sendRequest() {
        //구현!!
        PlusToaster.doIt(mContext, "준비중...");
    }
}
