package com.gntsoft.flagmon.setting;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.vending.billing.IInAppBillingService;
import com.gntsoft.flagmon.FMCommonFragment;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.inapp.IabHelper;
import com.gntsoft.flagmon.inapp.IabResult;
import com.gntsoft.flagmon.inapp.Purchase;
import com.pluslibrary.utils.PlusOnClickListener;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 4. 20.
 */
public class BuyItemFragment extends FMCommonFragment {


    protected static final String TAG = "plus";
    private final String SKU_ITEM10 = "item10";
    private final String SKU_ITEM50 = "item50";
    private final String SKU_ITEM100 = "item100";
    private final String SKU_ITEM500 = "item500";
    IInAppBillingService mService;
    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };
    IabHelper mHelper;
    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase
                    + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null)
                return;

            // We know this is the "gas" sku because it's the only one we
            // consume,
            // so we don't check which sku was consumed. If you have more than
            // one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in
                // our
                // game world's logic, which in our case means filling the gas
                // tank a bit
                Log.d(TAG, "Consumption successful. Provisioning.");

                //아이템 구매 성공 후 처리!!

            } else {
                complain("Error while consuming: " + result);
            }
            // updateUi();
            // setWaitScreen(false);
            Log.d(TAG, "End consumption flow.");
        }
    };
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {

            Log.d(TAG, "Purchase finished: " + result + ", purchase: "
                    + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null)
                return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
                // setWaitScreen(false);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                // setWaitScreen(false);
                return;
            }

            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(SKU_ITEM10)) {
                // bought 1/4 tank of gas. So consume it.
                Log.d(TAG, "Purchase is item10.");
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            } else if (purchase.getSku().equals(SKU_ITEM50)) {
                // bought 1/4 tank of gas. So consume it.
                Log.d(TAG, "Purchase is item50.");
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            } else if (purchase.getSku().equals(SKU_ITEM100)) {
                // bought 1/4 tank of gas. So consume it.
                Log.d(TAG, "Purchase is item100.");
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            } else if (purchase.getSku().equals(SKU_ITEM500)) {
                // bought 1/4 tank of gas. So consume it.
                Log.d(TAG, "Purchase is item500.");
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            }

            // 여기서 아이템 추가 해주시면 됩니다.
            // 만약 서버로 영수증 체크후에 아이템 추가한다면, 서버로 purchase.getOriginalJson() ,
            // purchase.getSignature() 2개 보내시면 됩니다.
        }
    };

    public BuyItemFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initInappService();
    }

    public void buy(String id_item) {
        // Var.ind_item = index;
        try {
            Bundle buyIntentBundle = mService.getBuyIntent(3, mActivity.getPackageName(),
                    id_item, "inapp", "test");
            PendingIntent pendingIntent = buyIntentBundle
                    .getParcelable("BUY_INTENT");

            if (pendingIntent != null) {
                // startIntentSenderForResult(pendingIntent.getIntentSender(),
                // 1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                // Integer.valueOf(0));
                mHelper.launchPurchaseFlow(mActivity, mActivity.getPackageName(), 1001,
                        mPurchaseFinishedListener, "test");
                // 위에 두줄 결제호출이 2가지가 있는데 위에것을 사용하면 결과가 onActivityResult 메서드로 가고,
                // 밑에것을 사용하면 OnIabPurchaseFinishedListener 메서드로 갑니다. (참고하세요!)
            } else {
                // 결제가 막혔다면
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void alreadyPurchaseItems() {
        try {
            Bundle ownedItems = mService.getPurchases(3, mActivity.getPackageName(),
                    "inapp", null);
            int response = ownedItems.getInt("RESPONSE_CODE");
            if (response == 0) {
                ArrayList<String> purchaseDataList = ownedItems
                        .getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                String[] tokens = new String[purchaseDataList.size()];
                for (int i = 0; i < purchaseDataList.size(); ++i) {
                    String purchaseData = (String) purchaseDataList.get(i);
                    JSONObject jo = new JSONObject(purchaseData);
                    tokens[i] = jo.getString("purchaseToken");
                    // 여기서 tokens를 모두 컨슘 해주기
                    mService.consumePurchase(3, mActivity.getPackageName(), tokens[i]);
                }
            }

            // 토큰을 모두 컨슘했으니 구매 메서드 처리
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mServiceConn != null) {
            mActivity.unbindService(mServiceConn);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_buy_item,
                container, false);
        return rootView;


    }

    @Override
    protected void addListenerToButton() {

        final Button item10 = (Button) mActivity.findViewById(R.id.item10);
        item10.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                buyItem(item10);
            }
        });

        final Button item50 = (Button) mActivity.findViewById(R.id.item50);
        item50.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                buyItem(item50);
            }
        });


        final Button item100 = (Button) mActivity.findViewById(R.id.item100);
        item100.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                buyItem(item100);
            }
        });

        final Button item500 = (Button) mActivity.findViewById(R.id.item500);
        item500.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                buyItem(item500);
            }
        });

    }

    private void initInappService() {
        Intent serviceIntent = new Intent(
                "com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        mActivity.bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
        // bindService(new Intent(
        // "com.android.vending.billing.InAppBillingService.BIND"),
        // mServiceConn, Context.BIND_AUTO_CREATE);

        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhcf3cQ/KtQUeoZPzjXBLY3cVRqElPPGm+sly0lULnltzTNyddtBrmMSz77MjpGcfuPegYPne2IYCdHiRafPG/Yrb3D4xmQqj3d5NlEHQaTLnNEgq+gmgIFIE1Ov70jKMtgBINJZxF9ISkv03SUfIgzNF+q2MVRzuUNsx4SDoJvXGO6pX7/kWd2saI6FGUA8HwWFgbh4WRGMFkVhzBg23GUkkDl0yNRaOwCeWuNy70sKkb6crSlfN6Bn5GrKkHXq1xse6+IssaVY6lCAxMKJqxWhUFpMh3b6nAqpjcfTAdvrAZMkts5NAtKXZmyvY2p+pVELDie4l9fJGa8RjUVlx5QIDAQAB";                                                                                                                                                                                                                                                                                                                                                                                                            // 발급받은
        // 바이너리키를
        // 입력해줍니다)

        mHelper = new IabHelper(mActivity, base64EncodedPublicKey);
        mHelper.enableDebugLogging(true);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null)
                    return;

                // alreadyPurchaseItems(); // AlreadyPurchaseItems(); 메서드는 구매목록을
                // 초기화하는 메서드입니다. v3으로 넘어오면서 구매기록이 모두 남게
                // 되는데 재구매 가능한 상품( 게임에서는 코인같은아이템은 ) 구매후
                // 삭제해주어야 합니다. 이 메서드는 상품 구매전 혹은 후에 반드시
                // 호출해야합니다. ( 재구매가 불가능한 1회성 아이템의경우 호출하면
                // 안됩니다 )

            }
        });

    }

    private void buyItem(View v) {
        String itemId = null;
        switch (v.getId()) {
            case R.id.item10:
                itemId = "item10";
                break;
            case R.id.item50:
                itemId = "item50";
                break;
            case R.id.item100:
                itemId = "item100";
                break;
            case R.id.item500:
                itemId = "item500";
                break;

        }

        buy(itemId);
    }

    /**
     * Verifies the developer payload of a purchase.
     */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

		/*
         * TODO: verify that the developer payload of the purchase is correct.
		 * It will be the same one that you sent when initiating the purchase.
		 *
		 * WARNING: Locally generating a random string when starting a purchase
		 * and verifying it here might seem like a good approach, but this will
		 * fail in the case where the user purchases an item on one device and
		 * then uses your app on a different device, because on the other device
		 * you will not have access to the random string you originally
		 * generated.
		 *
		 * So a good developer payload has these characteristics:
		 *
		 * 1. If two different users purchase an item, the payload is different
		 * between them, so that one user's purchase can't be replayed to
		 * another user.
		 *
		 * 2. The payload must be such that you can verify it even when the app
		 * wasn't the one who initiated the purchase flow (so that items
		 * purchased by the user on one device work on other devices owned by
		 * the user).
		 *
		 * Using your own server to store and verify developer payloads across
		 * app installations is recommended.
		 */

        return true;
    }

    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(mActivity);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }
}