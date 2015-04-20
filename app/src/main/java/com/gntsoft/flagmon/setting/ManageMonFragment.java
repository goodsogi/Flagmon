package com.gntsoft.flagmon.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gntsoft.flagmon.FMCommonFragment;
import com.gntsoft.flagmon.R;
import com.pluslibrary.utils.PlusOnClickListener;

/**
 * Created by johnny on 15. 4. 20.
 */
public class ManageMonFragment extends FMCommonFragment {

    public ManageMonFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showMonInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manage_mon,
                container, false);
        return rootView;
    }

    @Override
    protected void addListenerToButton() {
        final Button showMonInfo = (Button) mActivity.findViewById(R.id.showMonInfo);
        showMonInfo.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                showMonInfo();
            }
        });

        final Button buyItem = (Button) mActivity.findViewById(R.id.buyItem);
        buyItem.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                buyItem();
            }
        });


    }

    private void buyItem() {
        final Button buyItem = (Button) mActivity.findViewById(R.id.buyItem);
        buyItem.setSelected(true);

        final Button showMonInfo = (Button) mActivity.findViewById(R.id.showMonInfo);
        showMonInfo.setSelected(false);

        BuyItemFragment buyItemFragment = new BuyItemFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.containerManageMon, buyItemFragment)
                .commit();
    }

    private void showMonInfo() {
        final Button showMonInfo = (Button) mActivity.findViewById(R.id.showMonInfo);
        showMonInfo.setSelected(true);

        final Button buyItem = (Button) mActivity.findViewById(R.id.buyItem);
        buyItem.setSelected(false);


        ShowMonInfoFragment showMonInfoFragment = new ShowMonInfoFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.containerManageMon, showMonInfoFragment)
                .commit();
    }
}
