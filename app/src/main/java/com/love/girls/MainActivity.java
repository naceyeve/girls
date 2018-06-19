package com.love.girls;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageView;
    private Button mButton;
    private ArrayList<String> mUrls;
    private PictureLoader mLoader;
    private int index = 0;
    private int page = 1;
    private Button mBt_fresh;
    private SisterApi mApi;
    private ArrayList<Sister> mSisters ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoader = new PictureLoader();
        mApi = new SisterApi();
        initData();
        initView();

    }

    private void initData() {
        mSisters = new ArrayList<>();
        new sisterAsycTask(page).execute();


    }

    private void initView() {
        mImageView = findViewById(R.id.imageView);
        mButton = findViewById(R.id.bt_next);
        mBt_fresh = findViewById(R.id.bt_fresh);
        mButton.setOnClickListener(this);
        mBt_fresh.setOnClickListener(this);

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_next:
                if(mSisters != null && !mSisters.isEmpty()){

                    if(index > 9){
                        index = 0;
                    }
                    mLoader.load(mImageView,mSisters.get(index).getUrl());
                    index++;
                }
                break;
            case R.id.bt_fresh:
                page++;
                new sisterAsycTask(page).execute();
                index = 0;
                break;
        }
    }

   public class sisterAsycTask extends AsyncTask<Void,Void,ArrayList<Sister>>{
        int page  = 0;

        public sisterAsycTask(int page){
            this.page = page;
        }

       @Override
       protected ArrayList<Sister> doInBackground(Void... voids) {
           ArrayList<Sister> sisters = mApi.fetchSister(10, page);
           return sisters;
       }

       @Override
       protected void onPostExecute(ArrayList<Sister> sisters) {
           super.onPostExecute(sisters);
            mSisters.clear();
            mSisters.addAll(sisters);
       }
   }
}
