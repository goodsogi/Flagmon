package com.gntsoft.flagmon.setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonFragment;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.main.MainActivity;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.ProfileModel;
import com.gntsoft.flagmon.server.ProfileParser;
import com.gntsoft.flagmon.server.ServerResultModel;
import com.gntsoft.flagmon.server.ServerResultParser;
import com.gntsoft.flagmon.utils.LoginChecker;
import com.pluslibrary.PlusConstants;
import com.pluslibrary.img.CameraAlbumActivity;
import com.pluslibrary.img.ImagePathFinder;
import com.pluslibrary.img.PiccasaImageSaver;
import com.pluslibrary.img.PlusImageConstants;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusImageByteConverter;
import com.pluslibrary.utils.PlusLogger;
import com.pluslibrary.utils.PlusOnClickListener;
import com.pluslibrary.utils.PlusToaster;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnny on 15. 4. 21.
 */
public class UserProfileFragment extends FMCommonFragment implements PlusOnGetDataListener, OnKeyBackPressedListener {
    private static final int GET_PROFILE = 22;
    private static final int UPDATE_PROFILE = 33;
    private static final int UPDATE_PROFILE_PHOTO = 45;
    private static final int ACITIVITY_CHANGE_NAME = 39;
    private static final int ACITIVITY_CHANGE_STATUS_MESSAGE = 43;
    final int DRAWABLE_RIGHT = 2;

    String[] menuItems = {"카메라", "앨범"};
    String[] sexs = {"남", "여"};

    public UserProfileFragment() {
    }

