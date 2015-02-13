package com.gntsoft.flagmon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by johnny on 15. 2. 12.
 */
public class DetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        makeGridView();
    }

    private void makeGridView() {

        ArrayList<Integer> imgs = new ArrayList<>();


        imgs.add(R.drawable.sandarapark);
        imgs.add(R.drawable.sandarapark2);
        imgs.add(R.drawable.sandarapark3);
        imgs.add(R.drawable.sandarapark4);
        imgs.add(R.drawable.sandarapark5);
        imgs.add(R.drawable.sandarapark);
        imgs.add(R.drawable.sandarapark2);
        imgs.add(R.drawable.sandarapark3);
        imgs.add(R.drawable.sandarapark4);
        imgs.add(R.drawable.sandarapark5);
        imgs.add(R.drawable.sandarapark);
        imgs.add(R.drawable.sandarapark2);
        imgs.add(R.drawable.sandarapark3);
        imgs.add(R.drawable.sandarapark4);
        imgs.add(R.drawable.sandarapark5);


        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new DetailGridviewAdapter(this, imgs));
    }

    public void goToReply(View v) {
        Intent intent = new Intent(this, ReplyActivity.class);
        startActivity(intent); 

    }

}