    /**
     * 이미지 가져오기
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PlusLogger.doIt("onActivityResult");
        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {

            // 카메라로 찍은 이미지 가져오기
            case PlusImageConstants.FROM_CAMERA:
                String imgUri = data
                        .getStringExtra(PlusImageConstants.EXTRA_IMAGE_PATH);

                showImage(imgUri);

                break;

            // 앨범에서 이미지 가져오기
            case PlusImageConstants.FROM_GALLERY:
                String uriString = data
                        .getStringExtra(PlusImageConstants.EXTRA_IMAGE_PATH);
                Uri uri = Uri.parse(uriString);

                // 카메라 앨범에서 가져온 이미지는 media store를 이용하여 경로 추출
                if (uriString.toString().contains("storage")) {
                    String imgUri2 = ImagePathFinder.getPathCameraAlbum(
                            mActivity, uri);
                    showImage(imgUri2);

                } else {
                    // "content://com.google.android.gallery3d.provider/picasa/item/5973539989398179218"
                    // 와 같은 경로를 가지는 이미지를 파일에 저장

                    PiccasaImageSaver saver = new PiccasaImageSaver(
                            mActivity);
                    showImage(saver.doIt(uri));
                }

                break;
            case ACITIVITY_CHANGE_NAME:
                String userName = data.getStringExtra(FMConstants.KEY_USER_NAME);
                showUserName(userName);

            case ACITIVITY_CHANGE_STATUS_MESSAGE:
                String statusMessage = data.getStringExtra(FMConstants.KEY_STATUS_MESSAGE);
                showStatusMessage(statusMessage);
        }

    }

    public void setUserAge(String userAge) {
        TextView birth = (TextView) mActivity.findViewById(R.id.age);
        birth.setText(formatBirth(userAge));
    }

    private void showStatusMessage(String statusMessage) {
        final TextView status = (TextView) mActivity.findViewById(R.id.userStatus);
        status.setText(statusMessage);
    }

    private void showUserName(String userName) {
        TextView name = (TextView) mActivity.findViewById(R.id.userName);
        name.setText(userName);
    }

    private void showImage(String imgUri) {
        ImageView imageView = (ImageView) mActivity.findViewById(R.id.userPic);
        imageView.setTag(imgUri);

        Bitmap bmp = BitmapFactory.decodeFile(imgUri);
        imageView.setImageBitmap(bmp);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //회원정보를 조회하여 화면에 표시!!
        if(LoginChecker.isLogIn(mActivity)) getProfileDataOnServer();


    }

    @Override
    public void onSuccess(Integer from, Object datas) {


        if (datas == null)
            return;
        switch (from) {


            case GET_PROFILE:
                ProfileModel model = new ProfileParser().doIt((String) datas);
                showProfile(model);
                setLogoutHelpText(model.getEmail());
                addListenerToLogoutButton();
                break;

            case UPDATE_PROFILE:
                ServerResultModel model3 = new ServerResultParser().doIt((String) datas);
                PlusToaster.doIt(mActivity, model3.getResult().equals("success") ? "프로필을 갱신했습니다" : "프로필을 갱신하지 못했습니다");


        }

    }

    private void addListenerToLogoutButton() {
        TextView logout = (TextView) mActivity.findViewById(R.id.logout);
        TextView logoutHelp = (TextView) mActivity.findViewById(R.id.logoutHelp);

        logout.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                showLogoutDialog();
            }
        });

        logoutHelp.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                showLogoutDialog();
            }
        });
    }

    private void showLogoutDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(mActivity, AlertDialog.THEME_HOLO_LIGHT);
        ab.setTitle("로그아웃하시겠습니까?");
        ab.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        logout();
                        PlusToaster.doIt(mActivity, "로그아웃되었습니다");
                   }
                }).setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
        ab.show();
    }

    private void showProfile(ProfileModel profileModel) {
        final EditText email = (EditText) mActivity.findViewById(R.id.userEmail);
        email.setText(profileModel.getEmail());

        email.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int leftEdgeOfRightDrawable = email.getRight()
                            - email.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                    // when EditBox has padding, adjust leftEdge like
                    // leftEdgeOfRightDrawable -= getResources().getDimension(R.dimen.edittext_padding_left_right);
                    if (event.getRawX() >= leftEdgeOfRightDrawable) {
                        // clicked on clear icon
                        email.setText("");
                        return true;
                    }
                }
                return false;
            }
        });

        final EditText phone = (EditText) mActivity.findViewById(R.id.phoneNo);
        phone.setText(profileModel.getPhone());

        phone.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int leftEdgeOfRightDrawable = phone.getRight()
                            - phone.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                    // when EditBox has padding, adjust leftEdge like
                    // leftEdgeOfRightDrawable -= getResources().getDimension(R.dimen.edittext_padding_left_right);
                    if (event.getRawX() >= leftEdgeOfRightDrawable) {
                        // clicked on clear icon
                        phone.setText("");
                        return true;
                    }
                }
                return false;
            }
        });

        final TextView name = (TextView) mActivity.findViewById(R.id.userName);
        name.setText(profileModel.getName());
        name.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt( ) {
                startChangeNameActivity(name.getText().toString());
            }
        });

        TextView sex = (TextView) mActivity.findViewById(R.id.sex);
        sex.setText(profileModel.getGender());
        sex.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                showSexDialog();
            }
        });


        final TextView status = (TextView) mActivity.findViewById(R.id.userStatus);
        status.setText(profileModel.getStatus());
        status.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                startChangeStatusMessageActivity(status.getText().toString());
            }
        });

        TextView birth = (TextView) mActivity.findViewById(R.id.age);
        birth.setText(formatBirth(profileModel.getBirth()));
        birth.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
               showAgePicker();
            }
        });

        ImageView pic = (ImageView) mActivity.findViewById(R.id.userPic);

        mImageLoader.displayImage(profileModel.getPhoto(), pic, mOption);

        pic.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                showPhotoPickerDialog();
            }
        });




    }

    private void showAgePicker() {
        PlusNumberPicker picker = new PlusNumberPicker(mActivity, this);
        picker.show();
    }

    private void showSexDialog() {

        AlertDialog.Builder ab = new AlertDialog.Builder(mActivity, AlertDialog.THEME_HOLO_LIGHT);
        ab.setTitle("선택해주세요.");
        ab.setItems(sexs, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                changeSex(whichButton);

            }
        }).setNegativeButton("닫기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        ab.show();
    }

    private void changeSex(int whichButton) {
        TextView sex = (TextView) mActivity.findViewById(R.id.sex);
        sex.setText(sexs[whichButton]);
    }

    private void startChangeStatusMessageActivity(String statusMessage) {
        Intent intent = new Intent(mActivity, ChangeStatusMessageActivity.class);
        intent.putExtra(FMConstants.KEY_STATUS_MESSAGE, statusMessage);
        startActivityForResult(intent,ACITIVITY_CHANGE_STATUS_MESSAGE);
    }

    private void startChangeNameActivity(String fullName) {
        String lastName = fullName.substring(0,1);
        String firstName = fullName.substring(1,3);
        Intent intent = new Intent(mActivity, ChangeNameActivity.class);
        intent.putExtra(FMConstants.KEY_LAST_NAME, lastName);
        intent.putExtra(FMConstants.KEY_FIRST_NAME, firstName);
        startActivityForResult(intent, ACITIVITY_CHANGE_NAME);

    }

    private void showPhotoPickerDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(mActivity, AlertDialog.THEME_HOLO_LIGHT);
        ab.setTitle("사진 선택");
        ab.setItems(menuItems, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                doMenu(whichButton);

            }
        }).setNegativeButton("닫기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
        ab.show();
    }

    private void doMenu(int whichButton) {
        switch (whichButton) {
            case 0:
                takePhotoWithCamera();
                break;
            case 1:
                getPhotoOnAlbum();
                break;
        }
    }

    private void getPhotoOnAlbum() {
        Intent intent = new Intent(mActivity,
                CameraAlbumActivity.class);
        intent.putExtra(PlusImageConstants.KEY_IMAGE_SOURCE,
                PlusImageConstants.SOURCE_GALLERY);
        startActivityForResult(intent, PlusImageConstants.FROM_GALLERY);
    }

    private void takePhotoWithCamera() {
        Intent intent = new Intent(mActivity,
                CameraAlbumActivity.class);
        intent.putExtra(PlusImageConstants.KEY_IMAGE_SOURCE,
                PlusImageConstants.SOURCE_CAMERA);
        startActivityForResult(intent, PlusImageConstants.FROM_CAMERA);
    }


    private String formatBirth(String birth) {
        try {

            String year = birth.substring(0, 3);
            String month = birth.substring(4, 5);
            String day = birth.substring(5, 7);

            return year + "년 " + month + "월 " + day + "일";
        } catch (Exception e) {

        }
        return "";

    }




    private void updateProfile() {

        EditText emailView = (EditText) mActivity.findViewById(R.id.userEmail);
        String email = emailView.getText().toString();



        EditText phoneView = (EditText) mActivity.findViewById(R.id.phoneNo);
        String phone = phoneView.getText().toString();



        TextView nameView = (TextView) mActivity.findViewById(R.id.userName);
        String name = nameView.getText().toString();


        TextView sexView = (TextView) mActivity.findViewById(R.id.sex);
        String sex = sexView.getText().toString();



        TextView statusView = (TextView) mActivity.findViewById(R.id.userStatus);
        String status = statusView.getText().toString();



        TextView birthView = (TextView) mActivity.findViewById(R.id.age);
        String birth = birthView.getText().toString();





        ImageView imageView = (ImageView) mActivity.findViewById(R.id.userPic);
        String imageUrl = (String) imageView.getTag();



        MultipartEntity entity = new MultipartEntity();
        Charset chars = Charset.forName(PlusConstants.SERVER_ENCODING_TYPE);
        try {
            entity.addPart("key", new StringBody(
                    getUserAuthKey(), chars));
            entity.addPart("user_email", new StringBody(
                    email, chars));
            entity.addPart("user_name", new StringBody(
                    name, chars));
            entity.addPart("user_gender", new StringBody(
                    sex, chars));
            entity.addPart("user_birth", new StringBody(
                    birth, chars));
            entity.addPart("user_phone", new StringBody(
                    phone, chars));
            entity.addPart("status_msg", new StringBody(
                    status, chars));

            if (imageUrl != null && !imageUrl.equals("")) {
//나중에 수정해야 함!!
                entity.addPart("photo",
                        PlusImageByteConverter.doIt(imageUrl));
            }

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//나중에 api url 수정해야 함!!
        new PlusHttpClient(mActivity, this, true).execute(UPDATE_PROFILE_PHOTO,
                FMApiConstants.UPDATE_PROFILE,
                new PlusInputStreamStringConverter(), entity);
    }


    private void getProfileDataOnServer() {
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("key", getUserAuthKey()));



        new PlusHttpClient(mActivity, this, false).execute(GET_PROFILE,
                FMApiConstants.GET_PROFILE, new PlusInputStreamStringConverter(),
                postParams);
    }

    private void setLogoutHelpText(String email) {
        TextView logoutHelp =(TextView) mActivity.findViewById(R.id.logoutHelp);
        String text = email + "계정을 로그아웃합니다.";
        logoutHelp.setText(text);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate( R.layout.fragment_user_profile, container, false);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).setOnKeyBackPressedListener(this);
    } // in SearchFrag

    @Override
    public void onBack() {
        PlusLogger.doIt("onBack");
        updateProfile();
        mActivity.getFragmentManager().beginTransaction()
                .replace(R.id.container_main, new SettingFragment())
                .commit();
    }

    @Override
    public void onDestroyView() {

        ((MainActivity) mActivity).setOnKeyBackPressedListener(null);
        super.onDestroyView();
    }

    @Override
    protected void addListenerToButton() {

        TextView logout =(TextView) mActivity.findViewById(R.id.logout);
        logout.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                logout();
            }
        });

        TextView logoutHelp =(TextView) mActivity.findViewById(R.id.logoutHelp);
        logoutHelp.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                logout();
            }
        });

    }

    private void logout() {
        SharedPreferences sharedPreference = mActivity.getSharedPreferences(
                FMConstants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sharedPreference.edit();
        e.putBoolean(FMConstants.KEY_IS_LOGIN, false);
        e.commit();

        ((MainActivity) mActivity).refreshActivity();

    }
}
